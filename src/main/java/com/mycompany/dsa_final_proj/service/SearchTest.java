/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 *
 * Phase 5 Verification Test
 */

package com.mycompany.dsa_final_proj.service;

import com.mycompany.dsa_final_proj.model.Facility;
import com.mycompany.dsa_final_proj.model.FacilityType;
import com.mycompany.dsa_final_proj.model.SearchResult;
import com.mycompany.dsa_final_proj.tree.KDTree;
import com.mycompany.dsa_final_proj.util.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;

public class SearchTest {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║    Phase 5 Verification: Nearest Neighbor Search     ║");
        System.out.println("╚══════════════════════════════════════════════════════╝\n");

        KDTree tree = new KDTree();
        List<Facility> rawList = new ArrayList<>();

        // Generate a grid of points
        for (int x = 10; x <= 100; x += 10) {
            for (int y = 10; y <= 100; y += 10) {
                Facility f = new Facility("Fac " + x + "-" + y, x, y, FacilityType.ACADEMIC, "");
                rawList.add(f);
            }
        }
        
        // Build the tree balanced
        tree.buildBalanced(rawList);
        SearchService searchService = new SearchService(tree);

        testNearestNeighbor(searchService, rawList);
        testKNearestNeighbors(searchService, rawList);
        testRadiusSearch(searchService, rawList);

        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("  ALL PHASE 5 & 6 TESTS PASSED SUCCESSFULLY.");
        System.out.println("═══════════════════════════════════════════════════════");
    }

    private static void testNearestNeighbor(SearchService service, List<Facility> rawList) {
        System.out.println("── Test: findNearest ───────────────────────────────────");
        
        // Target point not exactly on a facility
        double tx = 33.0;
        double ty = 58.0;

        // 1. KD-Tree Search
        SearchResult kdResult = service.findNearest(tx, ty);

        // 2. Brute Force Linear Search (for validation)
        Facility bruteBest = null;
        double bruteBestDist = Double.MAX_VALUE;
        for (Facility f : rawList) {
            double d = DistanceCalculator.euclideanDistance(tx, ty, f.getX(), f.getY());
            if (d < bruteBestDist) {
                bruteBestDist = d;
                bruteBest = f;
            }
        }

        System.out.println("Target Coordinates: (" + tx + ", " + ty + ")");
        System.out.println("KD-Tree Result   : " + kdResult);
        System.out.println("Brute Force Result: " + bruteBest.getName() + " — Distance: " + bruteBestDist);

        assert kdResult.getFacility().equals(bruteBest) : "KD-Tree nearest neighbor failed!";
        System.out.println("✅ PASS: KD-Tree perfectly matched Brute Force Linear Search.\n");
    }

    private static void testKNearestNeighbors(SearchService service, List<Facility> rawList) {
        System.out.println("── Test: findKNearest ──────────────────────────────────");
        
        double tx = 76.0;
        double ty = 24.0;
        int K = 5;

        // 1. KD-Tree Search
        List<SearchResult> kdResults = service.findKNearest(tx, ty, K);

        // 2. Brute Force Linear Search (sort all, pick top K)
        List<SearchResult> bruteResults = new ArrayList<>();
        for (Facility f : rawList) {
            bruteResults.add(new SearchResult(f, DistanceCalculator.euclideanDistance(tx, ty, f.getX(), f.getY())));
        }
        bruteResults.sort(null); // uses Comparable (distance ascending)
        bruteResults = bruteResults.subList(0, K);

        System.out.println("Target Coordinates: (" + tx + ", " + ty + ") | K = " + K);
        System.out.println("KD-Tree Results:");
        for (SearchResult r : kdResults) {
            System.out.println("  " + r);
        }

        assert kdResults.size() == K : "Did not return exactly K results";
        for (int i = 0; i < K; i++) {
            assert kdResults.get(i).getFacility().equals(bruteResults.get(i).getFacility()) : "Mismatch at rank " + (i+1);
        }
        
        System.out.println("✅ PASS: KD-Tree K-Nearest perfectly matched Brute Force.\n");
    }

    private static void testRadiusSearch(SearchService service, List<Facility> rawList) {
        System.out.println("── Test: findWithinRadius ──────────────────────────────");
        
        double tx = 50.0;
        double ty = 50.0;
        double radius = 15.0;

        // 1. KD-Tree Search
        List<SearchResult> kdResults = service.findWithinRadius(tx, ty, radius);

        // 2. Brute Force Linear Search
        List<SearchResult> bruteResults = new ArrayList<>();
        for (Facility f : rawList) {
            double d = DistanceCalculator.euclideanDistance(tx, ty, f.getX(), f.getY());
            if (d <= radius) {
                bruteResults.add(new SearchResult(f, d));
            }
        }
        bruteResults.sort(null);

        System.out.println("Target Coordinates: (" + tx + ", " + ty + ") | Radius = " + radius);
        System.out.println("KD-Tree found " + kdResults.size() + " facilities.");
        for (SearchResult r : kdResults) {
            System.out.println("  " + r);
        }

        assert kdResults.size() == bruteResults.size() : "Count mismatch between KD-Tree and Brute Force";
        for (int i = 0; i < kdResults.size(); i++) {
            assert kdResults.get(i).getFacility().equals(bruteResults.get(i).getFacility()) : "Mismatch at rank " + (i+1);
        }
        
        System.out.println("✅ PASS: KD-Tree Radius Search perfectly matched Brute Force.");
    }
}
