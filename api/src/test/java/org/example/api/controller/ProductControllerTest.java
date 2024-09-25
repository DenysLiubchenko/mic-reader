package org.example.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.ModelUtils;
import org.example.api.generated.model.PageableDTO;
import org.example.api.generated.model.ProductPageDTO;
import org.example.api.handler.CustomExceptionHandler;
import org.example.api.mapper.PageableDTOMapper;
import org.example.api.mapper.ProductPageDTOMapper;
import org.example.domain.dto.PageDto;
import org.example.domain.dto.PageableDto;
import org.example.domain.dto.ProductDto;
import org.example.domain.service.ProductService;
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

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = {ProductController.class})
@Import(value = {AopAutoConfiguration.class, CustomExceptionHandler.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private PageableDTOMapper pageableDTOMapper;

    @MockBean
    private ProductPageDTOMapper productPageDTOMapper;

    @Test
    void findAllProductsTest() throws Exception {
        // Given
        PageableDTO pageableDTO = ModelUtils.getPageableDTO();
        PageableDto pageableDto = ModelUtils.getPageableDto();
        PageDto<ProductDto> pageDto = ModelUtils.pageDtoOf(ModelUtils.getProductDto());
        ProductPageDTO expectedPageDTO = ModelUtils.getProductPageDTO();

        given(pageableDTOMapper.fromDto(pageableDTO)).willReturn(pageableDto);
        given(productService.findAll(pageableDto)).willReturn(pageDto);
        given(productPageDTOMapper.toDTO(pageDto)).willReturn(expectedPageDTO);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pageableDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String result = mvcResult.getResponse().getContentAsString();

        // Then
        verify(pageableDTOMapper).fromDto(pageableDTO);
        verify(productService).findAll(pageableDto);
        verify(productPageDTOMapper).toDTO(pageDto);
        assertThat(result).isEqualTo(objectMapper.writeValueAsString(expectedPageDTO));
    }
}

