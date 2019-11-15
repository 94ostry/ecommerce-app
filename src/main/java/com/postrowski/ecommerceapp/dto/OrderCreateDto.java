package com.postrowski.ecommerceapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateDto {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 1)
    private List<OrderProductCreateDto> products;
}
