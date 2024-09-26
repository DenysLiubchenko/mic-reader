package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.constant.LogReason;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;
import org.example.domain.historyRepository.CartHistoryRepository;
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
    public final CartHistoryRepository cartHistoryRepository;

    @Override
    @Transactional
    public void addDiscountToCartWithId(Long cartId, Set<String> codes) {
        cartRepository.addDiscountToCart(cartId, codes);
        CartDto cartDto = cartRepository.getCartDtoById(cartId);
        cartHistoryRepository.save(cartDto, LogReason.UPDATE);
    }

    @Override
    @Transactional
    public void addProductToCartWithId(Long cartId, Set<ProductItemDto> productItems) {
        cartRepository.addProductToCart(cartId, productItems);
        CartDto cartDto = cartRepository.getCartDtoById(cartId);
        cartHistoryRepository.save(cartDto, LogReason.UPDATE);
    }

    @Override
    @Transactional
    public void deleteById(Long cartId) {
        CartDto cartDto = cartRepository.getCartDtoById(cartId);
        cartRepository.deleteCart(cartId);
        cartHistoryRepository.save(cartDto, LogReason.DELETE);
    }

    @Override
    @Transactional
    public void saveCart(CartDto cart) {
        CartDto cartDto = cartRepository.saveCart(cart);
        cartHistoryRepository.save(cartDto, LogReason.CREATE);
    }

    @Override
    @Transactional
    public void updateCart(CartDto cart) {
        CartDto cartDto = cartRepository.updateCart(cart);
        cartHistoryRepository.save(cartDto, LogReason.UPDATE);
    }

    @Override
    @Transactional
    public void removeDiscountFromCartWithId(Long cartId, Set<String> codes) {
        cartRepository.removeDiscountFromCart(cartId, codes);
        CartDto cartDto = cartRepository.getCartDtoById(cartId);
        cartHistoryRepository.save(cartDto, LogReason.UPDATE);
    }

    @Override
    @Transactional
    public void removeProductFromCartWithId(Long cartId, Set<Long> productIds) {
        cartRepository.removeProductFromCart(cartId, productIds);
        CartDto cartDto = cartRepository.getCartDtoById(cartId);
        cartHistoryRepository.save(cartDto, LogReason.UPDATE);
    }

    @Override
    public PageDto<CartDto> findAll(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, PageableDto pageableDto) {
        return cartRepository.findAllBy(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);
    }
}
