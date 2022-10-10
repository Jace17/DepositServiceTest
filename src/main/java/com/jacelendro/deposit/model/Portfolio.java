package com.jacelendro.deposit.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Portfolio {
    private String name;
    private BigDecimal amount;
}
