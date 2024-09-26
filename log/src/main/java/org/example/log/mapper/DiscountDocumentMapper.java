package org.example.log.mapper;

import org.example.domain.dto.DiscountDto;
import org.example.log.model.DiscountDocument;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;

@Mapper(componentModel = "spring")
public interface DiscountDocumentMapper {
    DiscountDocument toDiscountDocument(DiscountDto discountDto, String reason);

    default Instant map(OffsetDateTime value) {
        return value.toInstant();
    }
}
