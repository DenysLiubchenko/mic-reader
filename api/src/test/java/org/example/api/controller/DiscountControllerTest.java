package org.example.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.ModelUtils;
import org.example.api.generated.model.DiscountPageDTO;
import org.example.api.generated.model.PageableDTO;
import org.example.api.handler.CustomExceptionHandler;
import org.example.api.mapper.DiscountPageDTOMapper;
import org.example.api.mapper.PageableDTOMapper;
import org.example.domain.dto.DiscountDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.service.DiscountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiscountController.class)
@ContextConfiguration(classes = {DiscountController.class})
@Import(value = {AopAutoConfiguration.class, CustomExceptionHandler.class})
class DiscountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DiscountService discountService;

    @MockBean
    private PageableDTOMapper pageableDTOMapper;

    @MockBean
    private DiscountPageDTOMapper discountPageDTOMapper;

    @Test
    void findAllDiscountsTest() throws Exception {
        // Given
        PageableDTO pageableDTO = ModelUtils.getPageableDTO();
        PageableDto pageableDto = ModelUtils.getPageableDto();
        PageDto<DiscountDto> pageDto = ModelUtils.pageDtoOf(ModelUtils.getDiscountDto());
        DiscountPageDTO expectedPageDTO = ModelUtils.getDiscountPageDTO();

        given(pageableDTOMapper.fromDto(pageableDTO)).willReturn(pageableDto);
        given(discountService.findAll(pageableDto)).willReturn(pageDto);
        given(discountPageDTOMapper.toDTO(pageDto)).willReturn(expectedPageDTO);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/discount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        // Then
        verify(pageableDTOMapper).fromDto(pageableDTO);
        verify(discountService).findAll(pageableDto);
        verify(discountPageDTOMapper).toDTO(pageDto);
        assertThat(result).isEqualTo(objectMapper.writeValueAsString(expectedPageDTO));
    }
}

