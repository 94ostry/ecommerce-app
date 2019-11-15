package com.postrowski.ecommerceapp.controller;

import com.postrowski.ecommerceapp.config.SwaggerConstants;
import com.postrowski.ecommerceapp.controller.exception.ProductNotFoundException;
import com.postrowski.ecommerceapp.dto.OrderCreateDto;
import com.postrowski.ecommerceapp.dto.OrderDto;
import com.postrowski.ecommerceapp.model.Order;
import com.postrowski.ecommerceapp.model.OrderProduct;
import com.postrowski.ecommerceapp.model.Product;
import com.postrowski.ecommerceapp.repository.OrderRepository;
import com.postrowski.ecommerceapp.repository.ProductRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Api(tags = SwaggerConstants.SERVICE_NAME)
public class OrderController {

    class UriConstants {

        public static final String FROM_DATE = "from";
        public static final String TO_DATE = "to";

        static final String BASE_PATH = "/orders";
    }

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @ApiOperation("Find a Orders")
    @GetMapping(path = UriConstants.BASE_PATH, params = {UriConstants.FROM_DATE, UriConstants.TO_DATE})
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional
    public List<OrderDto> getOrders(@RequestParam(value = UriConstants.FROM_DATE, required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDateTime,
                                    @RequestParam(value = UriConstants.TO_DATE, required = false)
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDateTime) {

        if(fromDateTime == null) {
            fromDateTime = Instant.MIN;
        }

        if(toDateTime == null) {
            toDateTime = Instant.MAX;
        }

        return orderRepository.findAllByCreatedBetween(fromDateTime, toDateTime)
                .stream()
                .map(Order::toDto)
                .collect(Collectors.toList());

    }

    @ApiOperation("Create a Order")
    @PostMapping(UriConstants.BASE_PATH)
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional
    public OrderDto createOrder(@Valid @RequestBody OrderCreateDto createDto) {

        Order order = Order.builder()
                .email(createDto.getEmail())
                .build();

        List<OrderProduct> orderedProducts = createDto.getProducts().stream().map(orderProductCreateDto -> {

            Long productId = orderProductCreateDto.getProductId();

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException(productId));

            OrderProduct orderProduct = OrderProduct.builder()
                    .product(product)
                    .order(order)
                    .amount(orderProductCreateDto.getAmount())
                    .productPrice(product.getPrice())
                    .build();

            return orderProduct;
        }).collect(Collectors.toList());

        order.setOrderProducts(orderedProducts);

        return orderRepository.save(order).toDto();
    }

}
