package org.example.log.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.constant.LogReason;
import org.example.domain.dto.CartDto;
import org.example.domain.historyRepository.CartHistoryRepository;
import org.example.log.adapters.CartMongoAdapter;
import org.example.log.mapper.CartDocumentMapper;
import org.example.log.model.CartDocument;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartHistoryRepositoryImpl implements CartHistoryRepository {
    private final CartMongoAdapter cartMongoAdapter;
    private final CartDocumentMapper cartDocumentMapper;

    @Override
    public void save(CartDto cartDto, LogReason logReason) {
        CartDocument cartDocument = cartDocumentMapper.toCartDocument(cartDto, logReason.name());
        cartMongoAdapter.save(cartDocument);
        log.info("Save cart snapshot: {}", cartDocument);
    }
}
