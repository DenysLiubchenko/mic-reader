package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.constant.LogReason;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;
import org.example.domain.historyRepository.ProductHistoryRepository;
import org.example.domain.repository.ProductRepository;
import org.example.domain.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    public final ProductRepository productRepository;
    private final ProductHistoryRepository productHistoryRepository;

    @Override
    @Transactional
    public void save(ProductDto product) {
        ProductDto productDto = productRepository.save(product);
        productHistoryRepository.save(productDto, LogReason.CREATE);
    }

    @Override
    public PageDto<ProductDto> findAll(PageableDto pageableDto) {
        return productRepository.findAll(pageableDto);
    }
}
