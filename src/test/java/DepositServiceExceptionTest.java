import com.jacelendro.deposit.DepositService;
import com.jacelendro.deposit.model.DepositPlan;
import com.jacelendro.deposit.model.DepositPlanType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class DepositServiceExceptionTest {

    @Test()
    void whenDepositPlansAreNull() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
           DepositService.processDeposits(null, null);
        });
    }

    @Test()
    void whenDepositPlansAreEmpty() {

        List<DepositPlan> depositPlans = new ArrayList<>();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DepositService.processDeposits(depositPlans, null);
        });
    }

    @Test()
    void whenDepositPlansAreMoreThan2() {
        DepositPlan oneTime = new DepositPlan(DepositPlanType.ONE_TIME);
        oneTime.addAllocation("High risk", new BigDecimal(10000));
        oneTime.addAllocation("Retirement", new BigDecimal(500));

        DepositPlan monthly = new DepositPlan(DepositPlanType.MONTHLY);
        monthly.addAllocation("High risk", new BigDecimal(0));
        monthly.addAllocation("Retirement", new BigDecimal(100));

        DepositPlan monthly2 = new DepositPlan(DepositPlanType.MONTHLY);
        monthly2.addAllocation("High risk", new BigDecimal(0));
        monthly2.addAllocation("Retirement", new BigDecimal(100));

        List<DepositPlan> depositPlans = new ArrayList<>();
        depositPlans.add(oneTime);
        depositPlans.add(monthly);
        depositPlans.add(monthly2);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DepositService.processDeposits(depositPlans, null);
        });
    }

    @Test()
    void whenDepositPlansAreSameType() {
        DepositPlan monthly = new DepositPlan(DepositPlanType.MONTHLY);
        monthly.addAllocation("High risk", new BigDecimal(0));
        monthly.addAllocation("Retirement", new BigDecimal(100));

        DepositPlan monthly2 = new DepositPlan(DepositPlanType.MONTHLY);
        monthly2.addAllocation("High risk", new BigDecimal(0));
        monthly2.addAllocation("Retirement", new BigDecimal(100));

        List<DepositPlan> depositPlans = new ArrayList<>();
        depositPlans.add(monthly);
        depositPlans.add(monthly2);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DepositService.processDeposits(depositPlans, null);
        });
    }

    @Test()
    void whenDepositPlansHaveNoAllocation() {
        DepositPlan oneTime = new DepositPlan(DepositPlanType.ONE_TIME);

        List<DepositPlan> depositPlans = new ArrayList<>();
        depositPlans.add(oneTime);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DepositService.processDeposits(depositPlans, null);
        });
    }

    @Test()
    void whenDepositPlansHaveNegativeAllocation() {
        DepositPlan oneTime = new DepositPlan(DepositPlanType.ONE_TIME);
        oneTime.addAllocation("High risk", new BigDecimal(10000));
        oneTime.addAllocation("Retirement", new BigDecimal(500));

        DepositPlan monthly = new DepositPlan(DepositPlanType.MONTHLY);
        monthly.addAllocation("High risk", new BigDecimal(0));
        monthly.addAllocation("Retirement", new BigDecimal(-100));

        List<DepositPlan> depositPlans = new ArrayList<>();
        depositPlans.add(oneTime);
        depositPlans.add(monthly);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            DepositService.processDeposits(depositPlans, null);
        });
    }

}
