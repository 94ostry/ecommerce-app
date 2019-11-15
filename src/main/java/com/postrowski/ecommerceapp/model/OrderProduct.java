package com.postrowski.ecommerceapp.model;

import com.postrowski.ecommerceapp.dto.OrderProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    private BigDecimal productPrice;

    private Long amount;

    public OrderProductDto toDto() {
        return OrderProductDto.builder()
                .amount(getAmount())
                .productPrice(getProductPrice())
                .productId(getProduct().getId())
                .build();
    }
}
