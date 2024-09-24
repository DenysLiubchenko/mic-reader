package org.example.dao.adapters;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.example.dao.entity.CartEntity;
import org.example.dao.entity.ProductEntity;
import org.example.dao.entity.ProductItemEntity;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class CartSearchJpaAdapter {
    private final EntityManager em;
    private final CriteriaBuilder cb;
    private final ProductJpaAdapter productJpaAdapter;
    private final CartJpaAdapter cartJpaAdapter;

    @Autowired
    public CartSearchJpaAdapter(EntityManager em, ProductJpaAdapter productJpaAdapter, CartJpaAdapter cartJpaAdapter) {
        this.em = em;
        cb = em.getCriteriaBuilder();
        this.productJpaAdapter = productJpaAdapter;
        this.cartJpaAdapter = cartJpaAdapter;
    }

    public Page<CartEntity> search(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, Pageable pageable) {
        List<Long> cartIdsWithProductByName = productJpaAdapter.findAllCartIdsWithProductByName(productNameSearchQuery);

        Long totalCount = getTotalCount(totalCostFrom, totalCostTo, cartIdsWithProductByName);
        if (totalCount == 0) {
            return Page.empty();
        }

        CriteriaQuery<CartEntity> cq = cb.createQuery(CartEntity.class);
        Root<ProductItemEntity> productItemRoot = cq.from(ProductItemEntity.class);

        // Join with the products table
        Join<ProductItemEntity, ProductEntity> productJoin = productItemRoot.join("product");
        Join<ProductItemEntity, CartEntity> cartJoin = productItemRoot.join("cart");

        cq.select(productItemRoot.get("cart"));

        cq.where(productItemRoot.get("cart").get("id").in(cartIdsWithProductByName));
        cq.groupBy(productItemRoot.get("cart").get("id"), cartJoin.get("id"));

        Expression<BigDecimal> totalCartCost = cb.sum(cb.prod(productJoin.get("cost"), productItemRoot.get("quantity")));
        var minProductName = cb.min(productJoin.get("name"));
        var maxProductName = cb.max(productJoin.get("name"));

        // Having SUM of total_cart_cost between totalFrom and totalTo
        if (totalCostFrom != null && totalCostTo != null) {
            cq.having(cb.between(totalCartCost, totalCostFrom, totalCostTo));
        } else if (totalCostFrom != null) {
            cq.having(cb.greaterThan(totalCartCost, totalCostFrom));
        } else if (totalCostTo != null) {
            cq.having(cb.lessThan(totalCartCost, totalCostTo));
        }

        // Set the order by clause
        Sort pageableSort = pageable.getSort();
        Optional<Sort.Order> optionalOrder = pageableSort.stream().filter(o -> o.getProperty().equals("product.name")).findFirst();
        if (optionalOrder.isPresent()) {
            Sort.Order order = optionalOrder.get();
            if (order.isDescending()) {
                cq.orderBy(cb.desc(maxProductName));
            } else {
                cq.orderBy(cb.asc(minProductName));
            }
        }

        // Execute query
        TypedQuery<CartEntity> query = em.createQuery(cq);

        // Pagination
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<CartEntity> resultList = query.getResultList();
        List<Long> cartIds = resultList.stream().map(CartEntity::getId).toList();

        cartJpaAdapter.findAllByIdsFetchDiscounts(cartIds);
        cartJpaAdapter.findAllByIdsFetchProducts(cartIds);

        return new PageImpl<>(resultList, pageable, totalCount);
    }

    private Long getTotalCount(BigDecimal totalCostFrom, BigDecimal totalCostTo, List<Long> cartIdsWithProductByName) {
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ProductItemEntity> productItemRoot = cq.from(ProductItemEntity.class);

        // Join with the products table
        Join<ProductItemEntity, ProductEntity> productJoin = productItemRoot.join("product");

        cq.select(cb.count(productItemRoot.get("cart")));

        cq.where(productItemRoot.get("cart").get("id").in(cartIdsWithProductByName));
        cq.groupBy(productItemRoot.get("cart").get("id"));

        Expression<BigDecimal> totalCartCost = cb.sum(cb.prod(productJoin.get("cost"), productItemRoot.get("quantity")));

        // Having SUM of total_cart_cost between totalFrom and totalTo
        if (totalCostFrom != null && totalCostTo != null) {
            cq.having(cb.between(totalCartCost, totalCostFrom, totalCostTo));
        } else if (totalCostFrom != null) {
            cq.having(cb.greaterThan(totalCartCost, totalCostFrom));
        } else if (totalCostTo != null) {
            cq.having(cb.lessThan(totalCartCost, totalCostTo));
        }

        // Execute query
        CriteriaQuery<Long> countQuery = ((JpaCriteriaQuery<Long>) cq).createCountQuery();
        TypedQuery<Long> query = em.createQuery(countQuery);
        return query.getSingleResult();
    }
}
