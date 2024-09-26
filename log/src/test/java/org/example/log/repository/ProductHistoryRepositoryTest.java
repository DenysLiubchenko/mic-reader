package org.example.log.repository;

import org.example.domain.constant.LogReason;
import org.example.domain.dto.ProductDto;
import org.example.log.ModelUtils;
import org.example.log.adapters.ProductMongoAdapter;
import org.example.log.mapper.ProductDocumentMapper;
import org.example.log.model.ProductDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProductHistoryRepositoryTest {

    @Mock
    private ProductMongoAdapter productMongoAdapter;

    @Mock
    private ProductDocumentMapper productDocumentMapper;

    @InjectMocks
    private ProductHistoryRepositoryImpl productHistoryRepository;

    private final ProductDto productDto = ModelUtils.getProductDto();
    private final ProductDocument productDocument = ModelUtils.getProductDocument();

    @Test
    void saveProductHistoryTest() {
        // Given
        LogReason logReason = LogReason.CREATE;
        given(productDocumentMapper.toProductDocument(productDto, logReason.name())).willReturn(productDocument);

        // When
        productHistoryRepository.save(productDto, logReason);

        // Then
        then(productDocumentMapper).should().toProductDocument(productDto, logReason.name());
        then(productMongoAdapter).should().save(productDocument);
    }
}
