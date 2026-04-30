package com.retirement.service;

import com.retirement.model.Expense;
import com.retirement.model.QPeriod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Step 2: Apply Q-Period rules (Fixed Amount Override).
 * When a transaction falls within a q period, REPLACE remanent with fixed amount.
 * If multiple match: use the one with latest start date (first in list for ties).
 */
@Service
public class QPeriodProcessor {

    public void applyQPeriods(List<Expense> expenses, List<QPeriod> qPeriods) {
        if (qPeriods == null || qPeriods.isEmpty()) return;

        for (Expense expense : expenses) {
            QPeriod bestMatch = findBestMatch(expense.getDate(), qPeriods);
            if (bestMatch != null) {
                expense.setRemanent(bestMatch.getFixed());
            }
        }
    }

    QPeriod findBestMatch(LocalDateTime date, List<QPeriod> qPeriods) {
        QPeriod best = null;
        for (QPeriod qPeriod : qPeriods) {
            if (qPeriod.contains(date)) {
                if (best == null || qPeriod.getStart().isAfter(best.getStart())) {
                    best = qPeriod;
                }
            }
        }
        return best;
    }
}
