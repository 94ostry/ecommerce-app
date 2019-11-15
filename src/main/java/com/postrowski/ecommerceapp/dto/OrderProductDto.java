package com.postrowski.ecommerceapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {

    @NotNull
    private Long productId;

    @NotNull
    private BigDecimal productPrice;

    @NotNull
    private Long amount;

}
