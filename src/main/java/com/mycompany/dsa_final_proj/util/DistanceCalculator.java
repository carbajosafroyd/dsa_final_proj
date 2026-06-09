/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.util;

/**
 * Utility class for distance calculations in 2D space.
 *
 * <p><strong>Performance Note:</strong> Use {@link #squaredDistance} instead of
 * {@link #euclideanDistance} when you only need to <em>compare</em> distances.
 * Since sqrt is monotonic (if a < b, then sqrt(a) < sqrt(b)), comparing
 * squared distances gives the same ordering without the expensive sqrt call.
 * The KD-Tree search methods use squared distance internally for this reason.</p>
 *
 * @author DNSC IT221 Team
 */
public final class DistanceCalculator {

    /** Prevent instantiation — all methods are static. */
    private DistanceCalculator() {
    }

    /**
     * Computes the Euclidean distance between two 2D points.
     *
     * <p>Formula: sqrt((x2-x1)² + (y2-y1)²)</p>
     *
     * @param x1 X coordinate of point 1
     * @param y1 Y coordinate of point 1
     * @param x2 X coordinate of point 2
     * @param y2 Y coordinate of point 2
     * @return the Euclidean distance (always >= 0)
     */
    public static double euclideanDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(squaredDistance(x1, y1, x2, y2));
    }

    /**
     * Computes the squared Euclidean distance between two 2D points.
     *
     * <p>Formula: (x2-x1)² + (y2-y1)²</p>
     *
     * <p>Use this for distance <em>comparisons</em> to avoid the cost of
     * {@code Math.sqrt()}. The ordering is preserved because sqrt is monotonic.</p>
     *
     * @param x1 X coordinate of point 1
     * @param y1 Y coordinate of point 1
     * @param x2 X coordinate of point 2
     * @param y2 Y coordinate of point 2
     * @return the squared Euclidean distance (always >= 0)
     */
    public static double squaredDistance(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return dx * dx + dy * dy;
    }
}
