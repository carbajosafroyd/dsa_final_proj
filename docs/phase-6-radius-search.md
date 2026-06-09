# Phase 6 — Radius Search

> **Status:** ✅ Complete
> **Date Completed:** June 9, 2026
> **Owner:** Dev 1 (The Engine)

---

## 1. Objectives

- Implement a Range Search (Radius Search) algorithm within the KD-Tree.
- Expose this functionality through the `SearchService`.
- Verify mathematical correctness by matching the KD-Tree output against a brute-force list scan.

---

## 2. The Radius Search Algorithm (For Defense)

Radius search answers queries like: *"Find all clinics within a 50-meter radius."* 

While a basic list must calculate the distance to every single facility on campus, our KD-Tree uses mathematical boundaries to skip entire regions of the map.

### How it works:
1. **Intersection Checking:** The tree recursively traverses its nodes. At any given node, it asks: *"Does my search circle overlap with the left side of this splitting line? Does it overlap with the right side?"*
2. **Left overlap:** If `TargetCoordinate - Radius <= SplitValue`, the circle reaches into the left half-plane. We must search the left branch.
3. **Right overlap:** If `TargetCoordinate + Radius >= SplitValue`, the circle reaches into the right half-plane. We must search the right branch.
4. **Complete Pruning:** If the circle *only* overlaps the left side, the right branch is completely ignored (and vice versa).

This logic guarantees that we never test a facility if its entire enclosing region is entirely outside our search circle.

---

## 3. Implementation Details

**File:** [`KDTree.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/tree/KDTree.java)

- **`radiusSearch(double x, double y, double radius)`**: 
  We implemented a performance optimization here: instead of calculating `Math.sqrt()` for every single active node checked, we square the query radius upfront (`radiusSq = radius * radius`). During traversal, we compare squared distances. We only compute the expensive square root when an item is actually confirmed to be inside the circle and needs to be wrapped in a `SearchResult`.

**File:** [`SearchService.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/service/SearchService.java)

- Added `findWithinRadius(x, y, radius)`. The UI layer will use this to draw a circle on the JavaFX Canvas and highlight the dots that fall inside it.

---

## 4. Verification

**File:** [`SearchTest.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/service/SearchTest.java)

We extended the Phase 5 test suite to generate a 100-point grid and search for everything within a radius of 15.0 from the center point (50.0, 50.0).

**Results:**
- KD-Tree found exactly 9 facilities.
- Brute Force Linear Scan found exactly 9 facilities.
- The facilities and their exact distances matched perfectly.

---

## 5. Architectural Milestone

With the completion of Phase 6, **the "Engine" (Data Structure + Algorithms) of the system is 100% complete**. 
We now have a mathematically proven, high-performance spatial index capable of:
1. Fast Insertions O(log n)
2. Balanced Construction O(n log n)
3. Safe Deletions (Lazy Deletion)
4. Nearest Neighbor Search
5. K-Nearest Neighbors
6. Radius Search
