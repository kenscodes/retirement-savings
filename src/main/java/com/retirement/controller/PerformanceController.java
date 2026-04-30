package com.retirement.controller;

import com.retirement.dto.PerformanceResponse;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;

/**
 * REST Controller for Performance Report:
 *   GET /blackrock/challenge/v1/performance
 */
@RestController
@RequestMapping("/blackrock/challenge/v1")
public class PerformanceController {

    /**
     * Endpoint 5: Performance metrics
     * Reports response time, memory usage, and thread count.
     */
    @GetMapping("/performance")
    public PerformanceResponse getPerformance() {
        // Uptime
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        long uptimeMs = runtimeBean.getUptime();
        Duration duration = Duration.ofMillis(uptimeMs);
        String time = String.format("%02d:%02d:%02d.%03d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart(),
                duration.toMillisPart());

        // Memory usage in MB
        Runtime runtime = Runtime.getRuntime();
        double usedMemoryMB = (runtime.totalMemory() - runtime.freeMemory()) / (1024.0 * 1024.0);
        String memory = String.format("%.2f", usedMemoryMB);

        // Thread count
        int threads = Thread.activeCount();

        return new PerformanceResponse(time, memory, threads);
    }
}
