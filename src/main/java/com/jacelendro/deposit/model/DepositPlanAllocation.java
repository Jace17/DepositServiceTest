package com.jacelendro.deposit.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositPlanAllocation {
    private String portfolioName;
    private BigDecimal amount;
    private boolean completed = false;

    public DepositPlanAllocation(String portfolioName, BigDecimal amount) {
        this.portfolioName = portfolioName;
        this.amount = amount;
    }
}
