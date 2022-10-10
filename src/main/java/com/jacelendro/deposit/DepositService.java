package com.jacelendro.deposit;

import com.jacelendro.deposit.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


public class DepositService {
    private static final Logger log
            = LoggerFactory.getLogger(DepositService.class);

    public static Customer processDeposits(List<DepositPlan> depositPlans, List<BigDecimal> deposits) {
        validateProcessDepositsInput(depositPlans, deposits);

        final Customer customer = new Customer();

        // Assign one-time plan
        final DepositPlan oneTimePlan = depositPlans.stream()
                .filter(dp -> DepositPlanType.ONE_TIME.equals(dp.getType()))
                .findAny()
                .orElse(null);

        if (oneTimePlan != null) {
            customer.getDepositPlans().put(DepositPlanType.ONE_TIME, oneTimePlan);
        }

        // Assign monthly plan
        final DepositPlan monthlyPlan = depositPlans.stream()
                .filter(dp -> DepositPlanType.MONTHLY.equals(dp.getType()))
                .findAny()
                .orElse(null);

        if (monthlyPlan != null) {
            customer.getDepositPlans().put(DepositPlanType.MONTHLY, monthlyPlan);
        }

        depositPlans.forEach(dp -> {
            // Create the portfolios in the customer object
            dp.getAllocations().keySet().forEach(k -> {
                if (!customer.getPortfolios().containsKey(k)) {
                    customer.getPortfolios().put(k, new Portfolio(k, new BigDecimal(0)));
                }
            });
        });

        if(deposits!=null) {
            deposits.forEach(deposit -> {
                log.info("Processing deposit: $" + deposit);

                processDeposit(customer, deposit);

                log.info("Portfolio amounts: \n" + customer);
            });
        }

        return customer;
    }

    private static void processDeposit(Customer customer, BigDecimal deposit) {
        final DepositPlan oneTimePlan = customer.getDepositPlans().get(DepositPlanType.ONE_TIME);
        final DepositPlan monthlyPlan = customer.getDepositPlans().get(DepositPlanType.MONTHLY);

        // Attempt to fulfill the one-time deposit plan
        if (oneTimePlan != null && !oneTimePlan.isCompleted()) {
            // Get list of one-time allocations that have not yet been fulfilled
            final List<String> portfolioNames = oneTimePlan.getAllocations().values().stream()
                    .filter(d -> !d.isCompleted() || d.getAmount().equals(BigDecimal.ZERO))
                    .map(DepositPlanAllocation::getPortfolioName)
                    .toList();

            // Used to calculate the percent to allocate
            final BigDecimal allocationTotal = oneTimePlan.getAllocations().values().stream()
                    .filter(d -> !d.isCompleted())
                    .map(DepositPlanAllocation::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            final BigDecimal remainingDeposit = BigDecimal.ZERO;

            portfolioNames.forEach(n -> {
                final DepositPlanAllocation allocation = oneTimePlan.getAllocations().get(n);
                final Portfolio portfolio = customer.getPortfolios().get(n);

                // Max allocation for a one-time plan
                final BigDecimal maxAllocation = allocation.getAmount();
                // Percentage of the deposit to be allocated
                final BigDecimal amountToAllocate = deposit.multiply(maxAllocation.divide(allocationTotal, 20, RoundingMode.HALF_UP)).setScale(deposit.scale(), RoundingMode.HALF_UP);
                // Amount currently in the portfolio
                final BigDecimal portfolioAmount = portfolio.getAmount();
                if (portfolioAmount.add(amountToAllocate).compareTo(maxAllocation) >= 0) {
                    // If amount to allocate would exceed the plan, carry over
                    portfolio.setAmount(maxAllocation);
                    allocation.setCompleted(true);
                    remainingDeposit.add(amountToAllocate.add(portfolioAmount).subtract(maxAllocation));
                } else {
                    portfolio.setAmount(amountToAllocate.add(portfolioAmount));
                }
            });

            // Check if all allocations have been completed, then mark the deposit plan as completed
            boolean completed = true;
            for (DepositPlanAllocation a : oneTimePlan.getAllocations().values()) {
                if (!a.isCompleted() && !a.getAmount().equals(BigDecimal.ZERO)) {
                    completed = false;
                    break;
                }
            }
            oneTimePlan.setCompleted(completed);

            // Check if there are any remaining deposits to be processed
            if (remainingDeposit.compareTo(BigDecimal.ZERO) > 0) {
                processDeposit(customer, remainingDeposit);
            }
        } else {
            // Assign deposit by ratios if monthly plan is specified otherwise use ratios for one-time plan
            DepositPlan activePlan;
            if (monthlyPlan != null) {
                activePlan = monthlyPlan;
            } else {
                activePlan = oneTimePlan;
            }

            final List<String> portfolioNames = activePlan.getAllocations().values().stream()
                    .filter(a -> a.getAmount().compareTo(BigDecimal.ZERO) > 0)
                    .map(DepositPlanAllocation::getPortfolioName)
                    .toList();

            final BigDecimal allocationTotal = activePlan.getAllocations().values().stream()
                    .map(DepositPlanAllocation::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            portfolioNames.forEach(n -> {
                final DepositPlanAllocation allocation = activePlan.getAllocations().get(n);
                final Portfolio portfolio = customer.getPortfolios().get(n);

                // Max allocation for a one-time plan
                final BigDecimal maxAllocation = allocation.getAmount();
                // Percentage of the deposit to be allocated
                final BigDecimal amountToAllocate = deposit.multiply(maxAllocation.divide(allocationTotal, 20, RoundingMode.HALF_UP)).setScale(deposit.scale(), RoundingMode.HALF_UP);
                // Amount currently in the portfolio
                final BigDecimal portfolioAmount = portfolio.getAmount();
                portfolio.setAmount(amountToAllocate.add(portfolioAmount));
            });
        }
    }

    private static void validateProcessDepositsInput(List<DepositPlan> depositPlans, List<BigDecimal> deposits) {
        // Validate all mandatory input parameter conditions
        if (depositPlans == null || depositPlans.isEmpty()) {
            throw new IllegalArgumentException("Deposit plans cannot be empty.");
        }

        if (depositPlans.size() > 2) {
            throw new IllegalArgumentException("Cannot exceed 2 deposit plans.");
        }

        if (depositPlans.size() == 2 && depositPlans.get(0).getType() == depositPlans.get(1).getType()) {
            throw new IllegalArgumentException("Deposit plans cannot be BOTH one-time and monthly types.");
        }

        depositPlans.forEach(dp -> {
            if (dp.getAllocations() == null || dp.getAllocations().isEmpty()) {
                throw new IllegalArgumentException("Deposit plans should have at least one allocation.");
            }

            dp.getAllocations().values().forEach(a -> {
                if (a.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Allocations cannot be a negative value.");
                }
            });
        });

    }
}
