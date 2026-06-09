# Phase 3 — KD-Tree Implementation

> **Status:** ✅ Complete
> **Date Completed:** June 9, 2026
> **Owner:** Dev 1 (The Engine)

---

## 1. Objectives

- Implement the core spatial data structures (`KDNode`, `KDTree`)
- Create the fundamental data models (`Facility`, `FacilityType`, `SearchResult`)
- Build utility classes required for spatial operations (`DistanceCalculator`)
- Implement O(log n) tree insertion
- Implement O(n log n) balanced tree construction
- Implement lazy deletion strategy for tree maintenance
- Verify structural correctness with console-based test cases

---

## 2. Core Data Models

### 2.1 Facility and FacilityType

**Files:** 
- [`FacilityType.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/model/FacilityType.java)
- [`Facility.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/model/Facility.java)

`FacilityType` defines the categorization of campus buildings (Academic, Medical, Sports, etc.).

The `Facility` class represents the 2D point data stored in our tree. 
**Key Design Decision:** It exposes a `getCoordinate(int dimension)` method. This abstracts the X (dimension 0) and Y (dimension 1) coordinates so the KD-Tree can dynamically switch between axes without hardcoding `if (dim == 0) return x; else return y;` everywhere in the tree logic. It also holds an `active` boolean for lazy deletion.

### 2.2 SearchResult and DistanceCalculator

**Files:**
- [`SearchResult.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/model/SearchResult.java)
- [`DistanceCalculator.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/util/DistanceCalculator.java)

`DistanceCalculator` provides both `euclideanDistance` and `squaredDistance`. 
**Performance Optimization:** The KD-Tree internal logic strictly uses `squaredDistance` to avoid the CPU cost of `Math.sqrt()` during rapid comparisons. The exact Euclidean distance is only computed for the final UI output wrapped in a `SearchResult`.

---

## 3. KD-Tree Data Structure

### 3.1 KDNode

**File:** [`KDNode.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/tree/KDNode.java)

The node stores a `Facility`, references to its left and right children, and its current `depth` in the tree. 
**Core Logic:** The splitting dimension is determined dynamically using modulo arithmetic: `depth % 2`. Even depths split on the X-axis; odd depths split on the Y-axis.

### 3.2 KDTree Implementation

**File:** [`KDTree.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/tree/KDTree.java)

The `KDTree` class is the engine of the application. The following operations were implemented in this phase:

| Operation | Method | Time Complexity | Description |
|-----------|--------|-----------------|-------------|
| **Insertion** | `insert(Facility)` | O(log n) avg | Recursively traverses the tree, comparing X or Y based on the depth, placing the new facility at the appropriate leaf position. |
| **Balanced Build** | `buildBalanced(List)` | O(n log n) | Replaces the current tree. Sorts the list by the current dimension and chooses the median element as the root. Recursively builds the left and right subtrees. This prevents tree degeneration. |
| **Lazy Deletion** | `delete(Facility)` | O(log n) avg | Finds a node and marks its `active` flag to false. Rebuilds the tree automatically if inactive nodes exceed 30% of total nodes. |

---

## 4. Verification & Testing

**File:** [`KDTreeTest.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/tree/KDTreeTest.java)

A pure console test suite was created to validate the data structure before building the GUI.

**Test Results:**
- ✅ **Sequential Insert:** Correctly placed 5 nodes and calculated height correctly.
- ✅ **Balanced Construction:** Built a perfectly balanced tree of 7 nodes (height = 3). Verified root split on median.
- ✅ **Lazy Deletion:** Successfully marked nodes inactive, reducing the active size while preserving the tree structure.
- ✅ **Edge Cases:** Handled empty trees, duplicate coordinates, and non-existent deletions without crashing.

---

## 5. Notes for the Manuscript (Methodology & Defense)

This phase provides the core technical foundation for the manuscript:

- **Algorithm Mechanics:** The `insert` and `buildBalanced` implementations perfectly illustrate spatial partitioning. The alternation of axes (`depth % 2`) is the defining feature of the KD-Tree.
- **Design Decisions (Lazy Deletion):** Deleting a node from a KD-Tree requires finding a replacement node that honors the dimension split invariant—a complex and error-prone process. By choosing "Lazy Deletion" with periodic median-based rebuilding, we ensure the tree remains fast and stable without risking structural integrity. This is a defensible, production-ready engineering decision.
- **Performance:** `DistanceCalculator.squaredDistance()` highlights an understanding of micro-optimizations in high-frequency algorithmic operations.
