package org.example.dao.mapper;

import org.example.dao.entity.ProductEntity;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.ProductDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {ProductEntityMapper.class})
public interface ProductPageMapper {
    PageDto<ProductDto> toDto(Page<ProductEntity> productPage);
}
