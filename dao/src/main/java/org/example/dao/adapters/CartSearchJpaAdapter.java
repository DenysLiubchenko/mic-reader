package org.example.dao.adapters;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
        CriteriaQuery<CartEntity> cq = cb.createQuery(CartEntity.class);
        Root<CartEntity> root = cq.from(CartEntity.class);

        // Join with the products table
        Join<CartEntity, ProductItemEntity> pij = root.join("products", JoinType.LEFT);
        Join<ProductItemEntity, ProductEntity> productJoin = pij.join("product", JoinType.LEFT);

        // Select
        cq.select(root);

        // Where
        if (productNameSearchQuery != null) {
            List<Long> cartIdsWithProductByName = productJpaAdapter.findAllCartIdsWithProductByName(productNameSearchQuery);
            cq.where(pij.get("cart").get("id").in(cartIdsWithProductByName));
        }

        // Group by
        cq.groupBy(pij.get("cart").get("id"), root.get("id"));

        // Having
        Expression<BigDecimal> totalCartCost = cb.sum(cb.prod(productJoin.get("cost"), pij.get("quantity")));
        if (totalCostFrom != null && totalCostTo != null) {
            cq.having(cb.between(totalCartCost, totalCostFrom, totalCostTo));
        } else if (totalCostFrom != null) {
            cq.having(cb.greaterThan(totalCartCost, totalCostFrom));
        } else if (totalCostTo != null) {
            cq.having(cb.lessThan(totalCartCost, totalCostTo));
        }

        // Order by
        var minProductName = cb.min(productJoin.get("name"));
        var maxProductName = cb.max(productJoin.get("name"));
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

        // Count
        Long totalCount = getTotalCount(cq);
        if (totalCount == 0) {
            return Page.empty();
        }

        TypedQuery<CartEntity> query = em.createQuery(cq);

        // Pagination
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<CartEntity> resultList = query.getResultList();
        List<Long> cartIds = resultList.stream().map(CartEntity::getId).toList();

        // Fetch data
        cartJpaAdapter.findAllByIdsFetchDiscounts(cartIds);
        cartJpaAdapter.findAllByIdsFetchProducts(cartIds);

        return new PageImpl<>(resultList, pageable, totalCount);
    }

    private Long getTotalCount(CriteriaQuery<CartEntity> cq) {
        CriteriaQuery<Long> countQuery = ((JpaCriteriaQuery<CartEntity>) cq).createCountQuery();
        TypedQuery<Long> query = em.createQuery(countQuery);
        return query.getSingleResult();
    }
}
