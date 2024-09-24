package org.example.domain.repository;

import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;

public interface DiscountRepository {
    DiscountDto save(DiscountDto discountDto);

    PageDto<DiscountDto> findAll(PageableDto pageableDto);
}
