package org.example.service.service;

import org.example.domain.constant.LogReason;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;
import org.example.domain.historyRepository.ProductHistoryRepository;
import org.example.domain.repository.ProductRepository;
import org.example.service.ModelUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductHistoryRepository productHistoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private final ProductDto productDto = ModelUtils.getProductDto();
    private final PageableDto pageableDto = ModelUtils.getPageableDto();

    @Test
    void saveProductTest() {
        //Given
        given(productRepository.save(productDto)).willReturn(productDto);

        // When
        productService.save(productDto);

        // Then
        then(productRepository).should().save(productDto);
        then(productHistoryRepository).should().save(productDto, LogReason.CREATE);
    }

    @Test
    void findAllProductsTest() {
        // Given
        PageDto<ProductDto> expectedPageDto = ModelUtils.pageDtoOf(productDto);
        given(productRepository.findAll(pageableDto)).willReturn(expectedPageDto);

        // When
        PageDto<ProductDto> result = productService.findAll(pageableDto);

        // Then
        then(productRepository).should().findAll(pageableDto);
        assertThat(result).isEqualTo(expectedPageDto);
    }
}
