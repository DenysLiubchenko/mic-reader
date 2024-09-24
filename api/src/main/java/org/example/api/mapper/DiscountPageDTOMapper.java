package org.example.api.mapper;

import org.example.api.generated.model.DiscountPageDTO;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DiscountDTOMapper.class})
public interface DiscountPageDTOMapper {
    DiscountPageDTO toDTO(PageDto<DiscountDto> pageDto);
}
