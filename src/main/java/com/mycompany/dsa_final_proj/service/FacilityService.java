/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.service;

import com.mycompany.dsa_final_proj.model.Facility;
import com.mycompany.dsa_final_proj.persistence.DataStore;
import com.mycompany.dsa_final_proj.tree.KDTree;

import java.util.List;

/**
 * Service layer for all Facility CRUD (Create, Read, Update, Delete) operations.
 *
 * <p>This acts as the bridge between the UI and the KD-Tree/DataStore.
 * Every time data is modified, it updates the KD-Tree in memory and
 * immediately saves the new state to the JSON file.</p>
 *
 * @author DNSC IT221 Team
 */
public class FacilityService {

    private final KDTree tree;
    private final DataStore dataStore;

    /**
     * Initializes the service, loading existing facilities from disk
     * and building the balanced KD-Tree.
     *
     * @param tree the KDTree instance
     * @param dataStore the persistence store
     */
    public FacilityService(KDTree tree, DataStore dataStore) {
        this.tree = tree;
        this.dataStore = dataStore;

        List<Facility> savedFacilities = dataStore.load();
        if (!savedFacilities.isEmpty()) {
            tree.buildBalanced(savedFacilities);
        }
    }

    /**
     * Adds a new facility to the tree and saves to disk.
     */
    public void addFacility(Facility facility) {
        if (facility == null) return;
        
        tree.insert(facility);
        saveState();
    }

    /**
     * Removes a facility (lazy deletion) and saves to disk.
     */
    public boolean removeFacility(Facility facility) {
        if (facility == null) return false;

        boolean deleted = tree.delete(facility);
        if (deleted) {
            saveState();
        }
        return deleted;
    }

    /**
     * Updates an existing facility.
     *
     * <p>Because KD-Trees organize nodes based on X/Y coordinates, changing
     * coordinates while inside the tree violates the structural invariants.
     * Therefore, an update is performed as a removal of the old node
     * and an insertion of the new one.</p>
     */
    public boolean updateFacility(Facility oldFacility, Facility newFacility) {
        if (oldFacility == null || newFacility == null) return false;

        boolean deleted = tree.delete(oldFacility);
        if (!deleted) {
            return false;
        }

        tree.insert(newFacility);
        
        saveState();
        return true;
    }

    /**
     * Retrieves all active facilities currently in the tree.
     */
    public List<Facility> getAllFacilities() {
        return tree.getAllFacilities();
    }

    /**
     * Rebuilds the KD-Tree from scratch to ensure perfect balance,
     * then saves to disk. (Useful for an admin "Optimize Data" button).
     */
    public void optimizeAndSave() {
        List<Facility> activeFacilities = tree.getAllFacilities();
        tree.buildBalanced(activeFacilities);
        saveState();
    }

    private void saveState() {
        dataStore.save(tree.getAllFacilities());
    }
}
