package com.jacelendro.deposit;

import com.jacelendro.deposit.model.Customer;
import com.jacelendro.deposit.model.DepositPlan;
import com.jacelendro.deposit.model.DepositPlanType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Logger log
            = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DepositPlan oneTime = new DepositPlan(DepositPlanType.ONE_TIME);
        oneTime.addAllocation("High risk", new BigDecimal(10000));
        oneTime.addAllocation("Retirement", new BigDecimal(500));

        DepositPlan monthly = new DepositPlan(DepositPlanType.MONTHLY);
        monthly.addAllocation("High risk", new BigDecimal(0));
        monthly.addAllocation("Retirement", new BigDecimal(100));

        List<DepositPlan> depositPlans = new ArrayList<>();
        depositPlans.add(oneTime);
        depositPlans.add(monthly);

        List<BigDecimal> deposits = new ArrayList<>();
        deposits.add(new BigDecimal(10500));
        deposits.add(new BigDecimal((100)));

        Customer customer = DepositService.processDeposits(depositPlans, deposits);
        log.info("Portfolio amounts: " + customer);
    }
}