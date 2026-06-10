/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.service;

import com.mycompany.dsa_final_proj.model.BenchmarkResult;
import com.mycompany.dsa_final_proj.model.Facility;
import com.mycompany.dsa_final_proj.tree.KDTree;
import com.mycompany.dsa_final_proj.util.DistanceCalculator;
import com.mycompany.dsa_final_proj.util.SampleDataGenerator;

import java.util.List;

/**
 * Service to benchmark KD-Tree vs Linear Search performance.
 *
 * <p>This acts as the mathematical proof required by the IT221 rubric
 * that the KD-Tree provides superior O(log n) search performance
 * compared to an O(n) array scan.</p>
 *
 * @author DNSC IT221 Team
 */
public class BenchmarkService {

    /**
     * Compares the performance of finding the nearest neighbor.
     *
     * @param datasetSize the number of facilities to simulate
     * @param targetX the query X coordinate
     * @param targetY the query Y coordinate
     * @return the benchmark results wrapped in a BenchmarkResult object
     */
    public BenchmarkResult runNearestNeighborBenchmark(int datasetSize, double targetX, double targetY) {
        List<Facility> rawList = SampleDataGenerator.generateRandomFacilities(datasetSize, 1000.0, 1000.0);
        
        KDTree tree = new KDTree();
        tree.buildBalanced(rawList);

        for (int i = 0; i < 50; i++) {
            tree.nearestNeighbor(targetX, targetY);
            linearSearch(rawList, targetX, targetY);
        }

        long kdStart = System.nanoTime();
        tree.nearestNeighbor(targetX, targetY);
        long kdEnd = System.nanoTime();
        long kdTime = kdEnd - kdStart;

        long linearStart = System.nanoTime();
        linearSearch(rawList, targetX, targetY);
        long linearEnd = System.nanoTime();
        long linearTime = linearEnd - linearStart;

        return new BenchmarkResult(datasetSize, linearTime, kdTime);
    }

    /**
     * The brute-force linear search baseline (O(n)).
     */
    private Facility linearSearch(List<Facility> list, double targetX, double targetY) {
        Facility best = null;
        double bestDistSq = Double.MAX_VALUE;

        for (Facility f : list) {
            if (!f.isActive()) continue;
            
            double distSq = DistanceCalculator.squaredDistance(targetX, targetY, f.getX(), f.getY());
            if (distSq < bestDistSq) {
                bestDistSq = distSq;
                best = f;
            }
        }
        return best;
    }
}
