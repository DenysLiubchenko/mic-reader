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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartServiceImpl implements CartService {
    public final CartRepository cartRepository;

    @Override
    @Transactional
    public void addDiscountToCartWithId(Long cartId, String code) {

    }

    @Override
    @Transactional
    public void addProductToCartWithId(Long cartId, ProductItemDto productItem) {

    }

    @Override
    @Transactional
    public void deleteById(Long cartId) {

    }

    @Override
    @Transactional
    public void saveCart(CartDto cart) {

    }

    @Override
    @Transactional
    public void updateCart(CartDto cart) {

    }

    @Override
    @Transactional
    public void removeDiscountFromCartWithId(Long cartId, String code) {

    }

    @Override
    @Transactional
    public void removeProductFromCartWithId(Long cartId, Long productId) {

    }

    @Override
    public PageDto<CartDto> findAll(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, PageableDto pageableDto) {
        return cartRepository.findAllBy(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);
    }
}
