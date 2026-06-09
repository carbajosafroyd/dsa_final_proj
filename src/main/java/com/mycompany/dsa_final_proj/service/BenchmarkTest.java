/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 *
 * Phase 8 Verification Test
 */

package com.mycompany.dsa_final_proj.service;

import com.mycompany.dsa_final_proj.model.BenchmarkResult;

public class BenchmarkTest {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║    Phase 8 Verification: Performance Benchmark       ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        BenchmarkService service = new BenchmarkService();
        
        System.out.println("Warming up JVM... (takes a few seconds)\n");
        // Throwaway run to ensure classes are loaded and JIT is fully active
        service.runNearestNeighborBenchmark(10000, 500, 500);

        int[] datasetSizes = { 100, 1_000, 10_000, 100_000, 1_000_000 };

        System.out.println("Running Nearest Neighbor benchmarks at Coordinate (500.0, 500.0)...\n");

        for (int size : datasetSizes) {
            // Target the center of our 1000x1000 random grid
            BenchmarkResult result = service.runNearestNeighborBenchmark(size, 500.0, 500.0);
            System.out.println(result.toString());
        }

        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("  BENCHMARK COMPLETE.");
        System.out.println("═══════════════════════════════════════════════════════");
    }
}
