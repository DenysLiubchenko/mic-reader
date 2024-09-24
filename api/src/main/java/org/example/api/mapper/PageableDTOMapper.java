package org.example.api.mapper;

import org.example.api.generated.model.PageableDTO;
import org.example.domain.dto.PageableDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PageableDTOMapper {
    PageableDto fromDto(PageableDTO pageableDTO);
}
