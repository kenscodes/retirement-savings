package com.retirement.dto;

import com.retirement.model.Expense;
import com.retirement.model.KPeriod;
import com.retirement.model.PPeriod;
import com.retirement.model.QPeriod;

import java.util.List;

/** Request DTO for /returns/nps and /returns/index */
public class ReturnsRequest {
    private int age;
    private double wage;
    private double inflation;
    private List<QPeriod> q;
    private List<PPeriod> p;
    private List<KPeriod> k;
    private List<Expense> transactions;

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWage() { return wage; }
    public void setWage(double wage) { this.wage = wage; }
    public double getInflation() { return inflation; }
    public void setInflation(double inflation) { this.inflation = inflation; }
    public List<QPeriod> getQ() { return q; }
    public void setQ(List<QPeriod> q) { this.q = q; }
    public List<PPeriod> getP() { return p; }
    public void setP(List<PPeriod> p) { this.p = p; }
    public List<KPeriod> getK() { return k; }
    public void setK(List<KPeriod> k) { this.k = k; }
    public List<Expense> getTransactions() { return transactions; }
    public void setTransactions(List<Expense> transactions) { this.transactions = transactions; }
}
