/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.model;

/**
 * Wraps the results of a performance comparison between Linear Search
 * and KD-Tree Search.
 *
 * @author DNSC IT221 Team
 */
public class BenchmarkResult {

    private final int datasetSize;
    private final long linearTimeNanos;
    private final long kdTreeTimeNanos;

    public BenchmarkResult(int datasetSize, long linearTimeNanos, long kdTreeTimeNanos) {
        this.datasetSize = datasetSize;
        this.linearTimeNanos = linearTimeNanos;
        this.kdTreeTimeNanos = kdTreeTimeNanos;
    }

    public int getDatasetSize() {
        return datasetSize;
    }

    public long getLinearTimeNanos() {
        return linearTimeNanos;
    }

    public long getKdTreeTimeNanos() {
        return kdTreeTimeNanos;
    }

    /**
     * Calculates how many times faster the KD-Tree was compared to linear search.
     */
    public double getSpeedupMultiplier() {
        if (kdTreeTimeNanos == 0) return linearTimeNanos; // Prevent division by zero
        return (double) linearTimeNanos / kdTreeTimeNanos;
    }

    @Override
    public String toString() {
        return String.format("Dataset: %,d | Linear: %,d ns | KD-Tree: %,d ns | Speedup: %.2fx",
                datasetSize, linearTimeNanos, kdTreeTimeNanos, getSpeedupMultiplier());
    }
}
