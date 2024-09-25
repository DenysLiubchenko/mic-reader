package org.example.consumer.mapper;

import org.example.delta.ProductItem;
import org.example.delta.DeleteCartDeltaEvent;
import org.example.delta.DiscountCartDeltaEvent;
import org.example.delta.ModifyProductItemCartDeltaEvent;
import org.example.delta.RemoveProductItemCartDeltaEvent;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.ProductItemDto;
import org.example.fact.CartFactEvent;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CartDeltaEventMapper {
    CartFactEvent toEvent (CartDto cartDto, String reason);
    CartDto toDto (CartFactEvent cartFactEvent);
    DeleteCartDeltaEvent toEvent (Long id);
    ModifyProductItemCartDeltaEvent toEvent (Long id, List<ProductItemDto> products, String reason);
    Set<ProductItemDto> toProductItemDto (List<ProductItem> cartDto);
    RemoveProductItemCartDeltaEvent toEvent (Long id, List<Long> productIds);
    DiscountCartDeltaEvent toDiscountEvent (Long id, List<String> discounts, String reason);
}
