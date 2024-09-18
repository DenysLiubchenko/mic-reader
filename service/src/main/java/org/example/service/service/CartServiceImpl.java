package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.ProductItemDto;
import org.example.domain.repository.CartRepository;
import org.example.domain.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    public final CartRepository cartRepository;

    @Override
    public void addDiscountToCartWithId(Long cartId, String code) {

    }

    @Override
    public void addProductToCartWithId(Long cartId, ProductItemDto productItem) {

    }

    @Override
    public void deleteById(Long cartId) {

    }

    @Override
    public void saveCart(CartDto cart) {

    }

    @Override
    public void updateCart(CartDto cart) {

    }

    @Override
    public void removeDiscountFromCartWithId(Long cartId, String code) {

    }

    @Override
    public void removeProductFromCartWithId(Long cartId, Long productId) {

    }
}
