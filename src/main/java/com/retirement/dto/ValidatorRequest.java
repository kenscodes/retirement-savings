package com.retirement.dto;

import com.retirement.model.Expense;
import com.retirement.model.KPeriod;
import com.retirement.model.PPeriod;
import com.retirement.model.QPeriod;

import java.util.List;

/** Request DTO for /transactions/validator */
public class ValidatorRequest {
    private double wage;
    private List<Expense> transactions;

    public double getWage() { return wage; }
    public void setWage(double wage) { this.wage = wage; }
    public List<Expense> getTransactions() { return transactions; }
    public void setTransactions(List<Expense> transactions) { this.transactions = transactions; }
}
