package org.example.domain.service;

import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;

public interface ProductService {
    void save(ProductDto product);

    PageDto<ProductDto> findAll(PageableDto pageableDto);
}
