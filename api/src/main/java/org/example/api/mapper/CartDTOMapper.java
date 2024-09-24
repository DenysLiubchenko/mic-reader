package org.example.api.mapper;

import org.example.api.generated.model.CartDTO;
import org.example.domain.dto.CartDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartDTOMapper {
    CartDTO toDTO(CartDto cartDto);
}
