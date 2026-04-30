package com.retirement.service;

import com.retirement.model.Expense;
import com.retirement.model.KPeriod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Step 4: Group and sum remanents by K-periods.
 * Each k-period sums remanents independently.
 * A transaction can be in multiple k-periods (counted multiple times).
 */
@Service
public class KPeriodGrouper {

    /**
     * Calculate the total remanent sum for a specific k-period.
     */
    public double sumForPeriod(List<Expense> expenses, KPeriod kPeriod) {
        return expenses.stream()
                .filter(e -> kPeriod.contains(e.getDate()))
                .mapToDouble(Expense::getRemanent)
                .sum();
    }

    /**
     * Calculate the total across all k-periods.
     * If no k periods, sum all remanents.
     */
    public double groupAndSum(List<Expense> expenses, List<KPeriod> kPeriods) {
        if (kPeriods == null || kPeriods.isEmpty()) {
            return expenses.stream().mapToDouble(Expense::getRemanent).sum();
        }
        double total = 0;
        for (KPeriod kPeriod : kPeriods) {
            total += sumForPeriod(expenses, kPeriod);
        }
        return total;
    }
}
