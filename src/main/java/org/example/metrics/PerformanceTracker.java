package org.example.metrics;

import org.json.JSONObject;

public class PerformanceTracker {
    private final boolean enabled;
    private long comparisons;
    private long unions;
    private long arrayAccesses;
    private long allocations;
    private double startTimeNs;
    private double executionTimeMs;
    private boolean running;


    public PerformanceTracker() {
        this(true);
    }

    public PerformanceTracker(boolean enabled) {
        this.enabled = enabled;
    }

    public void start() {
        if (!enabled || running) return;
        running = true;
        startTimeNs = System.nanoTime();
    }

    public void stop() {
        if (!enabled || !running) return;
        executionTimeMs = (System.nanoTime() - startTimeNs) / 1_000_000;
        running = false;
    }

    public void reset() {
        if (!enabled) return;
        comparisons = unions = arrayAccesses = allocations = 0;
        executionTimeMs = 0.0;
        running = false;
    }

    public void incComparisons() { if (enabled) comparisons++; }

    public void incComparisons(int number) { if (enabled) comparisons += number; }

    public void incUnions() { if (enabled) unions++; }

    public void incArrayAccesses() { if (enabled) arrayAccesses++; }

    public void incArrayAccesses(int number) { if (enabled) arrayAccesses += number; }

    public void incAllocations() { if (enabled) allocations++; }

    public void incAllocations(int number) { if (enabled) comparisons += number; }

    public long getComparisons() { return comparisons; }

    public long getUnions() { return unions; }

    public long getArrayAccesses() { return arrayAccesses; }

    public long getAllocations() { return allocations; }

    public double getExecutionTimeMs() { return executionTimeMs; }

    public boolean isEnabled() { return enabled; }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("enabled", enabled);
        obj.put("comparisons", comparisons);
        obj.put("unions", unions);
        obj.put("arrayAccesses", arrayAccesses);
        obj.put("allocations", allocations);
        obj.put("executionTimeMs", executionTimeMs);
        return obj;
    }

    public String toCsvRow(String dataset, int vertices, int edges, String algorithm, double totalCost) {
        return String.format(
                "%s,%d,%d,%s,%.2f,%d,%d,%d,%d,%d",
                dataset,
                vertices,
                edges,
                algorithm,
                totalCost,
                comparisons,
                unions,
                arrayAccesses,
                allocations,
                executionTimeMs
        );
    }
}
