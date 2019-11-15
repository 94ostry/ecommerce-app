package com.postrowski.ecommerceapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postrowski.ecommerceapp.dto.ProductCreateDto;
import com.postrowski.ecommerceapp.dto.ProductUpdateDto;
import com.postrowski.ecommerceapp.model.Product;
import com.postrowski.ecommerceapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRepository productRepository;

    @Test
    void whenGetProducts_thenReturnOk() throws Exception {

        //given
        Mockito.when(productRepository.findAll()).thenReturn(products());

        //when
        mvc.perform(get(ProductController.UriConstants.BASE_PATH)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].name").value("Product1"))
                .andExpect(jsonPath("$.[0].price").value(10.0))
                .andReturn();

        //then
        Mockito.verify(productRepository, Mockito.times(1)).findAll();
    }

    @Test
    void whenCreateProduct_thenReturnCreated() throws Exception {

        //given
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Product1")
                .price(BigDecimal.valueOf(100))
                .build();

        //when
        mvc.perform(post(ProductController.UriConstants.BASE_PATH)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isCreated());

        //then
        Mockito.verify(productRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }

    @Test
    void whenCreateProductNullValue_thenReturnBadRequest() throws Exception {

        //given
        ProductCreateDto createDto = ProductCreateDto.builder()
                .build();

        //when
        mvc.perform(post(ProductController.UriConstants.BASE_PATH)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateProduct_thenReturnOk() throws Exception {

        //given
        Long productId = 1L;
        Mockito.when(productRepository.findById(productId)).thenReturn(
                Optional.of(product(productId, "Product1", 200.00)));
        Mockito.when(productRepository.save(Mockito.any()))
                .then(AdditionalAnswers.returnsFirstArg());

        ProductUpdateDto updateDto = ProductUpdateDto.builder()
                .price(BigDecimal.valueOf(400.00))
                .build();

        //when
        mvc.perform(patch(ProductController.UriConstants.BASE_PATH_WITH_ID, productId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Product1"))
                .andExpect(jsonPath("$.price").value("400.0"));

        //then
        Mockito.verify(productRepository, Mockito.atLeastOnce()).save(Mockito.any());
    }

    @Test
    void whenUpdateNotExistingProduct_thenReturnNotFound() throws Exception {
        //given
        Mockito.when(productRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());

        Long productId = 100L;
        ProductUpdateDto updateDto = ProductUpdateDto.builder()
                .price(BigDecimal.valueOf(400.00))
                .build();

        //when
        mvc.perform(patch(ProductController.UriConstants.BASE_PATH_WITH_ID, productId)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private List<Product> products() {
        return Arrays.asList(
                product(1L, "Product1", 10.00),
                product(2L, "Product2", 20.00),
                product(3L, "Product3", 30.00)
        );
    }

    private Product product(Long id, String name, double price) {
        return Product.builder().id(id).name(name).price(BigDecimal.valueOf(price)).build();
    }

}
