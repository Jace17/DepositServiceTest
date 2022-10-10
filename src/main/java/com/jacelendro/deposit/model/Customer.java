package com.jacelendro.deposit.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Customer {
    private String referenceCode;
    private Map<String, Portfolio> portfolios = new HashMap<>();
    private Map<DepositPlanType, DepositPlan> depositPlans = new HashMap<>();
}
