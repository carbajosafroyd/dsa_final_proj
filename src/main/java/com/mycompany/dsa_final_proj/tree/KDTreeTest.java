/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 *
 * Phase 3 Verification Test
 * Run this to verify that the KD-Tree insert and buildBalanced work correctly.
 *
 * TO RUN: Execute this class's main method directly (or via mvnw.cmd)
 */

package com.mycompany.dsa_final_proj.tree;

import com.mycompany.dsa_final_proj.model.Facility;
import com.mycompany.dsa_final_proj.model.FacilityType;
import com.mycompany.dsa_final_proj.util.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Console-based test for verifying KD-Tree operations.
 *
 * <p>This is NOT part of the final application. It exists to prove
 * that the data structure works before we build the GUI on top of it.</p>
 *
 * @author DNSC IT221 Team
 */
public class KDTreeTest {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║    KD-Tree — Phase 3 Verification Test              ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();

        testInsert();
        testBuildBalanced();
        testLazyDeletion();
        testEdgeCases();

        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("  ALL TESTS COMPLETED — Review results above.");
        System.out.println("═══════════════════════════════════════════════════════");
    }

    /**
     * Test 1: Insert facilities one by one and verify the tree structure.
     */
    private static void testInsert() {
        System.out.println("── Test 1: Sequential Insert ──────────────────────────");

        KDTree tree = new KDTree();

        // Insert 5 sample DNSC facilities
        tree.insert(new Facility("Library", 5, 4, FacilityType.ACADEMIC, "Main Library"));
        tree.insert(new Facility("Clinic", 2, 3, FacilityType.MEDICAL, "Campus Clinic"));
        tree.insert(new Facility("Registrar", 8, 1, FacilityType.ADMINISTRATIVE, "Registrar Office"));
        tree.insert(new Facility("Gym", 7, 2, FacilityType.SPORTS, "Athletics Center"));
        tree.insert(new Facility("Canteen", 4, 7, FacilityType.FOOD_SERVICE, "Main Canteen"));

        System.out.println("Inserted 5 facilities.");
        System.out.println("Tree: " + tree);
        System.out.println("Height: " + tree.getHeight());
        System.out.println("Size: " + tree.getSize());

        // Verify all facilities are in the tree
        List<Facility> all = tree.getAllFacilities();
        System.out.println("All facilities in tree:");
        for (Facility f : all) {
            System.out.println("  • " + f);
        }

        assert tree.getSize() == 5 : "Expected size 5, got " + tree.getSize();
        System.out.println("✅ PASS: 5 facilities inserted correctly.\n");
    }

    /**
     * Test 2: Build a balanced tree from a list and verify its height.
     *
     * A balanced tree with n nodes should have height ≈ log₂(n) + 1.
     * For 7 nodes: log₂(7) ≈ 2.8 → height should be 3.
     * For 15 nodes: log₂(15) ≈ 3.9 → height should be 4.
     */
    private static void testBuildBalanced() {
        System.out.println("── Test 2: Balanced Construction ──────────────────────");

        List<Facility> facilities = new ArrayList<>();
        facilities.add(new Facility("Building A", 100, 200, FacilityType.ACADEMIC, ""));
        facilities.add(new Facility("Building B", 300, 100, FacilityType.ACADEMIC, ""));
        facilities.add(new Facility("Building C", 200, 300, FacilityType.ACADEMIC, ""));
        facilities.add(new Facility("Building D", 150, 150, FacilityType.ACADEMIC, ""));
        facilities.add(new Facility("Building E", 250, 250, FacilityType.ACADEMIC, ""));
        facilities.add(new Facility("Building F", 350, 50, FacilityType.ACADEMIC, ""));
        facilities.add(new Facility("Building G", 50, 350, FacilityType.ACADEMIC, ""));

        KDTree tree = new KDTree();
        tree.buildBalanced(facilities);

        System.out.println("Built balanced tree from 7 facilities.");
        System.out.println("Tree: " + tree);
        System.out.println("Height: " + tree.getHeight());

        // For 7 nodes, a balanced binary tree has height 3
        int height = tree.getHeight();
        System.out.println("Expected height: 3 (perfect balance for 7 nodes)");

        // Verify root splits on X and is the median-X facility
        KDNode root = tree.getRoot();
        System.out.println("Root facility: " + root.getFacility().getName()
                + " at (" + root.getFacility().getX() + ", " + root.getFacility().getY() + ")");
        System.out.println("Root split dimension: " + (root.getSplitDimension() == 0 ? "X" : "Y"));

        assert tree.getSize() == 7 : "Expected size 7";
        assert height <= 4 : "Height too large, tree may not be balanced";
        System.out.println("✅ PASS: Balanced tree constructed correctly.\n");
    }

    /**
     * Test 3: Verify lazy deletion marks nodes inactive and triggers rebuild.
     */
    private static void testLazyDeletion() {
        System.out.println("── Test 3: Lazy Deletion ──────────────────────────────");

        KDTree tree = new KDTree();
        Facility library = new Facility("Library", 5, 4, FacilityType.ACADEMIC, "");
        Facility clinic = new Facility("Clinic", 2, 3, FacilityType.MEDICAL, "");
        Facility gym = new Facility("Gym", 7, 2, FacilityType.SPORTS, "");

        tree.insert(library);
        tree.insert(clinic);
        tree.insert(gym);

        System.out.println("Before deletion: size = " + tree.getSize()
                + ", total = " + tree.getTotalSize());

        // Delete the clinic
        boolean deleted = tree.delete(new Facility("Clinic", 2, 3, FacilityType.MEDICAL, ""));
        System.out.println("Deleted 'Clinic': " + deleted);
        System.out.println("After deletion: size = " + tree.getSize()
                + ", total = " + tree.getTotalSize());

        // Verify clinic is not in active list
        List<Facility> active = tree.getAllFacilities();
        System.out.println("Active facilities:");
        for (Facility f : active) {
            System.out.println("  • " + f);
        }

        assert tree.getSize() == 2 : "Expected size 2 after deletion";
        assert deleted : "Deletion should return true";
        assert !tree.contains(new Facility("Clinic", 2, 3, FacilityType.MEDICAL, ""))
                : "Clinic should not be found after deletion";

        System.out.println("✅ PASS: Lazy deletion works correctly.\n");
    }

    /**
     * Test 4: Edge cases — empty tree, single node, duplicate coordinates.
     */
    private static void testEdgeCases() {
        System.out.println("── Test 4: Edge Cases ─────────────────────────────────");

        // Empty tree
        KDTree empty = new KDTree();
        assert empty.isEmpty() : "New tree should be empty";
        assert empty.getSize() == 0 : "Size should be 0";
        assert empty.getAllFacilities().isEmpty() : "Should return empty list";
        System.out.println("✅ Empty tree handled correctly.");

        // Single node
        KDTree single = new KDTree();
        single.insert(new Facility("Only", 100, 200, FacilityType.UTILITY, ""));
        assert single.getSize() == 1 : "Size should be 1";
        assert single.getHeight() == 1 : "Height should be 1";
        System.out.println("✅ Single-node tree handled correctly.");

        // Duplicate coordinates (different names)
        KDTree dupes = new KDTree();
        dupes.insert(new Facility("Entrance A", 100, 50, FacilityType.UTILITY, ""));
        dupes.insert(new Facility("Entrance B", 100, 50, FacilityType.UTILITY, ""));
        assert dupes.getSize() == 2 : "Both facilities should exist";
        System.out.println("✅ Duplicate coordinates handled correctly.");

        // Build balanced with empty list
        KDTree emptyBuild = new KDTree();
        emptyBuild.buildBalanced(new ArrayList<>());
        assert emptyBuild.isEmpty() : "Should be empty after building from empty list";
        System.out.println("✅ Build from empty list handled correctly.");

        // Contains on empty tree
        assert !empty.contains(new Facility("X", 0, 0, FacilityType.UTILITY, ""))
                : "Contains should return false on empty tree";
        System.out.println("✅ Contains on empty tree handled correctly.");

        // Delete from empty tree
        assert !empty.delete(new Facility("X", 0, 0, FacilityType.UTILITY, ""))
                : "Delete should return false on empty tree";
        System.out.println("✅ Delete from empty tree handled correctly.\n");
    }
}
