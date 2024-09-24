package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.generated.api.DiscountApi;
import org.example.api.generated.model.DiscountPageDTO;
import org.example.api.generated.model.PageableDTO;
import org.example.api.mapper.DiscountPageDTOMapper;
import org.example.api.mapper.PageableDTOMapper;
import org.example.domain.dto.PageableDto;
import org.example.domain.service.DiscountService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class DiscountController implements DiscountApi {
    private final DiscountService discountService;
    private final PageableDTOMapper pageableDTOMapper;
    private final DiscountPageDTOMapper discountPageDTOMapper;

    @Override
    public DiscountPageDTO findAllDiscounts(PageableDTO pageable) {
        PageableDto pageableDto = pageableDTOMapper.fromDto(pageable);
        return discountPageDTOMapper.toDTO(discountService.findAll(pageableDto));
    }
}
