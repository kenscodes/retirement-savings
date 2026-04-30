package com.retirement.service;

import org.springframework.stereotype.Service;

/**
 * Tax calculator for Indian income tax (Simplified New Regime).
 */
@Service
public class TaxCalculator {

    private static final double[] SLAB_LIMITS = {700000, 1000000, 1200000, 1500000, Double.MAX_VALUE};
    private static final double[] SLAB_RATES = {0.00, 0.10, 0.15, 0.20, 0.30};
    private static final double NPS_DEDUCTION_CAP = 200000;

    public double calculateTax(double income) {
        if (income <= 0) return 0.0;
        double totalTax = 0.0;
        double previousLimit = 0;
        for (int i = 0; i < SLAB_LIMITS.length; i++) {
            if (income <= previousLimit) break;
            double taxableInSlab = Math.min(income, SLAB_LIMITS[i]) - previousLimit;
            if (taxableInSlab > 0) totalTax += taxableInSlab * SLAB_RATES[i];
            previousLimit = SLAB_LIMITS[i];
        }
        return totalTax;
    }

    public double calculateNPSTaxBenefit(double annualIncome, double npsInvestment) {
        double npsDeduction = Math.min(npsInvestment, Math.min(annualIncome * 0.10, NPS_DEDUCTION_CAP));
        return calculateTax(annualIncome) - calculateTax(annualIncome - npsDeduction);
    }

    public double calculateMaxNPSInvestment(double annualIncome, double availableAmount) {
        return Math.max(0, Math.min(availableAmount, Math.min(annualIncome * 0.10, NPS_DEDUCTION_CAP)));
    }
}
