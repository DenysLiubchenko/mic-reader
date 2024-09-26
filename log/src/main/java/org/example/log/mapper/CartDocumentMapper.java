package org.example.log.mapper;

import org.example.domain.dto.CartDto;
import org.example.log.model.CartDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartDocumentMapper {
    @Mapping(target = "cartId", source = "cartDto.id")
    @Mapping(target = "id", ignore = true)
    CartDocument toCartDocument(CartDto cartDto, String reason);
}
