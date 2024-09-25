package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;
import org.example.domain.repository.CartRepository;
import org.example.domain.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    public final CartRepository cartRepository;

    @Override
    @Transactional
    public void addDiscountToCartWithId(Long cartId, Set<String> codes) {
        cartRepository.addDiscountToCart(cartId, codes);
    }

    @Override
    @Transactional
    public void addProductToCartWithId(Long cartId, Set<ProductItemDto> productItems) {
        cartRepository.addProductToCart(cartId, productItems);
    }

    @Override
    @Transactional
    public void deleteById(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    @Override
    @Transactional
    public void saveCart(CartDto cart) {
        cartRepository.saveCart(cart);
    }

    @Override
    @Transactional
    public void updateCart(CartDto cart) {
        cartRepository.updateCart(cart);
    }

    @Override
    @Transactional
    public void removeDiscountFromCartWithId(Long cartId, Set<String> codes) {
        cartRepository.removeDiscountFromCart(cartId, codes);
    }

    @Override
    @Transactional
    public void removeProductFromCartWithId(Long cartId, Set<Long> productIds) {
        cartRepository.removeProductFromCart(cartId, productIds);
    }

    @Override
    public PageDto<CartDto> findAll(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, PageableDto pageableDto) {
        return cartRepository.findAllBy(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);
    }
}
