/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.util;

import com.mycompany.dsa_final_proj.model.Facility;
import com.mycompany.dsa_final_proj.model.FacilityType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility to generate massive datasets for benchmarking.
 *
 * @author DNSC IT221 Team
 */
public final class SampleDataGenerator {

    private SampleDataGenerator() {}

    /**
     * Generates a list of random facilities.
     *
     * @param count the number of facilities to generate
     * @param maxX the maximum X coordinate
     * @param maxY the maximum Y coordinate
     * @return list of generated facilities
     */
    public static List<Facility> generateRandomFacilities(int count, double maxX, double maxY) {
        List<Facility> list = new ArrayList<>(count);
        Random random = new Random(42);
        
        FacilityType[] types = FacilityType.values();

        for (int i = 0; i < count; i++) {
            double x = random.nextDouble() * maxX;
            double y = random.nextDouble() * maxY;
            FacilityType type = types[random.nextInt(types.length)];
            
            list.add(new Facility("Fac_" + i, x, y, type, "Auto-generated"));
        }

        return list;
    }
}
