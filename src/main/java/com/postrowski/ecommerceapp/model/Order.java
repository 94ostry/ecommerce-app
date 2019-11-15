package com.postrowski.ecommerceapp.model;

import com.postrowski.ecommerceapp.dto.OrderDto;
import com.postrowski.ecommerceapp.dto.OrderProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDER_")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;

    @Builder.Default
    private Instant created = Instant.now();

    public OrderDto toDto() {

        List<OrderProductDto> productDtos = getOrderProducts().stream().map(OrderProduct::toDto).collect(Collectors.toList());

        BigDecimal totalValue = productDtos.stream()
                .map(orderProductDto -> orderProductDto.getProductPrice().multiply(BigDecimal.valueOf(orderProductDto.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return OrderDto.builder()
                .id(getId())
                .email(getEmail())
                .orderedProducts(productDtos)
                .totalValue(totalValue)
                .build();
    }

}
