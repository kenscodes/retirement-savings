package com.retirement.service;

import org.springframework.stereotype.Service;

/**
 * Adjusts nominal returns for inflation to get real (today's purchasing power) value.
 * A_real = A / (1 + inflation)^t
 */
@Service
public class InflationAdjuster {

    public double adjustForInflation(double nominalAmount, double inflationRate, int years) {
        if (years <= 0 || inflationRate <= 0) return nominalAmount;
        return nominalAmount / Math.pow(1.0 + inflationRate, years);
    }
}
