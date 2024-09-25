package org.example.domain.service;

import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;

import java.math.BigDecimal;
import java.util.Set;

public interface CartService {
    void addDiscountToCartWithId(Long cartId, Set<String> codes);

    void addProductToCartWithId(Long cartId, Set<ProductItemDto> productItems);

    void deleteById(Long cartId);

    void saveCart(CartDto cart);

    void updateCart(CartDto cart);

    void removeDiscountFromCartWithId(Long cartId, Set<String> codes);

    void removeProductFromCartWithId(Long cartId, Set<Long> productIds);

    PageDto<CartDto> findAll(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, PageableDto pageableDto);
}
