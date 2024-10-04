package org.example.domain.repository;

import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;

import java.math.BigDecimal;
import java.util.Set;

public interface CartRepository {
    CartDto saveCart(CartDto cartDto);

    CartDto updateCart(CartDto cartDto);

    void addProductToCart(Long cartId, Set<ProductItemDto> productItemDtos);

    void addDiscountToCart(Long cartId, Set<String> discountCodes);

    void deleteCart(Long cartId);

    CartDto getCartDtoById(Long cartId);

    void removeDiscountFromCart(Long cartId, Set<String> codes);

    void removeProductFromCart(Long cartId, Set<Long> productIds);

    PageDto<CartDto> findAllBy(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, PageableDto pageableDto);

    boolean existsById(Long id);
}
