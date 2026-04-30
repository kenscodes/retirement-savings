package com.retirement.service;

import org.springframework.stereotype.Service;

/**
 * Compound interest calculator for NPS and NIFTY 50.
 * Both instruments receive the FULL investment amount (parallel projections).
 */
@Service
public class InvestmentCalculator {

    public static final double NPS_RATE = 0.0711;   // 7.11%
    public static final double NIFTY_RATE = 0.1449;  // 14.49%

    public double calculateReturn(double principal, double rate, int years) {
        if (principal <= 0 || years <= 0) return principal;
        return principal * Math.pow(1.0 + rate, years);
    }

    public double calculateNPSReturn(double principal, int years) {
        return calculateReturn(principal, NPS_RATE, years);
    }

    public double calculateNiftyReturn(double principal, int years) {
        return calculateReturn(principal, NIFTY_RATE, years);
    }
}
