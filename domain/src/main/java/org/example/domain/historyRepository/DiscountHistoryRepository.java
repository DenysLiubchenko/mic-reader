package org.example.domain.historyRepository;

import org.example.domain.constant.LogReason;
import org.example.domain.dto.DiscountDto;

public interface DiscountHistoryRepository {
    void save(DiscountDto discountDto, LogReason logReason);
}
