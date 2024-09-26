package org.example.log;

import org.example.domain.dto.CartDto;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.ProductDto;
import org.example.domain.dto.ProductItemDto;
import org.example.log.model.CartDocument;
import org.example.log.model.DiscountDocument;
import org.example.log.model.ProductDocument;
import org.example.log.model.ProductItem;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

public class ModelUtils {
    public static DiscountDto getDiscountDto() {
        return DiscountDto.builder().code("CODE_2000")
                .due(OffsetDateTime.of(3000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC)).build();
    }

    public static ProductDto getProductDto() {
        return ProductDto.builder().name("productName").cost(BigDecimal.TEN).build();
    }

    public static ProductItemDto getProductItemDto() {
        return ProductItemDto.builder().productId(1L).quantity(1).build();
    }

    public static ProductItemDto getProductItemDto(Long productId, Integer quantity) {
        return ProductItemDto.builder().productId(productId).quantity(quantity).build();
    }

    public static CartDto getCartDto() {
        return CartDto.builder()
                .id(1L)
                .discounts(Set.of("CODE_2000","CODE_2001","CODE_2002"))
                .products(Set.of(getProductItemDto(1L,1), getProductItemDto(2L,2), getProductItemDto(3L,3)))
                .build();
    }

    public static CartDocument getCartDocument() {
        return CartDocument.builder()
                .cartId(1L)
                .products(Set.of(getProductItem()))
                .discounts(Set.of("CODE_2000","CODE_2001","CODE_2002"))
                .build();
    }

    public static ProductItem getProductItem() {
        return ProductItem.builder()
                .cartId(1L)
                .productId(1L)
                .quantity(10)
                .build();
    }

    public static DiscountDocument getDiscountDocument() {
        return DiscountDocument.builder()
                .code("CODE_2000")
                .due(OffsetDateTime.of(3000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC).toInstant())
                .build();
    }

    public static ProductDocument getProductDocument() {
        return ProductDocument.builder()
                .productId(1L)
                .name("productName")
                .cost(BigDecimal.TEN)
                .build();
    }
}
