package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.generated.api.ProductApi;
import org.example.api.generated.model.PageableDTO;
import org.example.api.generated.model.ProductPageDTO;
import org.example.api.mapper.PageableDTOMapper;
import org.example.api.mapper.ProductPageDTOMapper;
import org.example.domain.dto.PageableDto;
import org.example.domain.service.ProductService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ProductController implements ProductApi {
    private final ProductService productService;
    private final PageableDTOMapper pageableDTOMapper;
    private final ProductPageDTOMapper productPageDTOMapper;

    @Override
    public ProductPageDTO findAllProducts(PageableDTO pageable) {
        PageableDto pageableDto = pageableDTOMapper.fromDto(pageable);
        return productPageDTOMapper.toDTO(productService.findAll(pageableDto));
    }
}
