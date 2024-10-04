package org.example.service.service;

import org.example.domain.constant.LogReason;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.exception.ConflictException;
import org.example.domain.historyRepository.DiscountHistoryRepository;
import org.example.domain.repository.DiscountRepository;
import org.example.service.ModelUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class DiscountServiceTest {
    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private DiscountHistoryRepository discountHistoryRepository;

    @InjectMocks
    private DiscountServiceImpl discountService;

    private final DiscountDto discountDto = ModelUtils.getDiscountDto();
    private final PageableDto pageableDto = ModelUtils.getPageableDto();

    @Test
    void saveDiscountTest() {
        // Given
        given(discountRepository.existsByCode(discountDto.getCode())).willReturn(false);
        given(discountRepository.save(discountDto)).willReturn(discountDto);

        // When
        discountService.save(discountDto);

        // Then
        then(discountRepository).should().existsByCode(discountDto.getCode());
        then(discountRepository).should().save(discountDto);
        then(discountHistoryRepository).should().save(discountDto, LogReason.CREATE);
    }


    @Test
    void saveDiscount_WhenEventDuplicated_ThrowsConflictExceptionTest() {
        // Given
        given(discountRepository.existsByCode(discountDto.getCode())).willReturn(true);
        // When
        assertThatThrownBy(()->discountService.save(discountDto)).isInstanceOf(ConflictException.class);

        // Then
        then(discountRepository).should().existsByCode(discountDto.getCode());
    }

    @Test
    void findAllDiscountsTest() {
        // Given
        PageDto<DiscountDto> expectedPageDto = ModelUtils.pageDtoOf(discountDto);
        given(discountRepository.findAll(pageableDto)).willReturn(expectedPageDto);

        // When
        PageDto<DiscountDto> result = discountService.findAll(pageableDto);

        // Then
        then(discountRepository).should().findAll(pageableDto);
        assertThat(result).isEqualTo(expectedPageDto);
    }
}
