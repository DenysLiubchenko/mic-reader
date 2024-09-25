package org.example.dao.repository;

import org.example.dao.ModelUtils;
import org.example.dao.adapters.CartJpaAdapter;
import org.example.dao.adapters.CartSearchJpaAdapter;
import org.example.dao.entity.CartEntity;
import org.example.dao.entity.ProductItemEntity;
import org.example.dao.entity.ProductItemId;
import org.example.dao.mapper.CartEntityMapper;
import org.example.dao.mapper.CartPageMapper;
import org.example.dao.mapper.PageableMapper;
import org.example.dao.mapper.ProductItemEntityMapper;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductItemDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CartRepositoryTest {

    @Mock
    private CartJpaAdapter cartJpaAdapter;

    @Mock
    private CartEntityMapper cartEntityMapper;

    @Mock
    private ProductItemEntityMapper productItemEntityMapper;

    @Mock
    private CartSearchJpaAdapter cartSearchJpaAdapter;

    @Mock
    private PageableMapper pageableMapper;

    @Mock
    private CartPageMapper cartPageMapper;

    @InjectMocks
    private CartRepositoryImpl cartRepository;

    private final CartEntity cartEntity = ModelUtils.getCartEntity();
    private final CartDto cartDto = ModelUtils.getCartDto();
    private final ProductItemDto productItemDto = ModelUtils.getProductItemDto();

    @Test
    void addProductToCartTest() {
        // Given
        Long cartId = cartDto.getId();
        given(cartJpaAdapter.findByIdFetchProducts(cartId)).willReturn(Optional.of(cartEntity));

        // When
        cartRepository.addProductToCart(cartId, Set.of(productItemDto));

        // Then
        then(cartJpaAdapter).should().findByIdFetchProducts(cartId);
        then(cartJpaAdapter).should().flush();
    }

    @Test
    void addProductToCart_withNewProductTest() {
        // Given
        ProductItemDto productItemDto = ModelUtils.getProductItemDto();
        productItemDto.setProductId(3L);
        ProductItemEntity productItemEntity = ModelUtils.getProductItemEntity();
        productItemEntity.getId().setProductId(3L);
        Long cartId = cartDto.getId();
        given(productItemEntityMapper.fromDto(productItemDto)).willReturn(productItemEntity);
        given(cartJpaAdapter.findByIdFetchProducts(cartId)).willReturn(Optional.of(cartEntity));

        // When
        cartRepository.addProductToCart(cartId, Set.of(productItemDto));

        // Then
        then(cartJpaAdapter).should().findByIdFetchProducts(cartId);
        then(cartJpaAdapter).should().flush();
    }

    @Test
    void removeProductFromCartTest() {
        // Given
        Long cartId = cartDto.getId();
        Long productId = 1L;
        given(cartJpaAdapter.findByIdFetchProducts(cartId)).willReturn(Optional.of(cartEntity));
        // When
        cartRepository.removeProductFromCart(cartId, Set.of(productId));

        // Then
        then(cartJpaAdapter).should().findByIdFetchProducts(cartId);
        then(cartJpaAdapter).should().flush();
    }

    @Test
    void addDiscountToCartTest() {
        // Given
        Long cartId = cartDto.getId();
        String discountCode = "DISCOUNT1";
        given(cartJpaAdapter.findByIdFetchDiscounts(cartId)).willReturn(Optional.of(cartEntity));

        // When
        cartRepository.addDiscountToCart(cartId, Set.of(discountCode));

        // Then
        then(cartJpaAdapter).should().findByIdFetchDiscounts(cartId);
        then(cartJpaAdapter).should().flush();
    }

    @Test
    void removeDiscountFromCartTest() {
        // Given
        Long cartId = cartDto.getId();
        String discountCode = "DISCOUNT1";
        given(cartJpaAdapter.findByIdFetchDiscounts(cartId)).willReturn(Optional.of(cartEntity));

        // When
        cartRepository.removeDiscountFromCart(cartId, Set.of(discountCode));

        // Then
        then(cartJpaAdapter).should().findByIdFetchDiscounts(cartId);
        then(cartJpaAdapter).should().flush();
    }

    @Test
    void saveCartTest() {
        // Given
        given(cartEntityMapper.fromDto(cartDto)).willReturn(cartEntity);
        given(cartJpaAdapter.save(cartEntity)).willReturn(cartEntity);
        given(cartEntityMapper.toDto(cartEntity)).willReturn(cartDto);

        // When
        CartDto result = cartRepository.saveCart(cartDto);

        // Then
        then(cartEntityMapper).should().fromDto(cartDto);
        then(cartJpaAdapter).should().save(cartEntity);
        then(cartJpaAdapter).should().flush();
        then(cartEntityMapper).should().toDto(cartEntity);
        assertThat(result).isEqualTo(cartDto);
    }

    @Test
    void updateCartTest() {
        // Given
        CartEntity newCartEntity = ModelUtils.getCartEntity();
        ProductItemEntity productItem = ProductItemEntity.builder()
                .id(new ProductItemId(1L, 2L))
                .cart(cartEntity)
                .quantity(1)
                .build();
        newCartEntity.addProduct(productItem);
        Long cartId = cartEntity.getId();

        given(cartEntityMapper.fromDto(cartDto)).willReturn(newCartEntity);
        given(cartJpaAdapter.findByIdFetchDiscounts(cartId)).willReturn(Optional.of(cartEntity));
        given(cartJpaAdapter.findByIdFetchProducts(cartId)).willReturn(Optional.of(cartEntity));
        given(cartEntityMapper.toDto(newCartEntity)).willReturn(cartDto);

        CartDto result = cartRepository.updateCart(cartDto);

        then(cartEntityMapper).should().fromDto(cartDto);
        then(cartJpaAdapter).should().findByIdFetchDiscounts(cartId);
        then(cartJpaAdapter).should().findByIdFetchProducts(cartId);
        then(cartEntityMapper).should().toDto(newCartEntity);
        assertThat(result).isEqualTo(cartDto);
    }

    @Test
    void deleteCartTest() {
        // Given
        Long cartId = cartDto.getId();

        // When
        cartRepository.deleteCart(cartId);

        // Then
        then(cartJpaAdapter).should().deleteById(cartId);
        then(cartJpaAdapter).should().flush();
    }

    @Test
    void getCartDtoByIdTest() {
        // Given
        Long cartId = cartDto.getId();
        given(cartJpaAdapter.findByIdFetchDiscounts(cartId)).willReturn(Optional.of(cartEntity));
        given(cartJpaAdapter.findByIdFetchProducts(cartId)).willReturn(Optional.of(cartEntity));
        given(cartEntityMapper.toDto(cartEntity)).willReturn(cartDto);

        // When
        CartDto result = cartRepository.getCartDtoById(cartId);

        // Then
        then(cartJpaAdapter).should().findByIdFetchDiscounts(cartId);
        then(cartJpaAdapter).should().findByIdFetchProducts(cartId);
        then(cartEntityMapper).should().toDto(cartEntity);
        assertThat(result).isEqualTo(cartDto);
    }

    @Test
    void findAll() {
        String productNameSearchQuery = "query";
        BigDecimal totalCostFrom = BigDecimal.ONE;
        BigDecimal totalCostTo = BigDecimal.valueOf(1111);
        PageableDto pageableDto = ModelUtils.getPageableDto();
        PageRequest pageRequest = PageRequest.of(1, 10);
        PageImpl<CartEntity> entityPage = new PageImpl<>(Collections.singletonList(cartEntity));
        PageDto<CartDto> cartDtoPageDto = ModelUtils.pageDtoOf(cartDto);

        given(pageableMapper.fromDto(pageableDto)).willReturn(pageRequest);
        given(cartSearchJpaAdapter.search(productNameSearchQuery, totalCostFrom, totalCostTo, pageRequest)).willReturn(entityPage);
        given(cartPageMapper.toDto(entityPage)).willReturn(cartDtoPageDto);

        PageDto<CartDto> result = cartRepository.findAllBy(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);

        then(pageableMapper).should().fromDto(pageableDto);
        then(cartSearchJpaAdapter).should().search(productNameSearchQuery, totalCostFrom, totalCostTo, pageRequest);
        then(cartPageMapper).should().toDto(entityPage);
        assertThat(result).isEqualTo(cartDtoPageDto);
    }
}

