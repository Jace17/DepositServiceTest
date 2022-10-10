package com.jacelendro.deposit.model;

import java.math.BigDecimal;

public class DepositPlanAllocation {
    private String portfolioName;
    private BigDecimal amount;
    private boolean completed = false;

    public DepositPlanAllocation(String portfolioName, BigDecimal amount) {
        this.portfolioName = portfolioName;
        this.amount = amount;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
