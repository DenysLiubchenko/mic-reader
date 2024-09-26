package org.example.domain.historyRepository;

import org.example.domain.constant.LogReason;
import org.example.domain.dto.CartDto;

public interface CartHistoryRepository {
    void save(CartDto cartDto, LogReason logReason);
}
