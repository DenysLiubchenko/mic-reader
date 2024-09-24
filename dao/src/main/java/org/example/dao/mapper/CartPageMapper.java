package org.example.dao.mapper;

import org.example.dao.entity.CartEntity;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.CartDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = {CartEntityMapper.class})
public interface CartPageMapper {
    PageDto<CartDto> toDto(Page<CartEntity> productPage);
}
