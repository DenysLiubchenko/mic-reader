package org.example.boot.it;

import org.example.dao.entity.CartEntity;
import org.example.dao.entity.DiscountEntity;
import org.example.dao.entity.ProductEntity;
import org.example.dao.entity.ProductItemEntity;
import org.example.dao.entity.ProductItemId;
import org.example.delta.DeleteCartDeltaEvent;
import org.example.delta.DiscountCartDeltaEvent;
import org.example.delta.ModifyProductItemCartDeltaEvent;
import org.example.delta.RemoveProductItemCartDeltaEvent;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.ProductDto;
import org.example.domain.dto.ProductItemDto;
import org.example.fact.CartFactEvent;
import org.example.fact.DiscountFactEvent;
import org.example.fact.ProductFactEvent;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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

    public static ProductFactEvent getProductFactEvent(String reason) {
        return ProductFactEvent.newBuilder()
                .setId(1)
                .setReason(reason)
                .setName("productName")
                .setCost("22.22")
                .build();
    }

    public static DiscountFactEvent getDiscountFactEvent(String reason) {
        return DiscountFactEvent.newBuilder()
                .setReason(reason)
                .setCode("CODE_2000")
                .setDue("2030-12-03T10:15:30+01:00")
                .build();
    }

    public static CartFactEvent getCartFactEvent(String reason) {
        return CartFactEvent.newBuilder()
                .setId(1)
                .setReason(reason)
                .setDiscounts(List.of("CODE_2000"))
                .setProducts(List.of(getFactProductItem()))
                .build();
    }

    public static CartFactEvent getCartFactEvent(Long id, String reason) {
        return CartFactEvent.newBuilder()
                .setId(id)
                .setReason(reason)
                .setDiscounts(List.of("CODE_2000"))
                .setProducts(List.of(getFactProductItem()))
                .build();
    }

    public static org.example.fact.ProductItem getFactProductItem() {
        return org.example.fact.ProductItem.newBuilder()
                .setProductId(1L)
                .setQuantity(1)
                .build();
    }

    public static org.example.delta.ProductItem getDeltaProductItem() {
        return org.example.delta.ProductItem.newBuilder()
                .setProductId(1L)
                .setQuantity(1)
                .build();
    }

    public static DeleteCartDeltaEvent getDeleteCartDeltaEvent() {
        return DeleteCartDeltaEvent.newBuilder()
                .setId(53)
                .build();
    }

    public static ModifyProductItemCartDeltaEvent getModifyProductItemCartDeltaEvent(String reason) {
        return ModifyProductItemCartDeltaEvent.newBuilder()
                .setId(54)
                .setProducts(List.of(getDeltaProductItem()))
                .setReason(reason)
                .build();
    }

    public static RemoveProductItemCartDeltaEvent getRemoveProductItemCartDeltaEvent() {
        return RemoveProductItemCartDeltaEvent.newBuilder()
                .setId(56)
                .setProductIds(List.of(1L))
                .build();
    }

    public static DiscountCartDeltaEvent getDiscountCartDeltaEvent(String reason) {
        return DiscountCartDeltaEvent.newBuilder()
                .setId(55)
                .setReason(reason)
                .setDiscounts(List.of("CODE_2000"))
                .build();
    }

    public static ProductItemEntity getProductItemEntity() {
        return ProductItemEntity.builder()
                .id(new ProductItemId(1L,1L))
                .quantity(12)
                .cart(new CartEntity(1L, null, null))
                .build();
    }

    public static DiscountEntity getDiscountEntity() {
        return DiscountEntity.builder()
                .code("CODE_2000")
                .due(OffsetDateTime.of(3000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC)).build();
    }

    public static ProductEntity getProductEntity() {
        return ProductEntity.builder()
                .id(1L)
                .cost(BigDecimal.TEN)
                .name("productName")
                .build();
    }
}
