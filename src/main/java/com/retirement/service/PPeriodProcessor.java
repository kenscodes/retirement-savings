package com.retirement.service;

import com.retirement.model.Expense;
import com.retirement.model.PPeriod;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Step 3: Apply P-Period rules (Extra Amount Addition).
 * When a transaction falls within a p period, ADD extra to remanent.
 * Multiple p periods stack (all extras are added).
 * Applied AFTER q periods.
 */
@Service
public class PPeriodProcessor {

    public void applyPPeriods(List<Expense> expenses, List<PPeriod> pPeriods) {
        if (pPeriods == null || pPeriods.isEmpty()) return;

        for (Expense expense : expenses) {
            double totalExtra = pPeriods.stream()
                    .filter(p -> p.contains(expense.getDate()))
                    .mapToDouble(PPeriod::getExtra)
                    .sum();
            if (totalExtra > 0) {
                expense.setRemanent(expense.getRemanent() + totalExtra);
            }
        }
    }
}
