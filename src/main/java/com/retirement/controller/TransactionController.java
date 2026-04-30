package com.retirement.controller;

import com.retirement.dto.FilterRequest;
import com.retirement.dto.ValidatorRequest;
import com.retirement.dto.ValidatorResponse;
import com.retirement.model.Expense;
import com.retirement.model.KPeriod;
import com.retirement.model.PPeriod;
import com.retirement.model.QPeriod;
import com.retirement.service.RemanentCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST Controller for Transaction endpoints:
 *   POST /blackrock/challenge/v1/transactions/parse
 *   POST /blackrock/challenge/v1/transactions/validator
 *   POST /blackrock/challenge/v1/transactions/filter
 */
@RestController
@RequestMapping("/blackrock/challenge/v1/transactions")
public class TransactionController {

    @Autowired
    private RemanentCalculator remanentCalculator;

    /**
     * Endpoint 1: Transaction Builder
     * Receives expenses and enriches with ceiling and remanent.
     */
    @PostMapping("/parse")
    public List<Expense> parseTransactions(@RequestBody List<Expense> expenses) {
        remanentCalculator.calculateRemanents(expenses);
        return expenses;
    }

    /**
     * Endpoint 2: Transaction Validator
     * Validates transactions: checks for negative amounts and duplicates.
     */
    @PostMapping("/validator")
    public ValidatorResponse validateTransactions(@RequestBody ValidatorRequest request) {
        List<Object> valid = new ArrayList<>();
        List<Object> invalid = new ArrayList<>();

        Set<String> seenDates = new HashSet<>();

        for (Expense tx : request.getTransactions()) {
            // Check for negative amounts
            if (tx.getAmount() < 0) {
                Map<String, Object> invalidTx = createTransactionMap(tx);
                invalidTx.put("message", "Negative amounts are not allowed");
                invalid.add(invalidTx);
                continue;
            }

            // Check for duplicate dates
            String dateKey = tx.getDate().toString();
            if (seenDates.contains(dateKey)) {
                Map<String, Object> invalidTx = createTransactionMap(tx);
                invalidTx.put("message", "Duplicate transaction");
                invalid.add(invalidTx);
                continue;
            }
            seenDates.add(dateKey);

            // Valid transaction
            Map<String, Object> validTx = createTransactionMap(tx);
            validTx.put("isPartOf", true);
            valid.add(validTx);
        }

        return new ValidatorResponse(valid, invalid);
    }

    /**
     * Endpoint 3: Temporal Constraints Validator
     * Applies q/p/k period rules and determines which transactions are valid/invalid.
     */
    @PostMapping("/filter")
    public ValidatorResponse filterTransactions(@RequestBody FilterRequest request) {
        List<Object> valid = new ArrayList<>();
        List<Object> invalid = new ArrayList<>();

        List<Expense> transactions = request.getTransactions();
        List<QPeriod> qPeriods = request.getQ() != null ? request.getQ() : Collections.emptyList();
        List<PPeriod> pPeriods = request.getP() != null ? request.getP() : Collections.emptyList();
        List<KPeriod> kPeriods = request.getK() != null ? request.getK() : Collections.emptyList();

        // Calculate remanents
        remanentCalculator.calculateRemanents(transactions);

        // Check for duplicates first
        Set<String> seenDates = new HashSet<>();
        Set<String> duplicateDates = new HashSet<>();
        for (Expense tx : transactions) {
            String dateKey = tx.getDate().toString();
            if (!seenDates.add(dateKey)) {
                duplicateDates.add(dateKey);
            }
        }

        seenDates.clear();

        for (Expense tx : transactions) {
            String dateKey = tx.getDate().toString();

            // Check negative
            if (tx.getAmount() < 0) {
                Map<String, Object> invalidTx = createTransactionMap(tx);
                invalidTx.put("message", "Negative amounts are not allowed");
                invalid.add(invalidTx);
                continue;
            }

            // Check duplicates
            if (duplicateDates.contains(dateKey) && !seenDates.add(dateKey)) {
                Map<String, Object> invalidTx = createTransactionMap(tx);
                invalidTx.put("message", "Duplicate transaction");
                invalid.add(invalidTx);
                continue;
            }
            seenDates.add(dateKey);

            // Apply q-period override
            double remanent = tx.getRemanent();
            QPeriod bestQ = null;
            for (QPeriod q : qPeriods) {
                if (q.contains(tx.getDate())) {
                    if (bestQ == null || q.getStart().isAfter(bestQ.getStart())) {
                        bestQ = q;
                    }
                }
            }
            if (bestQ != null) {
                remanent = bestQ.getFixed();
            }

            // Apply p-period additions
            double totalExtra = pPeriods.stream()
                    .filter(p -> p.contains(tx.getDate()))
                    .mapToDouble(PPeriod::getExtra)
                    .sum();
            remanent += totalExtra;
            tx.setRemanent(remanent);

            // Check if transaction falls within any k-period
            boolean isPartOfK = kPeriods.isEmpty() ||
                    kPeriods.stream().anyMatch(k -> k.contains(tx.getDate()));

            Map<String, Object> validTx = createTransactionMap(tx);
            validTx.put("isPartOf", isPartOfK);
            valid.add(validTx);
        }

        return new ValidatorResponse(valid, invalid);
    }

    private Map<String, Object> createTransactionMap(Expense tx) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("date", tx.getDate());
        map.put("amount", tx.getAmount());
        map.put("ceiling", tx.getCeiling());
        map.put("remanent", tx.getRemanent());
        return map;
    }
}
