package org.example.service.service;


import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;
import org.example.domain.repository.CartRepository;
import org.example.service.ModelUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private final CartDto cartDto = ModelUtils.getCartDto();
    private final Long cartId = 1L;
    private final Set<String> discountCodes = Set.of("DISCOUNT10");
    private final Set<ProductItemDto> productItems = Set.of(ModelUtils.getProductItemDto());
    private final PageableDto pageableDto = ModelUtils.getPageableDto();

    @Test
    void addDiscountToCartWithIdTest() {
        // When
        cartService.addDiscountToCartWithId(cartId, discountCodes);

        // Then
        then(cartRepository).should().addDiscountToCart(cartId, discountCodes);
    }

    @Test
    void addProductToCartWithIdTest() {
        // When
        cartService.addProductToCartWithId(cartId, productItems);

        // Then
        then(cartRepository).should().addProductToCart(cartId, productItems);
    }

    @Test
    void deleteByIdTest() {
        // When
        cartService.deleteById(cartId);

        // Then
        then(cartRepository).should().deleteCart(cartId);
    }

    @Test
    void saveCartTest() {
        // When
        cartService.saveCart(cartDto);

        // Then
        then(cartRepository).should().saveCart(cartDto);
    }

    @Test
    void updateCartTest() {
        // When
        cartService.updateCart(cartDto);

        // Then
        then(cartRepository).should().updateCart(cartDto);
    }

    @Test
    void removeDiscountFromCartWithIdTest() {
        // When
        cartService.removeDiscountFromCartWithId(cartId, discountCodes);

        // Then
        then(cartRepository).should().removeDiscountFromCart(cartId, discountCodes);
    }

    @Test
    void removeProductFromCartWithIdTest() {
        Set<Long> productIds = Set.of(1L, 2L); // Example product IDs

        // When
        cartService.removeProductFromCartWithId(cartId, productIds);

        // Then
        then(cartRepository).should().removeProductFromCart(cartId, productIds);
    }

    @Test
    void findAllCartsTest() {
        // Given
        PageDto<CartDto> expectedPageDto = ModelUtils.pageDtoOf(cartDto);
        String productNameSearchQuery = "example";
        BigDecimal totalCostFrom = BigDecimal.ZERO;
        BigDecimal totalCostTo = BigDecimal.TEN;
        given(cartRepository.findAllBy(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto)).willReturn(expectedPageDto);

        // When
        PageDto<CartDto> result = cartService.findAll(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);

        // Then
        then(cartRepository).should().findAllBy(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);
        assertThat(result).isEqualTo(expectedPageDto);
    }
}

