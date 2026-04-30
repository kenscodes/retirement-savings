package com.retirement.dto;

import java.time.LocalDateTime;
import java.util.List;

/** Response DTO for /returns/nps and /returns/index */
public class ReturnsResponse {
    private double totalTransactionAmount;
    private double totalCeiling;
    private List<SavingsByDate> savingsByDates;

    public ReturnsResponse() {}
    public ReturnsResponse(double totalTransactionAmount, double totalCeiling, List<SavingsByDate> savingsByDates) {
        this.totalTransactionAmount = totalTransactionAmount;
        this.totalCeiling = totalCeiling;
        this.savingsByDates = savingsByDates;
    }

    public double getTotalTransactionAmount() { return totalTransactionAmount; }
    public void setTotalTransactionAmount(double v) { this.totalTransactionAmount = v; }
    public double getTotalCeiling() { return totalCeiling; }
    public void setTotalCeiling(double v) { this.totalCeiling = v; }
    public List<SavingsByDate> getSavingsByDates() { return savingsByDates; }
    public void setSavingsByDates(List<SavingsByDate> v) { this.savingsByDates = v; }

    /** Each k-period's savings breakdown */
    public static class SavingsByDate {
        private LocalDateTime start;
        private LocalDateTime end;
        private double amount;
        private double profit;
        private double taxBenefit;

        public SavingsByDate() {}
        public SavingsByDate(LocalDateTime start, LocalDateTime end, double amount, double profit, double taxBenefit) {
            this.start = start;
            this.end = end;
            this.amount = amount;
            this.profit = profit;
            this.taxBenefit = taxBenefit;
        }

        public LocalDateTime getStart() { return start; }
        public void setStart(LocalDateTime start) { this.start = start; }
        public LocalDateTime getEnd() { return end; }
        public void setEnd(LocalDateTime end) { this.end = end; }
        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
        public double getProfit() { return profit; }
        public void setProfit(double profit) { this.profit = profit; }
        public double getTaxBenefit() { return taxBenefit; }
        public void setTaxBenefit(double taxBenefit) { this.taxBenefit = taxBenefit; }
    }
}
