package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.api.generated.api.CartApi;
import org.example.api.generated.model.CartPageDTO;
import org.example.api.generated.model.FindAllCartsTotalCostRangeParameterDTO;
import org.example.api.generated.model.PageableDTO;
import org.example.api.mapper.CartPageDTOMapper;
import org.example.api.mapper.PageableDTOMapper;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.service.CartService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CartController implements CartApi {
    private final CartService cartService;
    private final PageableDTOMapper pageableDTOMapper;
    private final CartPageDTOMapper cartPageDTOMapper;

    @Override
    public CartPageDTO findAllCarts(String productNameSearchQuery, FindAllCartsTotalCostRangeParameterDTO totalCostRange, PageableDTO pageable) {
        PageableDto pageableDto = pageableDTOMapper.fromDto(pageable);
        PageDto<CartDto> cartDtoPageDto = cartService.findAll(productNameSearchQuery, totalCostRange.getFrom(), totalCostRange.getTo(), pageableDto);
        return cartPageDTOMapper.toDTO(cartDtoPageDto);
    }
}
