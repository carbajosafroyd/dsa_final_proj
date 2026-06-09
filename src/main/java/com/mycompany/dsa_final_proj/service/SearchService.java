/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.service;

import com.mycompany.dsa_final_proj.model.SearchResult;
import com.mycompany.dsa_final_proj.tree.KDTree;

import java.util.List;

/**
 * Service layer for all spatial search operations.
 *
 * <p>This class acts as an interface between the JavaFX UI controllers and
 * the underlying KD-Tree data structure. The UI controllers should NEVER
 * interact with the KDTree directly. This enforces the Single Responsibility
 * Principle and keeps our architecture clean.</p>
 *
 * @author DNSC IT221 Team
 */
public class SearchService {

    private final KDTree tree;

    /**
     * Constructs a new SearchService.
     *
     * @param tree the KDTree instance to search against
     */
    public SearchService(KDTree tree) {
        if (tree == null) {
            throw new IllegalArgumentException("KDTree cannot be null");
        }
        this.tree = tree;
    }

    /**
     * Finds the single nearest facility to the specified coordinates.
     *
     * @param x the target X coordinate
     * @param y the target Y coordinate
     * @return a SearchResult containing the facility and distance, or null if the system is empty
     */
    public SearchResult findNearest(double x, double y) {
        return tree.nearestNeighbor(x, y);
    }

    /**
     * Finds the K nearest facilities to the specified coordinates.
     *
     * @param x the target X coordinate
     * @param y the target Y coordinate
     * @param k the number of facilities to find
     * @return a sorted list of the K nearest facilities (closest first)
     */
    public List<SearchResult> findKNearest(double x, double y, int k) {
        return tree.kNearestNeighbors(x, y, k);
    }

    /**
     * Finds all facilities within a specific radius of the target coordinates.
     *
     * @param x the target X coordinate
     * @param y the target Y coordinate
     * @param radius the maximum search distance
     * @return a list of facilities within the radius, sorted by distance (closest first)
     */
    public List<SearchResult> findWithinRadius(double x, double y, double radius) {
        return tree.radiusSearch(x, y, radius);
    }
}
