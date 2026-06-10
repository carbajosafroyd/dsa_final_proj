/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.model;

import java.util.Objects;

/**
 * Represents a campus facility with a name, 2D coordinates, type, and description.
 *
 * <p>This is the primary data entity stored in the KD-Tree. Each facility
 * has an (x, y) coordinate that represents its position on the campus map.</p>
 *
 * <p><strong>Design Note:</strong> Coordinates should NOT be modified while
 * the facility is inside the KD-Tree, as this would violate the tree's
 * spatial ordering. To "move" a facility, remove it and re-insert it
 * with new coordinates.</p>
 *
 * @author DNSC IT221 Team
 */
public class Facility {

    private String name;
    private double x;
    private double y;
    private FacilityType type;
    private String description;

    /**
     * Flag for lazy deletion in the KD-Tree.
     *
     * <p>Instead of physically removing a node (which is complex in KD-Trees
     * due to dimensional splitting), we mark it as inactive. The tree
     * periodically rebuilds to purge inactive nodes.</p>
     *
     * <p><strong>Why lazy deletion?</strong> True KD-Tree deletion requires
     * finding a replacement node that satisfies the splitting dimension
     * invariant — a notoriously error-prone operation. Lazy deletion is
     * a legitimate strategy used in production spatial indexes.</p>
     */
    private boolean active;

    /**
     * Constructs a new active Facility.
     *
     * @param name        the facility name (e.g., "Main Library")
     * @param x           the X coordinate on the campus map
     * @param y           the Y coordinate on the campus map
     * @param type        the category of this facility
     * @param description a brief description of the facility
     * @throws IllegalArgumentException if name is null or blank
     */
    public Facility(String name, double x, double y, FacilityType type, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Facility name cannot be null or blank.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Facility type cannot be null.");
        }

        this.name = name;
        this.x = x;
        this.y = y;
        this.type = type;
        this.description = (description != null) ? description : "";
        this.active = true;
    }


    public String getName() {
        return name;
    }

    /**
     * Returns the X coordinate of this facility.
     * Used by the KD-Tree at even depths (0, 2, 4, ...) for splitting.
     *
     * @return the X coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the Y coordinate of this facility.
     * Used by the KD-Tree at odd depths (1, 3, 5, ...) for splitting.
     *
     * @return the Y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns the coordinate value for a given dimension.
     * This is the key method used by the KD-Tree during traversal.
     *
     * @param dimension 0 for X, 1 for Y
     * @return the coordinate value for that dimension
     * @throws IllegalArgumentException if dimension is not 0 or 1
     */
    public double getCoordinate(int dimension) {
        return switch (dimension) {
            case 0 -> x;
            case 1 -> y;
            default -> throw new IllegalArgumentException(
                    "Invalid dimension: " + dimension + ". Must be 0 (X) or 1 (Y)."
            );
        };
    }

    public FacilityType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setType(FacilityType type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Marks this facility as inactive (lazy deletion).
     * The facility remains in the tree but is excluded from search results.
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * Two facilities are equal if they have the same name and coordinates.
     * This is used when searching the tree for a specific facility to delete.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facility facility = (Facility) o;
        return Double.compare(facility.x, x) == 0
                && Double.compare(facility.y, y) == 0
                && Objects.equals(name, facility.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, x, y);
    }

    @Override
    public String toString() {
        return String.format("%s (%.1f, %.1f) [%s]%s",
                name, x, y, type.getDisplayName(),
                active ? "" : " [INACTIVE]");
    }
}
