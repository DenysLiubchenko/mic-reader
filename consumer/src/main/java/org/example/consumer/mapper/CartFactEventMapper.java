package org.example.consumer.mapper;

import org.example.domain.dto.CartDto;
import org.example.fact.CartFactEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartFactEventMapper {
    CartFactEvent toEvent(CartDto cartDto, String reason);
    CartDto toDto(CartFactEvent cartFactEvent);
}
