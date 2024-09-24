package org.example.api.mapper;

import org.example.api.generated.model.DiscountDTO;
import org.example.domain.dto.DiscountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscountDTOMapper {
    DiscountDTO toDTO(DiscountDto discountDto);
}
