package com.mycompany.dsa_final_proj.ui;

import java.util.List;

/**
 * TEMPORARY stub services — provides fake data so the UI can be built
 * before Dev 1 finishes the real service implementations.
 *
 * DELETE this file on Integration Day (Day 7) and replace with real services.
 */
public class StubServices {

    public static class Facility {
        public final String name;
        public final double x, y;
        public final String type;
        public final String description;

        public Facility(String name, double x, double y, String type, String description) {
            this.name = name; this.x = x; this.y = y;
            this.type = type; this.description = description;
        }

        @Override public String toString() { return name; }
    }

    public static class StubFacilityService {
        private static final List<Facility> data = new java.util.ArrayList<>(List.of(
            new Facility("Davao del Norte State College GAD", 195.2, 411.2, "ADMINISTRATIVE", "Gender and Development Office"),
            new Facility("BUGSAI TBI", 407.2, 152.0, "ACADEMIC", "Technology Business Incubator"),
            new Facility("DNSC - Academic Building", 598.4, 162.4, "ACADEMIC", "Main academic classrooms"),
            new Facility("DNSC Audio Visual Room", 528.0, 289.6, "ACADEMIC", "AVR for events and seminars")
        ));

        public List<Facility> getAllFacilities() { return data; }
        public int getSize() { return data.size(); }
        public void addFacility(Facility f) { data.add(f); }
        public void removeFacility(Facility f) { data.remove(f); }
    }

    public static class SearchResult {
        public final Facility facility;
        public final double distance;
        public SearchResult(Facility f, double d) { this.facility = f; this.distance = d; }
    }

    public static class StubSearchService {
        private final StubFacilityService fs = new StubFacilityService();

        public SearchResult findNearest(double x, double y) {
            return fs.getAllFacilities().stream()
                .map(f -> new SearchResult(f, Math.hypot(f.x - x, f.y - y)))
                .min(java.util.Comparator.comparingDouble(r -> r.distance))
                .orElse(null);
        }

        public List<SearchResult> findKNearest(double x, double y, int k) {
            return fs.getAllFacilities().stream()
                .map(f -> new SearchResult(f, Math.hypot(f.x - x, f.y - y)))
                .sorted(java.util.Comparator.comparingDouble(r -> r.distance))
                .limit(k)
                .collect(java.util.stream.Collectors.toList());
        }

        public List<SearchResult> findWithinRadius(double x, double y, double radius) {
            return fs.getAllFacilities().stream()
                .map(f -> new SearchResult(f, Math.hypot(f.x - x, f.y - y)))
                .filter(r -> r.distance <= radius)
                .collect(java.util.stream.Collectors.toList());
        }
    }

    public static class BenchmarkResult {
        public final long linearSearchTimeNs;
        public final long kdTreeSearchTimeNs;
        public final int dataSize;
        public BenchmarkResult(long l, long k, int d) {
            linearSearchTimeNs = l; kdTreeSearchTimeNs = k; dataSize = d;
        }
    }

    public static class StubBenchmarkService {
        public java.util.List<BenchmarkResult> runBenchmarkSuite() {
            return java.util.List.of(
                new BenchmarkResult(500, 100, 100),
                new BenchmarkResult(2500, 150, 500),
                new BenchmarkResult(5000, 200, 1000),
                new BenchmarkResult(25000, 250, 5000),
                new BenchmarkResult(50000, 300, 10000),
                new BenchmarkResult(250000, 400, 50000),
                new BenchmarkResult(500000, 500, 100000)
            );
        }
    }
}
