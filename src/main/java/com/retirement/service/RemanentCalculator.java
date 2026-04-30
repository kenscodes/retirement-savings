package com.retirement.service;

import com.retirement.model.Expense;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Step 1: Calculate ceiling and remanent for each expense.
 * ceiling = next multiple of 100 above amount
 * remanent = ceiling - amount
 */
@Service
public class RemanentCalculator {

    private static final double ROUNDING_MULTIPLE = 100.0;

    public void calculateRemanents(List<Expense> expenses) {
        for (Expense expense : expenses) {
            double amount = expense.getAmount();
            double ceiling = Math.ceil(amount / ROUNDING_MULTIPLE) * ROUNDING_MULTIPLE;
            expense.setCeiling(ceiling);
            expense.setRemanent(ceiling - amount);
        }
    }

    public double calculateSingleRemanent(double amount) {
        double ceiling = Math.ceil(amount / ROUNDING_MULTIPLE) * ROUNDING_MULTIPLE;
        return ceiling - amount;
    }
}
