package com.retirement.model;

import java.time.LocalDateTime;

public class KPeriod {
    private LocalDateTime start;
    private LocalDateTime end;

    public KPeriod() {}
    public KPeriod(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() { return start; }
    public void setStart(LocalDateTime start) { this.start = start; }
    public LocalDateTime getEnd() { return end; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public boolean contains(LocalDateTime dt) {
        return !dt.isBefore(start) && !dt.isAfter(end);
    }
}
