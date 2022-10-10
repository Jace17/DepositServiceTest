package com.jacelendro.deposit.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class DepositPlan {
    private DepositPlanType type;
    private Map<String, DepositPlanAllocation> allocations = new HashMap<>();
    private boolean completed = false;

    public DepositPlan(DepositPlanType type) {
        this.type = type;
    }

    public void addAllocation(String portfolioName, BigDecimal amount) {
        allocations.put(portfolioName, new DepositPlanAllocation(portfolioName, amount));
    }
}
