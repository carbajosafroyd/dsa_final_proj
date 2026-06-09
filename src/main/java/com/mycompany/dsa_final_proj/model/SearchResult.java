/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.model;

/**
 * Wraps a {@link Facility} with its computed distance from a query point.
 *
 * <p>Returned by search operations (nearest neighbor, K-nearest, radius search).
 * Implements {@link Comparable} so results can be sorted by distance.</p>
 *
 * @author DNSC IT221 Team
 */
public class SearchResult implements Comparable<SearchResult> {

    private final Facility facility;
    private final double distance;

    /**
     * Creates a new search result.
     *
     * @param facility the facility that was found
     * @param distance the Euclidean distance from the query point to this facility
     */
    public SearchResult(Facility facility, double distance) {
        this.facility = facility;
        this.distance = distance;
    }

    public Facility getFacility() {
        return facility;
    }

    /**
     * Returns the Euclidean distance from the query point.
     *
     * @return the distance (always >= 0)
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Compares by distance (ascending).
     * Closer facilities come first when sorting.
     */
    @Override
    public int compareTo(SearchResult other) {
        return Double.compare(this.distance, other.distance);
    }

    @Override
    public String toString() {
        return String.format("%s — Distance: %.2f", facility.getName(), distance);
    }
}
