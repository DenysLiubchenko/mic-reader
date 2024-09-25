package org.example.consumer.mapper;

import org.example.fact.DiscountFactEvent;
import org.example.domain.dto.DiscountDto;
import org.mapstruct.Mapper;

import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface DiscountFactEventMapper {
    DiscountFactEvent toEvent(DiscountDto discountDto, String reason);
    DiscountDto toDto(DiscountFactEvent discountFactEvent);

    default String map(OffsetDateTime value) {
        return value.toString();
    }
    default OffsetDateTime map(String due) {
        return OffsetDateTime.parse(due);
    }
}
