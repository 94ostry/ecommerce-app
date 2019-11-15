package com.postrowski.ecommerceapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @NotNull
    private BigDecimal price;
}
