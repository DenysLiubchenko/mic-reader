package org.example.dao.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.adapters.ProductJpaAdapter;
import org.example.dao.entity.ProductEntity;
import org.example.dao.mapper.PageableMapper;
import org.example.dao.mapper.ProductEntityMapper;
import org.example.dao.mapper.ProductPageMapper;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;
import org.example.domain.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final ProductJpaAdapter productJpaAdapter;
    private final PageableMapper pageableMapper;
    private final ProductPageMapper productPageMapper;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public ProductDto save(ProductDto productDto) {
        ProductEntity productEntity = productEntityMapper.fromDto(productDto);
        ProductEntity saved = productJpaAdapter.save(productEntity);
        productJpaAdapter.flush();
        log.info("Saved product: {}", productEntity);
        return productEntityMapper.toDto(saved);
    }

    @Override
    public PageDto<ProductDto> findAll(PageableDto pageableDto) {
        PageRequest pageRequest = pageableMapper.fromDto(pageableDto);
        Page<ProductEntity> page = productJpaAdapter.findAll(pageRequest);
        return productPageMapper.toDto(page);
    }
}
