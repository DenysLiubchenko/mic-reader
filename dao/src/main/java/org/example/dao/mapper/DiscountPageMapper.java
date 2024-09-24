package org.example.dao.mapper;

import org.example.dao.entity.DiscountEntity;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {DiscountEntityMapper.class})
public interface DiscountPageMapper {
    PageDto<DiscountDto> toDto(Page<DiscountEntity> discountPage);
}
