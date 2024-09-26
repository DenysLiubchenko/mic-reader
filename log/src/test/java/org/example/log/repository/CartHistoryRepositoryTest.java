package org.example.log.repository;

import org.example.domain.constant.LogReason;
import org.example.log.ModelUtils;
import org.example.domain.dto.CartDto;
import org.example.log.adapters.CartMongoAdapter;
import org.example.log.mapper.CartDocumentMapper;
import org.example.log.model.CartDocument;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class CartHistoryRepositoryTest {

    @Mock
    private CartMongoAdapter cartMongoAdapter;

    @Mock
    private CartDocumentMapper cartDocumentMapper;

    @InjectMocks
    private CartHistoryRepositoryImpl cartHistoryRepository;

    private final CartDto cartDto = ModelUtils.getCartDto();
    private final CartDocument cartDocument = ModelUtils.getCartDocument();

    @Test
    void saveCartHistoryTest() {
        // Given
        LogReason logReason = LogReason.CREATE;
        given(cartDocumentMapper.toCartDocument(cartDto, logReason.name())).willReturn(cartDocument);

        // When
        cartHistoryRepository.save(cartDto, logReason);

        // Then
        then(cartDocumentMapper).should().toCartDocument(cartDto, logReason.name());
        then(cartMongoAdapter).should().save(cartDocument);
    }
}
