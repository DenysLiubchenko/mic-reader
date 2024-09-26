package org.example.log.mapper;

import org.example.domain.dto.ProductDto;
import org.example.log.model.ProductDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDocumentMapper {
    @Mapping(target = "productId", source = "productDto.id")
    @Mapping(target = "id", ignore = true)
    ProductDocument toProductDocument(ProductDto productDto, String reason);
}
