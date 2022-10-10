package com.jacelendro.deposit.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    public DepositPlanType getType() {
        return type;
    }

    public void setType(DepositPlanType type) {
        this.type = type;
    }

    public Map<String, DepositPlanAllocation> getAllocations() {
        return allocations;
    }

    public void setAllocations(Map<String, DepositPlanAllocation> allocations) {
        this.allocations = allocations;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
