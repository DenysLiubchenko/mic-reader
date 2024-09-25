package org.example.api;

import org.example.api.generated.model.CartDTO;
import org.example.api.generated.model.CartPageDTO;
import org.example.api.generated.model.DiscountDTO;
import org.example.api.generated.model.DiscountPageDTO;
import org.example.api.generated.model.PageableDTO;
import org.example.api.generated.model.ProductDTO;
import org.example.api.generated.model.ProductItemDTO;
import org.example.api.generated.model.ProductPageDTO;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;
import org.example.domain.dto.ProductItemDto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ModelUtils {

    public static DiscountDto getDiscountDto() {
        return DiscountDto.builder().code("CODE_2000")
                .due(OffsetDateTime.of(3000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC)).build();
    }

    public static ProductDto getProductDto() {
        return ProductDto.builder().id(1L).name("productName").cost(BigDecimal.TEN).build();
    }

    public static ProductItemDto getProductItemDto() {
        return ProductItemDto.builder().productId(1L).quantity(1).build();
    }

    public static CartDto getCartDto() {
        return CartDto.builder().id(1L).discounts(Set.of("CODE_2000")).products(Set.of(getProductItemDto())).build();
    }

    public static PageableDto getPageableDto() {
        return PageableDto.builder()
                .page(0)
                .size(8)
                .sort(Collections.emptyList())
                .build();
    }

    public static <T> PageDto<T> pageDtoOf(T... elements) {
        return PageDto.<T>builder()
                .content(List.of(elements))
                .empty(false)
                .first(true)
                .last(false)
                .number(0)
                .totalElements(10L)
                .numberOfElements(elements.length)
                .build();
    }

    public static PageableDTO getPageableDTO() {
        PageableDTO pageableDTO = new PageableDTO();
        pageableDTO.setPage(0);
        pageableDTO.setSize(8);
        pageableDTO.setSort(Collections.emptyList());
        return pageableDTO;
    }

    public static ProductPageDTO getProductPageDTO() {
        ProductPageDTO productPageDTO = new ProductPageDTO();
        List<ProductDTO> productDTO = List.of(getProductDTO());
        productPageDTO.content(productDTO);
        productPageDTO.empty(false);
        productPageDTO.first(true);
        productPageDTO.last(false);
        productPageDTO.number(0);
        productPageDTO.totalElements(10L);
        productPageDTO.numberOfElements(productDTO.size());
        return productPageDTO;
    }

    public static ProductDTO getProductDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.id(1L);
        productDTO.name("productName");
        productDTO.cost(BigDecimal.TEN);
        return productDTO;
    }

    public static DiscountPageDTO getDiscountPageDTO() {
        DiscountPageDTO discountPageDTO = new DiscountPageDTO();
        List<DiscountDTO> discountDTO = List.of(getDiscountDTO());
        discountPageDTO.content(discountDTO);
        discountPageDTO.empty(false);
        discountPageDTO.first(true);
        discountPageDTO.last(false);
        discountPageDTO.number(0);
        discountPageDTO.totalElements(10L);
        discountPageDTO.numberOfElements(discountDTO.size());
        return discountPageDTO;
    }

    public static DiscountDTO getDiscountDTO() {
        DiscountDTO discountDTO = new DiscountDTO();
        discountDTO.code("CODE_2000");
        discountDTO.due(OffsetDateTime.of(3000, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC));
        return discountDTO;
    }

    public static CartPageDTO getCartPageDTO() {
        CartPageDTO cartPageDTO = new CartPageDTO();
        List<CartDTO> cartDTOS = List.of(getCartDTO());
        cartPageDTO.content(cartDTOS);
        cartPageDTO.empty(false);
        cartPageDTO.first(true);
        cartPageDTO.last(false);
        cartPageDTO.number(0);
        cartPageDTO.totalElements(10L);
        cartPageDTO.numberOfElements(cartDTOS.size());
        return cartPageDTO;
    }

    public static CartDTO getCartDTO() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.id(1L);
        cartDTO.setProducts(Set.of(getProductItemDTO()));
        cartDTO.setDiscounts(Set.of("CODE_2000"));
        return cartDTO;
    }

    private static ProductItemDTO getProductItemDTO() {
        ProductItemDTO productItemDTO = new ProductItemDTO();
        productItemDTO.setProductId(1L);
        productItemDTO.setQuantity(100);
        return productItemDTO;
    }
}
