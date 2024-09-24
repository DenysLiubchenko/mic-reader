package org.example.api.mapper;

import org.example.api.generated.model.ProductPageDTO;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.ProductDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ProductDTOMapper.class})
public interface ProductPageDTOMapper {
    ProductPageDTO toDTO(PageDto<ProductDto> pageDto);
}
