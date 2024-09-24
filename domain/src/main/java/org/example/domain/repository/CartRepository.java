package org.example.domain.repository;

import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;

import java.math.BigDecimal;

public interface CartRepository {
    CartDto saveCart(CartDto cartDto);

    CartDto updateCart(CartDto cartDto);

    CartDto addProductToCart(Long cartId, ProductItemDto productItemDto);

    CartDto addDiscountToCart(Long cartId, String discountCode);

    CartDto deleteCart(Long cartId);

    CartDto getCartDtoById(Long cartId);

    CartDto removeDiscountFromCart(Long cartId, String code);

    CartDto removeProductFromCart(Long cartId, Long productId);

    PageDto<CartDto> findAllBy(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, PageableDto pageableDto);
}
