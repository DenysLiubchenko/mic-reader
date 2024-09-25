package org.example.consumer.mapper;

import org.example.fact.ProductFactEvent;
import org.example.domain.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductFactEventMapper {
    ProductFactEvent toEvent(ProductDto productDto, String reason);
    ProductDto toDto(ProductFactEvent productFactEvent);
}
