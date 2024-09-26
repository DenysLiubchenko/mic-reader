package org.example.domain.historyRepository;

import org.example.domain.constant.LogReason;
import org.example.domain.dto.ProductDto;

public interface ProductHistoryRepository {
    void save(ProductDto productDto, LogReason logReason);
}
