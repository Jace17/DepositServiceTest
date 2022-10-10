package com.jacelendro.deposit.model;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String referenceCode;
    private Map<String, Portfolio> portfolios = new HashMap<>();
    private Map<DepositPlanType, DepositPlan> depositPlans = new HashMap<>();

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public Map<String, Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(Map<String, Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    public Map<DepositPlanType, DepositPlan> getDepositPlans() {
        return depositPlans;
    }

    public void setDepositPlans(Map<DepositPlanType, DepositPlan> depositPlans) {
        this.depositPlans = depositPlans;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (Portfolio p : portfolios.values()) {
            sb.append(p.getName());
            sb.append(": $");
            sb.append(p.getAmount().toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
