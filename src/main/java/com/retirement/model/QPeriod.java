package com.retirement.model;

import java.time.LocalDateTime;

public class QPeriod {
    private double fixed;
    private LocalDateTime start;
    private LocalDateTime end;

    public QPeriod() {}
    public QPeriod(double fixed, LocalDateTime start, LocalDateTime end) {
        this.fixed = fixed;
        this.start = start;
        this.end = end;
    }

    public double getFixed() { return fixed; }
    public void setFixed(double fixed) { this.fixed = fixed; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public boolean contains(LocalDateTime dt) {
        return !dt.isBefore(start) && !dt.isAfter(end);
    }
}
