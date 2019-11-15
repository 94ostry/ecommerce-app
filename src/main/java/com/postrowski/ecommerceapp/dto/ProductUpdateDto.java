package com.postrowski.ecommerceapp.dto;

import com.postrowski.ecommerceapp.validation.ExactlyOneNotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ExactlyOneNotNull(fields = {"name", "price"})
public class ProductUpdateDto {
    @Size(min = 1, max = 255)
    private String name;

    private BigDecimal price;
}
