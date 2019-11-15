package com.postrowski.ecommerceapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.postrowski.ecommerceapp.dto.OrderCreateDto;
import com.postrowski.ecommerceapp.dto.OrderProductCreateDto;
import com.postrowski.ecommerceapp.model.Order;
import com.postrowski.ecommerceapp.model.OrderProduct;
import com.postrowski.ecommerceapp.model.Product;
import com.postrowski.ecommerceapp.repository.OrderRepository;
import com.postrowski.ecommerceapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    void whenGetOrders_thenReturnOk() throws Exception {

        //given
        Instant from = Instant.now().minus(10, ChronoUnit.DAYS);
        Instant to = Instant.now();

        String fromText = DateTimeFormatter.ISO_INSTANT.format(from);
        String toText = DateTimeFormatter.ISO_INSTANT.format(to);

        Mockito.when(orderRepository.findAllByCreatedBetween(Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList(order()));

        //when
        mvc.perform(get(OrderController.UriConstants.BASE_PATH)
                .param(OrderController.UriConstants.FROM_DATE, fromText)
                .param(OrderController.UriConstants.TO_DATE, toText)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id").value("1"))
                .andExpect(jsonPath("$.[0].email").value("ostry@test1.com"))
                .andExpect(jsonPath("$.[0].totalValue").value(200.00))
                .andExpect(jsonPath("$.[0].orderedProducts").isArray())
                .andExpect(jsonPath("$.[0].orderedProducts", hasSize(2)));
    }

    @Test
    void whenGetOrdersOutside_thenReturnOk() throws Exception {

        //given
        Instant from = Instant.now().minus(10, ChronoUnit.DAYS);
        Instant to = Instant.now();

        String fromText = DateTimeFormatter.ISO_INSTANT.format(from);
        String toText = DateTimeFormatter.ISO_INSTANT.format(to);

        Mockito.when(orderRepository.findAllByCreatedBetween(Mockito.any(), Mockito.any()))
                .thenReturn(Arrays.asList());

        //when
        mvc.perform(get(OrderController.UriConstants.BASE_PATH)
                .param(OrderController.UriConstants.FROM_DATE, fromText)
                .param(OrderController.UriConstants.TO_DATE, toText)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void whenCreateOrder_thenReturnOk() throws Exception {

        //given
        Long product1Id = 1L;
        Long product2Id = 2L;

        OrderCreateDto createDto = OrderCreateDto.builder()
                .email("ostry@test2.com")
                .products(Arrays.asList(
                        OrderProductCreateDto.builder().productId(product1Id).amount(10L).build(),
                        OrderProductCreateDto.builder().productId(product2Id).amount(5L).build()
                ))
                .build();

        Mockito.when(productRepository.findById(product1Id)).thenReturn(Optional.of(product1()));
        Mockito.when(productRepository.findById(product2Id)).thenReturn(Optional.of(product2()));
        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order());

        //when
        mvc.perform(post(OrderController.UriConstants.BASE_PATH)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void whenCreateOrderWithoutProducts_thenReturnBadRequest() throws Exception {

        //given
        OrderCreateDto createDto = OrderCreateDto.builder()
                .email("ostry@test2.com")
                .build();

        //when
        mvc.perform(post(OrderController.UriConstants.BASE_PATH)
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private Order order() {
        Product product1 = product1();
        Product product2 = product2();

        OrderProduct orderProduct1 = OrderProduct.builder()
                .id(1L)
                .product(product1)
                .productPrice(product1.getPrice())
                .amount(10L)
                .build();

        OrderProduct orderProduct2 = OrderProduct.builder()
                .id(2L)
                .product(product2)
                .productPrice(product2.getPrice())
                .amount(5L)
                .build();

        return Order.builder()
                .id(1L)
                .email("ostry@test1.com")
                .created(Instant.now().minus(5, ChronoUnit.DAYS))
                .orderProducts(Arrays.asList(orderProduct1, orderProduct2))
                .build();
    }

    private Product product1() {
        return product(1L, "Product1", 10.00);
    }

    private Product product2() {
        return product(2L, "Product2", 20.00);
    }


    private Product product(Long id, String name, double price) {
        return Product.builder().id(id).name(name).price(BigDecimal.valueOf(price)).build();
    }
}
