package com.postrowski.ecommerceapp.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.postrowski.ecommerceapp.config.MoneySerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrderDto {

    @NotNull
    private Long id;

    @NotNull
    private String email;

    //@JsonSerialize(using = MoneySerializer.class)
    private BigDecimal totalValue;

    private List<OrderProductDto> orderedProducts;
}
