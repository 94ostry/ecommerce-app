package com.postrowski.ecommerceapp.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MoneySerializerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void serializeMoneyValue() throws JsonProcessingException {

        //given
        BigDecimal moneyValue = BigDecimal.valueOf(200.0);

        //when
        String moneyText = objectMapper.writeValueAsString(moneyValue);

        //then
        assertThat(moneyText).isNotNull();
        assertThat(moneyText).isEqualTo("200.00");
    }

}
