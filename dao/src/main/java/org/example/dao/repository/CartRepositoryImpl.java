package org.example.dao.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.adapters.CartJpaAdapter;
import org.example.dao.adapters.CartSearchJpaAdapter;
import org.example.dao.entity.CartEntity;
import org.example.dao.entity.ProductItemEntity;
import org.example.dao.mapper.CartEntityMapper;
import org.example.dao.mapper.CartPageMapper;
import org.example.dao.mapper.PageableMapper;
import org.example.dao.mapper.ProductItemEntityMapper;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;
import org.example.domain.exception.NotFoundException;
import org.example.domain.repository.CartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {
    private final CartJpaAdapter cartJpaAdapter;
    private final CartSearchJpaAdapter cartSearchJpaAdapter;
    private final PageableMapper pageableMapper;
    private final CartEntityMapper cartEntityMapper;
    private final CartPageMapper cartPageMapper;
    private final ProductItemEntityMapper productItemEntityMapper;

    @Override
    public void addProductToCart(Long cartId, Set<ProductItemDto> productItemDtos) {
        CartEntity cart = cartJpaAdapter.findByIdFetchProducts(cartId)
                .orElseThrow(() -> new NotFoundException("Cart with id: %s is not found".formatted(cartId)));
        for (ProductItemDto productItemDto : productItemDtos) {
            Optional<ProductItemEntity> optionalProductItem = cart.getProducts().stream()
                    .filter(product -> product.getId().getProductId().equals(productItemDto.getProductId()))
                    .findFirst();

            if (optionalProductItem.isPresent()) {
                // Update product quantity
                optionalProductItem.get().setQuantity(productItemDto.getQuantity());
                log.info("Updated product item quantity {}", productItemDto);
            } else {
                // Add new product
                ProductItemEntity productItem = productItemEntityMapper.fromDto(productItemDto);
                cart.addProduct(productItem);
                log.info("Added product item {}", productItem);
            }
        }
        cartJpaAdapter.flush();
    }

    @Override
    public void removeProductFromCart(Long cartId, Set<Long> productIds) {
        CartEntity cart = cartJpaAdapter.findByIdFetchProducts(cartId)
                .orElseThrow(() -> new NotFoundException("Cart with id: %s is not found".formatted(cartId)));
        cart.getProducts().removeIf(productId -> productIds.contains(productId.getId().getProductId()));
        cartJpaAdapter.flush();
    }

    @Override
    public PageDto<CartDto> findAllBy(String productNameSearchQuery, BigDecimal totalCostFrom, BigDecimal totalCostTo, PageableDto pageableDto) {
        PageRequest pageRequest = pageableMapper.fromDto(pageableDto);
        Page<CartEntity> entityPage = cartSearchJpaAdapter.search(productNameSearchQuery, totalCostFrom, totalCostTo, pageRequest);
        return cartPageMapper.toDto(entityPage);
    }

    @Override
    public void addDiscountToCart(Long cartId, Set<String> discountCode) {
        CartEntity cart = cartJpaAdapter.findByIdFetchDiscounts(cartId)
                .orElseThrow(() -> new NotFoundException("Cart with id: %s is not found".formatted(cartId)));

        cart.getDiscounts().addAll(discountCode);

        cartJpaAdapter.flush();
        log.info("Added discount code {} to the cart {}", discountCode, cartId);
    }

    @Override
    public void removeDiscountFromCart(Long cartId, Set<String> code) {
        CartEntity cart = cartJpaAdapter.findByIdFetchDiscounts(cartId)
                .orElseThrow(() -> new NotFoundException("Cart with id: %s is not found".formatted(cartId)));
        cart.getDiscounts().removeAll(code);

        cartJpaAdapter.flush();
        log.info("Removed discount code {} from the cart {}", code, cartId);
    }

    @Override
    public CartDto saveCart(CartDto cartDto) {
        CartEntity cartEntity = cartEntityMapper.fromDto(cartDto);
        cartEntity.getProducts().forEach(product -> product.setCart(cartEntity));

        CartEntity savedCartEntity = cartJpaAdapter.save(cartEntity);
        cartJpaAdapter.flush();
        log.info("Saved cart entity: {}", savedCartEntity);
        return cartEntityMapper.toDto(savedCartEntity);
    }

    @Override
    public CartDto updateCart(CartDto cartDto) {
        CartEntity cartEntity = cartEntityMapper.fromDto(cartDto);
        CartEntity existingCart = getCartById(cartEntity.getId());

        existingCart.getDiscounts().removeIf(discount -> !cartEntity.getDiscounts().contains(discount));
        cartEntity.getDiscounts().removeIf(discount -> existingCart.getDiscounts().contains(discount));
        existingCart.getDiscounts().addAll(cartEntity.getDiscounts());

        updateProducts(existingCart, cartEntity.getProducts());

        cartJpaAdapter.flush();
        log.info("Updated cart entity: {}", cartEntity);
        return cartEntityMapper.toDto(existingCart);
    }

    @Override
    public void deleteCart(Long cartId) {
        cartJpaAdapter.deleteById(cartId);

        cartJpaAdapter.flush();
        log.info("Deleted cart entity by id: {}", cartId);
    }

    @Override
    @Transactional(readOnly = true)
    public CartDto getCartDtoById(Long cartId) {
        return cartEntityMapper.toDto(getCartById(cartId));
    }

    private CartEntity getCartById(Long cartId) {
        CartEntity cartEntity = cartJpaAdapter.findByIdFetchDiscounts(cartId)
                .orElseThrow(() -> new NotFoundException("Cart with id: %s is not found".formatted(cartId)));
        cartJpaAdapter.findByIdFetchProducts(cartId).get();
        return cartEntity;
    }

    private void updateProducts(CartEntity existingCart, Set<ProductItemEntity> newProducts) {
        Set<ProductItemEntity> existingProducts = existingCart.getProducts();

        Map<Long, ProductItemEntity> newProductMap = newProducts.stream()
                .collect(Collectors.toMap(p -> p.getId().getProductId(), p -> p));

        Iterator<ProductItemEntity> iterator = existingProducts.iterator();
        while (iterator.hasNext()) {
            ProductItemEntity existingProduct = iterator.next();
            Long productId = existingProduct.getId().getProductId();

            if (newProductMap.containsKey(productId)) {
                ProductItemEntity newProduct = newProductMap.get(productId);
                existingProduct.setQuantity(newProduct.getQuantity());
                newProductMap.remove(productId);
            } else {
                iterator.remove();
            }
        }

        newProductMap.values().forEach(newProduct -> {
            newProduct.setCart(existingCart);
            existingCart.getProducts().add(newProduct);
        });
    }
}
