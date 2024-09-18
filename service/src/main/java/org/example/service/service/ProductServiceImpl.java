package org.example.service.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.ProductDto;
import org.example.domain.repository.ProductRepository;
import org.example.domain.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    public final ProductRepository productRepository;

    @Override
    public void save(ProductDto product) {

    }
}
