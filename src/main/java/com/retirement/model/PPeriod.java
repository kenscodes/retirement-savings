package com.retirement.model;

import java.time.LocalDateTime;

public class PPeriod {
    private double extra;
    private LocalDateTime start;
    private LocalDateTime end;

    public PPeriod() {}
    public PPeriod(double extra, LocalDateTime start, LocalDateTime end) {
        this.extra = extra;
        this.start = start;
        this.end = end;
    }

    public double getExtra() { return extra; }
    public void setExtra(double extra) { this.extra = extra; }
    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public boolean contains(LocalDateTime dt) {
        return !dt.isBefore(start) && !dt.isAfter(end);
    }
}
