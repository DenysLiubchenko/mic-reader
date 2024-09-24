package org.example.api.mapper;

import org.example.api.generated.model.CartPageDTO;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartDTOMapper.class})
public interface CartPageDTOMapper {
    CartPageDTO toDTO(PageDto<CartDto> pageDto);
}
