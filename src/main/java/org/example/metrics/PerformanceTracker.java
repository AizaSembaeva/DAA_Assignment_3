package org.example.metrics;

import org.json.JSONObject;

public class PerformanceTracker {
    private long comparisons;
    private long unions;
    private long arrayAccesses;
    private long allocations;
    private long startTimeNs;
    private long executionTimeMs;
    private boolean running;

    public void start() {
        if (!running) {
            running = true;
            startTimeNs = System.nanoTime();
        }
    }

    public void stop() {
        if (running) {
            executionTimeMs = (System.nanoTime() - startTimeNs) / 1_000_000;
            running = false;
        }
    }

    public void reset() {
        comparisons = unions = arrayAccesses = allocations = executionTimeMs = 0;
        running = false;
    }

    public void incComparisons() {
        comparisons++;
    }

    public void incUnions() {
        unions++;
    }

    public void incArrayAccesses() {
        arrayAccesses++;
    }

    public void incArrayAccesses(int number) {
        arrayAccesses += number;
    }

    public void incAllocations() {
        allocations++;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getUnions() {
        return unions;
    }

    public long getArrayAccesses() {
        return arrayAccesses;
    }

    public long getAllocations() {
        return allocations;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("comparisons", comparisons);
        obj.put("unions", unions);
        obj.put("arrayAccesses", arrayAccesses);
        obj.put("allocations", allocations);
        obj.put("executionTimeMs", executionTimeMs);
        return obj;
    }

    public String toCsvRow(String dataset, int vertices, int edges, String algorithm, double totalCost) {
        return String.format("%s,%d,%d,%s,%.2f,%d,%d,%d,%d,%d",
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
