/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 *
 * Phase 4 Verification Test
 */

package com.mycompany.dsa_final_proj.service;

import com.mycompany.dsa_final_proj.model.Facility;
import com.mycompany.dsa_final_proj.model.FacilityType;
import com.mycompany.dsa_final_proj.persistence.DataStore;
import com.mycompany.dsa_final_proj.tree.KDTree;

import java.io.File;
import java.util.List;

public class CRUDTest {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║    Phase 4 Verification: CRUD & Persistence          ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        // Use a test-specific JSON file
        System.setProperty("user.home", System.getProperty("user.dir") + "/target");
        File jsonFile = new File(System.getProperty("user.dir") + "/target/dnsc_facilities.json");
        if (jsonFile.exists()) {
            jsonFile.delete();
        }

        testAddAndSave();
        testLoadAndRebuildTree();
        testUpdate();
        testRemove();

        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("  ALL PHASE 4 TESTS PASSED SUCCESSFULLY.");
        System.out.println("═══════════════════════════════════════════════════════");
    }

    private static void testAddAndSave() {
        System.out.println("── Test: Add & Save to JSON ────────────────────────────");
        KDTree tree = new KDTree();
        DataStore store = new DataStore();
        FacilityService service = new FacilityService(tree, store);

        service.addFacility(new Facility("Library", 50, 50, FacilityType.ACADEMIC, "Main Library"));
        service.addFacility(new Facility("Clinic", 20, 80, FacilityType.MEDICAL, "Campus Clinic"));

        File jsonFile = new File(System.getProperty("user.dir") + "/target/dnsc_facilities.json");
        assert jsonFile.exists() : "JSON file was not created!";
        assert jsonFile.length() > 0 : "JSON file is empty!";

        System.out.println("✅ PASS: Facilities added to KD-Tree and saved to " + jsonFile.getName());
    }

    private static void testLoadAndRebuildTree() {
        System.out.println("── Test: Load JSON & Rebuild Tree ──────────────────────");
        // Simulate an app restart by creating entirely new instances
        KDTree newTree = new KDTree();
        DataStore newStore = new DataStore();
        
        // The constructor should automatically load the JSON and call buildBalanced()
        FacilityService newService = new FacilityService(newTree, newStore);

        List<Facility> loaded = newService.getAllFacilities();
        assert loaded.size() == 2 : "Expected 2 facilities loaded, got " + loaded.size();
        
        boolean foundLibrary = false;
        for (Facility f : loaded) {
            if (f.getName().equals("Library")) foundLibrary = true;
        }
        assert foundLibrary : "Failed to load specific facility from JSON";

        assert newTree.getHeight() > 0 : "Tree was not rebuilt properly";

        System.out.println("✅ PASS: JSON loaded correctly and KD-Tree rebuilt successfully.");
    }

    private static void testUpdate() {
        System.out.println("── Test: Update Facility ───────────────────────────────");
        KDTree tree = new KDTree();
        DataStore store = new DataStore();
        FacilityService service = new FacilityService(tree, store); // Will load the 2 facilities

        Facility oldFac = new Facility("Library", 50, 50, FacilityType.ACADEMIC, "Main Library");
        Facility newFac = new Facility("Library (Renovated)", 55, 55, FacilityType.ACADEMIC, "Updated");

        boolean updated = service.updateFacility(oldFac, newFac);
        assert updated : "Update failed to find old facility";

        List<Facility> current = service.getAllFacilities();
        assert current.size() == 2 : "Size should remain 2 after update";
        
        boolean foundNew = false;
        for (Facility f : current) {
            if (f.getName().equals("Library (Renovated)")) foundNew = true;
        }
        assert foundNew : "New facility was not found after update";

        System.out.println("✅ PASS: Update correctly swapped nodes and preserved structure.");
    }

    private static void testRemove() {
        System.out.println("── Test: Remove Facility ───────────────────────────────");
        KDTree tree = new KDTree();
        DataStore store = new DataStore();
        FacilityService service = new FacilityService(tree, store);

        Facility toRemove = new Facility("Clinic", 20, 80, FacilityType.MEDICAL, "Campus Clinic");
        boolean removed = service.removeFacility(toRemove);
        assert removed : "Failed to remove clinic";

        assert service.getAllFacilities().size() == 1 : "Expected 1 facility remaining";

        System.out.println("✅ PASS: Lazy deletion and JSON save worked correctly.");
    }
}
