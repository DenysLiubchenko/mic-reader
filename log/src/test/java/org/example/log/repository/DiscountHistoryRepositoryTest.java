package org.example.log.repository;

import org.example.domain.constant.LogReason;
import org.example.domain.dto.DiscountDto;
import org.example.log.ModelUtils;
import org.example.log.adapters.DiscountMongoAdapter;
import org.example.log.mapper.DiscountDocumentMapper;
import org.example.log.model.DiscountDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class DiscountHistoryRepositoryTest {

    @Mock
    private DiscountMongoAdapter discountMongoAdapter;

    @Mock
    private DiscountDocumentMapper discountDocumentMapper;

    @InjectMocks
    private DiscountHistoryRepositoryImpl discountHistoryRepository;

    private final DiscountDto discountDto = ModelUtils.getDiscountDto();
    private final DiscountDocument discountDocument = ModelUtils.getDiscountDocument();

    @Test
    void saveDiscountHistoryTest() {
        // Given
        LogReason logReason = LogReason.CREATE;
        given(discountDocumentMapper.toDiscountDocument(discountDto, logReason.name())).willReturn(discountDocument);

        // When
        discountHistoryRepository.save(discountDto, logReason);

        // Then
        then(discountDocumentMapper).should().toDiscountDocument(discountDto, logReason.name());
        then(discountMongoAdapter).should().save(discountDocument);
    }
}
