package org.example.dao.repository;

import org.example.dao.ModelUtils;
import org.example.dao.adapters.ProductJpaAdapter;
import org.example.dao.entity.ProductEntity;
import org.example.dao.mapper.PageableMapper;
import org.example.dao.mapper.ProductEntityMapper;
import org.example.dao.mapper.ProductPageMapper;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryTest {

    @Mock
    private ProductJpaAdapter productJpaAdapter;

    @Mock
    private ProductEntityMapper productEntityMapper;
    @Mock
    private ProductPageMapper productPageMapper;
    @Mock
    private PageableMapper pageableMapper;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private final ProductDto productDto = ModelUtils.getProductDto();
    private final ProductEntity productEntity = ModelUtils.getProductEntity();
    
    @Test
    void saveProductTest() {
        // Given
        given(productEntityMapper.fromDto(productDto)).willReturn(productEntity);
        given(productJpaAdapter.save(productEntity)).willReturn(productEntity);
        given(productEntityMapper.toDto(productEntity)).willReturn(productDto);

        // When
        ProductDto result = productRepository.save(productDto);

        // Then
        then(productEntityMapper).should().fromDto(productDto);
        then(productJpaAdapter).should().save(productEntity);
        then(productJpaAdapter).should().flush();
        then(productEntityMapper).should().toDto(productEntity);
        assertThat(result).isEqualTo(productDto);
    }

    @Test
    void saveProduct_whenSaveFails_throwsRuntimeExceptionTest() {
        // Given
        given(productEntityMapper.fromDto(productDto)).willReturn(productEntity);
        given(productJpaAdapter.save(productEntity)).willThrow(new RuntimeException("Save failed"));

        // When
        assertThatThrownBy(() -> productRepository.save(productDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Save failed");

        // Then
        then(productEntityMapper).should().fromDto(productDto);
        then(productJpaAdapter).should().save(productEntity);
    }

    @Test
    void findAllProductsTest() {
        // Given
        PageableDto pageableDto = ModelUtils.getPageableDto();
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductEntity> productEntities = new PageImpl<>(Collections.singletonList(productEntity));
        PageDto<ProductDto> productDtoPageDto = ModelUtils.pageDtoOf(productDto);

        given(pageableMapper.fromDto(pageableDto)).willReturn(pageRequest);
        given(productJpaAdapter.findAll(pageRequest)).willReturn(productEntities);
        given(productPageMapper.toDto(productEntities)).willReturn(productDtoPageDto);

        // When
        PageDto<ProductDto> result = productRepository.findAll(pageableDto);

        // Then
        then(pageableMapper).should().fromDto(pageableDto);
        then(productJpaAdapter).should().findAll(pageRequest);
        then(productPageMapper).should().toDto(productEntities);
        assertThat(result).isEqualTo(productDtoPageDto);
    }
}

