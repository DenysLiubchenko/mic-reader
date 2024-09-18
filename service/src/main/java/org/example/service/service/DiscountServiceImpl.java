package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.DiscountDto;
import org.example.domain.repository.DiscountRepository;
import org.example.domain.service.DiscountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    public final DiscountRepository discountRepository;

    @Override
    public void save(DiscountDto discount) {

    }
}
