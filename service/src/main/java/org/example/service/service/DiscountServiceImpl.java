package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.repository.DiscountRepository;
import org.example.domain.service.DiscountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    public final DiscountRepository discountRepository;

    @Override
    @Transactional
    public void save(DiscountDto discount) {
        discountRepository.save(discount);
    }

    @Override
    public PageDto<DiscountDto> findAll(PageableDto pageableDto) {
        return discountRepository.findAll(pageableDto);
    }
}
