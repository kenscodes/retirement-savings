package com.retirement.model;

import java.time.LocalDateTime;

/**
 * Represents a single expense/transaction.
 * After parsing, includes ceiling and remanent.
 */
public class Expense {

    private LocalDateTime date;
    private double amount;
    private double ceiling;
    private double remanent;

    public Expense() {}

    public Expense(LocalDateTime date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public double getCeiling() { return ceiling; }
    public void setCeiling(double ceiling) { this.ceiling = ceiling; }

    public double getRemanent() { return remanent; }
    public void setRemanent(double remanent) { this.remanent = remanent; }

    @Override
    public String toString() {
        return String.format("Expense{date=%s, amount=%.0f, ceiling=%.0f, remanent=%.0f}", date, amount, ceiling, remanent);
    }
}
