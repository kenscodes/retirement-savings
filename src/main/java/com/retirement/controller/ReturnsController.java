package com.retirement.controller;

import com.retirement.dto.ReturnsRequest;
import com.retirement.dto.ReturnsResponse;
import com.retirement.dto.ReturnsResponse.SavingsByDate;
import com.retirement.model.Expense;
import com.retirement.model.KPeriod;
import com.retirement.model.QPeriod;
import com.retirement.model.PPeriod;
import com.retirement.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST Controller for Returns Calculation:
 *   POST /blackrock/challenge/v1/returns/nps    (NPS at 7.11%)
 *   POST /blackrock/challenge/v1/returns/index  (NIFTY 50 at 14.49%)
 */
@RestController
@RequestMapping("/blackrock/challenge/v1/returns")
public class ReturnsController {

    @Autowired private RemanentCalculator remanentCalculator;
    @Autowired private QPeriodProcessor qPeriodProcessor;
    @Autowired private PPeriodProcessor pPeriodProcessor;
    @Autowired private KPeriodGrouper kPeriodGrouper;
    @Autowired private TaxCalculator taxCalculator;
    @Autowired private InvestmentCalculator investmentCalculator;
    @Autowired private InflationAdjuster inflationAdjuster;

    /**
     * Endpoint 4a: NPS Returns (7.11%, with tax benefit)
     */
    @PostMapping("/nps")
    public ReturnsResponse calculateNpsReturns(@RequestBody ReturnsRequest request) {
        return calculateReturns(request, InvestmentCalculator.NPS_RATE, true);
    }

    /**
     * Endpoint 4b: Index (NIFTY 50) Returns (14.49%, taxBenefit = 0)
     */
    @PostMapping("/index")
    public ReturnsResponse calculateIndexReturns(@RequestBody ReturnsRequest request) {
        return calculateReturns(request, InvestmentCalculator.NIFTY_RATE, false);
    }

    private ReturnsResponse calculateReturns(ReturnsRequest request, double rate, boolean isNps) {
        List<Expense> transactions = request.getTransactions();
        List<QPeriod> qPeriods = request.getQ() != null ? request.getQ() : Collections.emptyList();
        List<PPeriod> pPeriods = request.getP() != null ? request.getP() : Collections.emptyList();
        List<KPeriod> kPeriods = request.getK() != null ? request.getK() : Collections.emptyList();

        int yearsToRetirement = Math.max(60 - request.getAge(), 5);
        double annualIncome = request.getWage() * 12;
        // Inflation comes as percentage (5.5) or decimal (0.055)
        double inflation = request.getInflation() > 1 ? request.getInflation() / 100.0 : request.getInflation();

        // Step 1: Calculate remanents
        remanentCalculator.calculateRemanents(transactions);

        // Remove duplicates (keep first occurrence)
        Set<String> seenDates = new LinkedHashSet<>();
        List<Expense> validTransactions = new ArrayList<>();
        for (Expense tx : transactions) {
            if (tx.getAmount() >= 0 && seenDates.add(tx.getDate().toString())) {
                validTransactions.add(tx);
            }
        }

        // Step 2: Apply Q-periods
        qPeriodProcessor.applyQPeriods(validTransactions, qPeriods);

        // Step 3: Apply P-periods
        pPeriodProcessor.applyPPeriods(validTransactions, pPeriods);

        // Calculate totals from valid transactions
        double totalTransactionAmount = validTransactions.stream().mapToDouble(Expense::getAmount).sum();
        double totalCeiling = validTransactions.stream().mapToDouble(Expense::getCeiling).sum();

        // Step 4: Calculate savings per k-period
        List<SavingsByDate> savingsByDates = new ArrayList<>();

        for (KPeriod kPeriod : kPeriods) {
            double kAmount = kPeriodGrouper.sumForPeriod(validTransactions, kPeriod);

            // Step 5: Calculate compound interest return
            double nominalReturn = investmentCalculator.calculateReturn(kAmount, rate, yearsToRetirement);

            // Step 6: Inflation adjustment
            double realReturn = inflationAdjuster.adjustForInflation(nominalReturn, inflation, yearsToRetirement);

            // Profit = real return - invested amount
            double profit = Math.round((realReturn - kAmount) * 100.0) / 100.0;

            // Tax benefit (NPS only)
            double taxBenefit = 0.0;
            if (isNps) {
                taxBenefit = taxCalculator.calculateNPSTaxBenefit(annualIncome, kAmount);
                taxBenefit = Math.round(taxBenefit * 100.0) / 100.0;
            }

            savingsByDates.add(new SavingsByDate(
                    kPeriod.getStart(), kPeriod.getEnd(),
                    kAmount, profit, taxBenefit
            ));
        }

        return new ReturnsResponse(totalTransactionAmount, totalCeiling, savingsByDates);
    }
}
