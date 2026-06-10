/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mycompany.dsa_final_proj.model.Facility;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading facility data to a JSON file.
 *
 * <p>We don't save the actual KD-Tree structure (nodes, left/right pointers).
 * Instead, we just save the flat list of Facility objects. When the app starts,
 * we load this flat list and use {@code KDTree.buildBalanced()} to reconstruct
 * a perfectly balanced tree in memory.</p>
 *
 * @author DNSC IT221 Team
 */
public class DataStore {

    private static final String DATA_FILE = System.getProperty("user.home") + "/dnsc_facilities.json";
    private final Gson gson;

    public DataStore() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Saves a list of facilities to the JSON file.
     *
     * @param facilities the list to save
     */
    public void save(List<Facility> facilities) {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            gson.toJson(facilities, writer);
        } catch (IOException e) {
            System.err.println("Failed to save facilities to " + DATA_FILE);
            e.printStackTrace();
        }
    }

    /**
     * Loads facilities from the JSON file.
     *
     * @return the list of facilities, or an empty list if the file doesn't exist or an error occurs
     */
    public List<Facility> load() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Facility>>(){}.getType();
            List<Facility> facilities = gson.fromJson(reader, listType);
            return facilities != null ? facilities : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Failed to load facilities from " + DATA_FILE);
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
