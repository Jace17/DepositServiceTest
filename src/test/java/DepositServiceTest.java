import com.jacelendro.deposit.DepositService;
import com.jacelendro.deposit.model.Customer;
import com.jacelendro.deposit.model.DepositPlan;
import com.jacelendro.deposit.model.DepositPlanType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepositServiceTest {

    @Test
    @DisplayName("Happy case from requirements")
    void whenHappyCase () {
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
        assertEquals(new BigDecimal(10000), customer.getPortfolios().get("High risk").getAmount());
        assertEquals(new BigDecimal(600), customer.getPortfolios().get("Retirement").getAmount());
    }

    @Test
    @DisplayName("No deposits made")
    void whenNoDeposits () {
        DepositPlan oneTime = new DepositPlan(DepositPlanType.ONE_TIME);
        oneTime.addAllocation("High risk", new BigDecimal(10000));
        oneTime.addAllocation("Retirement", new BigDecimal(500));

        DepositPlan monthly = new DepositPlan(DepositPlanType.MONTHLY);
        monthly.addAllocation("High risk", new BigDecimal(0));
        monthly.addAllocation("Retirement", new BigDecimal(100));

        List<DepositPlan> depositPlans = new ArrayList<>();
        depositPlans.add(oneTime);
        depositPlans.add(monthly);

        Customer customer = DepositService.processDeposits(depositPlans, null);
        assertEquals(BigDecimal.ZERO, customer.getPortfolios().get("High risk").getAmount());
        assertEquals(BigDecimal.ZERO, customer.getPortfolios().get("Retirement").getAmount());
    }

    @Test
    @DisplayName("Three Portfolios")
    void threePortfolios () {
        DepositPlan oneTime = new DepositPlan(DepositPlanType.ONE_TIME);
        oneTime.addAllocation("High risk", new BigDecimal(10000));
        oneTime.addAllocation("Medium risk", new BigDecimal(20000));
        oneTime.addAllocation("Retirement", new BigDecimal(500));

        DepositPlan monthly = new DepositPlan(DepositPlanType.MONTHLY);
        monthly.addAllocation("High risk", new BigDecimal(0));
        monthly.addAllocation("Medium risk", new BigDecimal(1000));
        monthly.addAllocation("Retirement", new BigDecimal(100));

        List<DepositPlan> depositPlans = new ArrayList<>();
        depositPlans.add(oneTime);
        depositPlans.add(monthly);

        List<BigDecimal> deposits = new ArrayList<>();
        deposits.add(new BigDecimal(30500));
        deposits.add(new BigDecimal((1100)));

        Customer customer = DepositService.processDeposits(depositPlans, deposits);
        assertEquals(new BigDecimal(10000), customer.getPortfolios().get("High risk").getAmount());
        assertEquals(new BigDecimal(21000), customer.getPortfolios().get("Medium risk").getAmount());
        assertEquals(new BigDecimal(600), customer.getPortfolios().get("Retirement").getAmount());
    }

    @Test
    @DisplayName("Three Portfolios")
    void monthlyPlanOnly () {
        DepositPlan monthly = new DepositPlan(DepositPlanType.MONTHLY);
        monthly.addAllocation("High risk", new BigDecimal(200));
        monthly.addAllocation("Medium risk", new BigDecimal(500));
        monthly.addAllocation("Retirement", new BigDecimal(300));

        List<DepositPlan> depositPlans = new ArrayList<>();;
        depositPlans.add(monthly);

        List<BigDecimal> deposits = new ArrayList<>();
        deposits.add(new BigDecimal(1000));
        deposits.add(new BigDecimal((3000)));
        deposits.add(new BigDecimal((4000)));
        deposits.add(new BigDecimal((2000)));

        Customer customer = DepositService.processDeposits(depositPlans, deposits);
        assertEquals(new BigDecimal(2000), customer.getPortfolios().get("High risk").getAmount());
        assertEquals(new BigDecimal(5000), customer.getPortfolios().get("Medium risk").getAmount());
        assertEquals(new BigDecimal(3000), customer.getPortfolios().get("Retirement").getAmount());
    }
}