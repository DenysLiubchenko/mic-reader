package org.example.domain.repository;

import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;

public interface ProductRepository {
    ProductDto save(ProductDto productDto);

    PageDto<ProductDto> findAll(PageableDto pageableDto);

    boolean existsById(Long id);
}
