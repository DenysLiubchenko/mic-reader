package org.example.dao.adapters;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLTemplates;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.example.dao.entity.CartEntity;
import org.example.dao.entity.QCartEntity;
import org.example.dao.entity.QProductEntity;
import org.example.dao.entity.QProductItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartSearchJpaAdapter {
    private final EntityManager em;
    private final CartJpaAdapter cartJpaAdapter;
    private final String TOTAL_FIELD = "total";
    private final String MIN_NAME_FIELD = "min_name";
    private final String MAX_NAME_FIELD = "max_name";

    public Page<CartEntity> search(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, Pageable pageable) {
        SQLTemplates sqlTemplates = PostgreSQLTemplates.builder().printSchema().build();
        QCartEntity cart = QCartEntity.cartEntity;
        QProductItemEntity productItem = QProductItemEntity.productItemEntity;
        QProductEntity product = QProductEntity.productEntity;

        // Define the alias for the CTE
        PathBuilder<Object> cteAlias = new PathBuilder<>(Object.class, "cte");

        // Define window functions
        var totalCost = SQLExpressions.sum(product.cost.multiply(productItem.quantity))
                .over().partitionBy(cart.id);
        var minName = SQLExpressions.min(product.name).over().partitionBy(cart.id);
        var maxName = SQLExpressions.max(product.name).over().partitionBy(cart.id);

        // Subquery for the CTE
        JPASQLQuery<?> cteSubquery = new JPASQLQuery<>(em, sqlTemplates)
                .select(cart.id, minName.as(MIN_NAME_FIELD), maxName.as(MAX_NAME_FIELD), totalCost.as(TOTAL_FIELD))
                .distinct()
                .from(cart)
                .leftJoin(productItem).on(Expressions.numberPath(Long.class, productItem, "cart_id").eq(cart.id))
                .leftJoin(product).on(product.id.eq(Expressions.numberPath(Long.class, productItem, "product_id")));

        if (productNameSearchQuery != null && !productNameSearchQuery.isEmpty()) {
            cteSubquery.where(product.name.likeIgnoreCase("%" + productNameSearchQuery + "%"));
        }

        BooleanExpression whereTotalBetween = whereTotalBetween(totalCostFrom, totalCostTo);

        // Total count
        JPASQLQuery<CartEntity> countQuery = new JPASQLQuery<>(em, sqlTemplates);
        countQuery.select(cteAlias.get("id"))
                .with(cteAlias, cteSubquery)
                .from(cteAlias)
                .where(whereTotalBetween);

        long total = countQuery.fetchCount();
        if (total == 0) {
            return Page.empty(pageable);
        }

        // Main query using the CTE
        JPASQLQuery<CartEntity> mainQuery = new JPASQLQuery<>(em, sqlTemplates);
        mainQuery.select(cart)
                .with(cteAlias, cteSubquery)
                .from(cart)
                .where(whereTotalBetween)
                .rightJoin(cteAlias).on(cart.id.eq(cteAlias.get("id")))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        OrderSpecifier<String> asc = orderByName(pageable);
        if (asc != null) {
            mainQuery.orderBy(asc);
        }

        List<CartEntity> content = mainQuery.fetch();

        // Fetch data
        List<Long> cartIds = content.stream().map(CartEntity::getId).toList();
        cartJpaAdapter.findAllByIdsFetchDiscounts(cartIds);
        cartJpaAdapter.findAllByIdsFetchProducts(cartIds);

        return new PageImpl<>(content, pageable, total);
    }


    private OrderSpecifier<String> orderByName(Pageable pageable) {
        // Order by
        Sort pageableSort = pageable.getSort();
        Optional<Sort.Order> optionalOrder = pageableSort.stream().filter(o -> o.getProperty().equals("products.name")).findFirst();
        if (optionalOrder.isPresent()) {
            Sort.Order order = optionalOrder.get();
            if (order.isDescending()) {
                return Expressions.stringPath(MAX_NAME_FIELD).desc();
            } else {
                return Expressions.stringPath(MIN_NAME_FIELD).asc();
            }
        }
        return null;
    }

    private BooleanExpression whereTotalBetween(BigDecimal totalCostFrom, BigDecimal totalCostTo) {
        if (totalCostFrom != null && totalCostTo != null) {
            return Expressions.numberPath(Double.class, TOTAL_FIELD).between(totalCostFrom, totalCostTo);
        } else if (totalCostFrom != null) {
            return Expressions.numberPath(Double.class, TOTAL_FIELD).goe(totalCostFrom);
        } else if (totalCostTo != null) {
            return Expressions.numberPath(Double.class, TOTAL_FIELD).loe(totalCostTo);
        }
        return null;
    }
}