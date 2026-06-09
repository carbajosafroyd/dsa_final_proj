/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.model;

/**
 * Categorizes campus facilities by their function.
 *
 * <p>Used for filtering search results and color-coding facilities
 * on the visualization map.</p>
 *
 * @author DNSC IT221 Team
 */
public enum FacilityType {

    /** Classrooms, laboratories, lecture halls. */
    ACADEMIC("Academic"),

    /** Clinic, health services. */
    MEDICAL("Medical"),

    /** Gym, courts, athletic fields. */
    SPORTS("Sports"),

    /** Registrar, admin offices, HR. */
    ADMINISTRATIVE("Administrative"),

    /** Canteen, cafeteria, food stalls. */
    FOOD_SERVICE("Food Service"),

    /** Restrooms, parking, maintenance. */
    UTILITY("Utility");

    private final String displayName;

    FacilityType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns a human-readable name for display in the UI.
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
