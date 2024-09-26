package org.example.log.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.constant.LogReason;
import org.example.domain.dto.DiscountDto;
import org.example.domain.historyRepository.DiscountHistoryRepository;
import org.example.log.adapters.DiscountMongoAdapter;
import org.example.log.mapper.DiscountDocumentMapper;
import org.example.log.model.DiscountDocument;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountHistoryRepositoryImpl implements DiscountHistoryRepository {
    private final DiscountMongoAdapter discountMongoAdapter;
    private final DiscountDocumentMapper discountDocumentMapper;

    @Override
    public void save(DiscountDto discountDto, LogReason logReason) {
        DiscountDocument discountDocument = discountDocumentMapper.toDiscountDocument(discountDto, logReason.name());
        discountMongoAdapter.save(discountDocument);
        log.info("Save discount snapshot: {}", discountDocument);
    }
}
