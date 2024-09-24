package org.example.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDto<T> {
    private Long totalElements;

    private Integer totalPages;

    private Boolean first;

    private Boolean last;

    private Integer number;

    private Integer numberOfElements;

    private Integer size;

    private Boolean empty;

    private List<T> content;
}
