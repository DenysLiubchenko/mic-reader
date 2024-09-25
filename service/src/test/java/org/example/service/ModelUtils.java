package org.example.service;

import org.example.domain.dto.CartDto;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;
import org.example.domain.dto.ProductItemDto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
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

    public static PageableDto getPageableDto() {
        return PageableDto.builder()
                .page(0)
                .size(8)
                .sort(Collections.emptyList())
                .build();
    }

    public static <T> PageDto<T> pageDtoOf(T... elements) {
        return PageDto.<T>builder()
                .content(List.of(elements))
                .empty(false)
                .first(true)
                .last(false)
                .number(0)
                .totalElements(10L)
                .numberOfElements(elements.length)
                .build();
    }
}
