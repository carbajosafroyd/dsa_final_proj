# Phase 5 — Nearest Neighbor Search

> **Status:** ✅ Complete
> **Date Completed:** June 9, 2026
> **Owner:** Dev 1 (The Engine)

---

## 1. Objectives

- Implement the fundamental spatial search algorithm (Nearest Neighbor) using recursive backtracking.
- Implement the K-Nearest Neighbors variation using a Max-Heap (`PriorityQueue`).
- Create the `SearchService` to act as an abstraction layer between the KD-Tree and the UI.
- Verify that KD-Tree results perfectly match a brute-force linear search to guarantee algorithmic correctness.

---

## 2. The Backtracking Algorithm (For Defense)

The nearest neighbor search is the most critical logic in the project. It works in three steps:

1. **Descent (Greedy Guess):** We traverse down the tree just like we are inserting the point, making a greedy choice at each node based on the split dimension. The leaf node we reach becomes our "current best guess".
2. **Ascent (Backtracking):** As the recursion unwinds, we check if the current node is closer than our "current best guess". If so, we update the best guess.
3. **Pruning (The Secret Sauce):** At each node during the ascent, we calculate the perpendicular distance from the target point to the splitting plane (the X or Y axis depending on depth). 
   - If this perpendicular distance is **greater** than the distance to our current best guess, it is mathematically impossible for the other side of the tree to contain a closer point. We completely prune (skip) that subtree.
   - If the perpendicular distance is **smaller**, the other side of the tree *might* have a closer point, so we must recursively search it.

This pruning is what reduces the time complexity from `O(n)` (checking every node) to an average of `O(log n)`.

---

## 3. Implementation Details

### 3.1 Tree Search Logic

**File:** [`KDTree.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/tree/KDTree.java)

- **`nearestNeighbor(double x, double y)`**: Implements the logic described above.
- **`kNearestNeighbors(double x, double y, int k)`**: Uses the exact same traversal and pruning logic, but instead of keeping a single `KDNode best`, it maintains a `PriorityQueue<SearchResult>` configured as a **Max-Heap**. 
   - A Max-Heap ensures that the *furthest* element in our top K is always at the root of the queue.
   - When we find a new point, if the queue isn't full (size < K), we add it.
   - If the queue is full, we compare the new point's distance to the Max-Heap's root. If it's closer, we pop the root and insert the new point.

### 3.2 Tie-Breaking

**File:** [`SearchResult.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/model/SearchResult.java)

During testing, we discovered that multiple facilities can be exactly the same distance from a query point. We updated `SearchResult.compareTo()` to use the `Facility.getName()` as a secondary sort key if distances are equal. This ensures that spatial queries are 100% deterministic, which is a hallmark of professional software.

### 3.3 Service Layer

**File:** [`SearchService.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/service/SearchService.java)

We created a dedicated `SearchService` class. The UI controllers will hold a reference to this service, rather than interacting with `KDTree` directly. This respects the **Single Responsibility Principle** and cleanly separates the presentation layer from the data structures layer.

---

## 4. Verification & Testing

**File:** [`SearchTest.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/service/SearchTest.java)

To prove our KD-Tree search logic is flawless, we wrote a test suite that dynamically generates a grid of 100 coordinates. 

For both `findNearest` and `findKNearest`, the test:
1. Queries the KD-Tree.
2. Runs a `for`-loop over the raw list calculating every single distance (Brute-Force Linear Search).
3. Asserts that the KD-Tree results **perfectly match** the Brute-Force results.

**Test Results:**
- ✅ `findNearest` matched perfectly.
- ✅ `findKNearest` matched perfectly.
- Performance: The KD-Tree skipped the vast majority of the dataset due to correct spatial pruning.
