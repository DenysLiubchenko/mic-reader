package org.example.log.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.constant.LogReason;
import org.example.domain.dto.ProductDto;
import org.example.domain.historyRepository.ProductHistoryRepository;
import org.example.log.adapters.ProductMongoAdapter;
import org.example.log.mapper.ProductDocumentMapper;
import org.example.log.model.ProductDocument;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductHistoryRepositoryImpl implements ProductHistoryRepository {
    private final ProductMongoAdapter productMongoAdapter;
    private final ProductDocumentMapper productDocumentMapper;

    @Override
    public void save(ProductDto productDto, LogReason logReason) {
        ProductDocument productDocument = productDocumentMapper.toProductDocument(productDto, logReason.name());
        productMongoAdapter.save(productDocument);
        log.info("Save product snapshot: {}", productDocument);
    }
}
