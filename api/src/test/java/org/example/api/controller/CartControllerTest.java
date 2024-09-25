package org.example.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.ModelUtils;
import org.example.api.generated.model.CartPageDTO;
import org.example.api.generated.model.PageableDTO;
import org.example.api.handler.CustomExceptionHandler;
import org.example.api.mapper.CartPageDTOMapper;
import org.example.api.mapper.PageableDTOMapper;
import org.example.domain.dto.CartDto;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.service.CartService;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@ContextConfiguration(classes = {CartController.class})
@Import(value = {AopAutoConfiguration.class, CustomExceptionHandler.class})
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CartService cartService;

    @MockBean
    private PageableDTOMapper pageableDTOMapper;

    @MockBean
    private CartPageDTOMapper cartPageDTOMapper;

    @Test
    void findAllCartsTest() throws Exception {
        // Given
        PageableDTO pageableDTO = ModelUtils.getPageableDTO();
        PageableDto pageableDto = ModelUtils.getPageableDto();
        PageDto<CartDto> pageDto = ModelUtils.pageDtoOf(ModelUtils.getCartDto());
        CartPageDTO expectedPageDTO = ModelUtils.getCartPageDTO();
        String productNameSearchQuery = "example";
        BigDecimal totalCostFrom = BigDecimal.ZERO;
        BigDecimal totalCostTo = BigDecimal.TEN;

        given(pageableDTOMapper.fromDto(pageableDTO)).willReturn(pageableDto);
        given(cartService.findAll(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto)).willReturn(pageDto);
        given(cartPageDTOMapper.toDTO(pageDto)).willReturn(expectedPageDTO);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/cart")
                        .param("productNameSearchQuery", productNameSearchQuery)
                        .param("from", String.valueOf(totalCostFrom))
                        .param("to", String.valueOf(totalCostTo))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        // Then
        verify(pageableDTOMapper).fromDto(pageableDTO);
        verify(cartService).findAll(productNameSearchQuery, totalCostFrom, totalCostTo, pageableDto);
        verify(cartPageDTOMapper).toDTO(pageDto);
        assertThat(result).isEqualTo(objectMapper.writeValueAsString(expectedPageDTO));
    }
}
