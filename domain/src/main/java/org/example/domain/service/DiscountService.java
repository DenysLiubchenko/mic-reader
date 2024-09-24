package org.example.domain.service;

import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;

public interface DiscountService {
    void save(DiscountDto discount);

    PageDto<DiscountDto> findAll(PageableDto pageableDto);
}
