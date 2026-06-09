/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.tree;

import com.mycompany.dsa_final_proj.model.Facility;
import com.mycompany.dsa_final_proj.model.SearchResult;
import com.mycompany.dsa_final_proj.util.DistanceCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * A 2-Dimensional KD-Tree for spatial indexing of campus facilities.
 *
 * <p>This is the <strong>core data structure</strong> of the DNSC Smart Campus
 * Facility Finder. It organizes {@link Facility} objects by their (x, y)
 * coordinates, enabling efficient spatial queries:</p>
 *
 * <ul>
 *   <li>{@link #insert} — Add a facility: O(log n) average</li>
 *   <li>{@link #buildBalanced} — Construct a balanced tree from a list: O(n log n)</li>
 *   <li>{@code nearestNeighbor} — Find closest facility: O(log n) average (Phase 5)</li>
 *   <li>{@code kNearestNeighbors} — Find K closest: O(k log n) average (Phase 5)</li>
 *   <li>{@code radiusSearch} — Find all within radius: O(√n + m) average (Phase 6)</li>
 * </ul>
 *
 * <h3>How It Works</h3>
 * <p>The tree alternates splitting dimensions at each level:</p>
 * <pre>
 *   Depth 0: Compare X coordinates → go left if smaller, right if larger
 *   Depth 1: Compare Y coordinates → go left if smaller, right if larger
 *   Depth 2: Compare X coordinates → (cycle repeats)
 * </pre>
 *
 * <p>This spatial partitioning allows the tree to prune entire subtrees
 * during search, achieving logarithmic performance instead of the linear
 * scan required by a simple ArrayList.</p>
 *
 * <h3>Deletion Strategy</h3>
 * <p>This implementation uses <strong>lazy deletion</strong>: nodes are marked
 * as inactive rather than physically removed. When inactive nodes exceed
 * a threshold, the tree rebuilds itself from the remaining active nodes.
 * This is a legitimate production strategy that avoids the complexity
 * of true KD-Tree deletion (which requires finding replacement nodes
 * that respect the dimensional splitting invariant).</p>
 *
 * @author DNSC IT221 Team
 * @see KDNode
 * @see Facility
 */
public class KDTree {

    /** Rebuild the tree when inactive nodes exceed this fraction of total. */
    private static final double REBUILD_THRESHOLD = 0.3;

    /** Root node of the tree. Null if the tree is empty. */
    private KDNode root;

    /** Number of active (non-deleted) facilities in the tree. */
    private int activeSize;

    /** Total number of nodes in the tree (including inactive/deleted). */
    private int totalSize;

    /**
     * Constructs an empty KD-Tree.
     */
    public KDTree() {
        this.root = null;
        this.activeSize = 0;
        this.totalSize = 0;
    }

    // ═══════════════════════════════════════════════════════════════════
    // INSERT OPERATION — O(log n) average, O(n) worst case
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Inserts a facility into the KD-Tree.
     *
     * <p><strong>Algorithm:</strong></p>
     * <ol>
     *   <li>Start at the root (depth 0, split on X).</li>
     *   <li>Compare the facility's coordinate for the current split dimension.</li>
     *   <li>If smaller → go left. If greater or equal → go right.</li>
     *   <li>Repeat at the next depth (alternating dimension).</li>
     *   <li>When a null child is reached, insert the new node there.</li>
     * </ol>
     *
     * <p><strong>Time Complexity:</strong> O(log n) average when the tree
     * is balanced. O(n) worst case when points are inserted in sorted order
     * (tree degenerates to a linked list).</p>
     *
     * @param facility the facility to insert
     * @throws IllegalArgumentException if facility is null
     */
    public void insert(Facility facility) {
        if (facility == null) {
            throw new IllegalArgumentException("Cannot insert null facility.");
        }
        root = insertRecursive(root, facility, 0);
        activeSize++;
        totalSize++;
    }

    /**
     * Recursive helper for insertion.
     *
     * <p>At each node, we determine the split dimension from the depth,
     * compare the appropriate coordinate, and recurse into the correct child.</p>
     *
     * @param node     the current node (null if we've reached an empty spot)
     * @param facility the facility to insert
     * @param depth    the current depth in the tree
     * @return the (possibly new) node at this position
     */
    private KDNode insertRecursive(KDNode node, Facility facility, int depth) {
        // Base case: empty spot found — create a new node here
        if (node == null) {
            return new KDNode(facility, depth);
        }

        // Determine which dimension to compare at this depth
        // depth % 2: even depths → X (dimension 0), odd depths → Y (dimension 1)
        int dimension = node.getSplitDimension();

        // Compare the facility's coordinate with this node's split value
        double facilityValue = facility.getCoordinate(dimension);
        double nodeValue = node.getSplitValue();

        if (facilityValue < nodeValue) {
            // Facility's coordinate is smaller → go left
            node.setLeft(insertRecursive(node.getLeft(), facility, depth + 1));
        } else {
            // Facility's coordinate is greater or equal → go right
            node.setRight(insertRecursive(node.getRight(), facility, depth + 1));
        }

        return node;
    }

    // ═══════════════════════════════════════════════════════════════════
    // BALANCED CONSTRUCTION — O(n log n)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Builds a balanced KD-Tree from a list of facilities.
     *
     * <p><strong>Algorithm:</strong></p>
     * <ol>
     *   <li>Sort the list by the current split dimension (X at depth 0).</li>
     *   <li>Pick the median element as the root of this subtree.</li>
     *   <li>Recursively build the left subtree from elements before the median.</li>
     *   <li>Recursively build the right subtree from elements after the median.</li>
     * </ol>
     *
     * <p><strong>Why use the median?</strong> The median guarantees that roughly
     * half the points go left and half go right, producing a balanced tree
     * with O(log n) height. This prevents the worst-case degeneration that
     * can happen with sequential insertion.</p>
     *
     * <p><strong>Time Complexity:</strong> O(n log n) — sorting at each level
     * contributes O(n), and there are O(log n) levels.</p>
     *
     * <p>This method replaces any existing tree content.</p>
     *
     * @param facilities the list of facilities to build the tree from
     */
    public void buildBalanced(List<Facility> facilities) {
        if (facilities == null || facilities.isEmpty()) {
            root = null;
            activeSize = 0;
            totalSize = 0;
            return;
        }

        // Work on a mutable copy so we don't modify the caller's list
        List<Facility> mutableList = new ArrayList<>(facilities);

        root = buildBalancedRecursive(mutableList, 0);
        activeSize = facilities.size();
        totalSize = facilities.size();
    }

    /**
     * Recursive helper for balanced tree construction.
     *
     * @param facilities the sublist of facilities for this subtree
     * @param depth      the current depth
     * @return the root node of the constructed subtree
     */
    private KDNode buildBalancedRecursive(List<Facility> facilities, int depth) {
        if (facilities.isEmpty()) {
            return null;
        }

        // Only one element — it becomes a leaf node
        if (facilities.size() == 1) {
            return new KDNode(facilities.get(0), depth);
        }

        // Determine the split dimension for this depth
        int dimension = depth % KDNode.DIMENSIONS;

        // Sort by the current dimension to find the median
        // Dimension 0 → sort by X; Dimension 1 → sort by Y
        facilities.sort(Comparator.comparingDouble(f -> f.getCoordinate(dimension)));

        // Pick the median as the root of this subtree
        int medianIndex = facilities.size() / 2;

        // Create this node with the median facility
        KDNode node = new KDNode(facilities.get(medianIndex), depth);

        // Recursively build left subtree (elements before median)
        // and right subtree (elements after median)
        List<Facility> leftList = facilities.subList(0, medianIndex);
        List<Facility> rightList = facilities.subList(medianIndex + 1, facilities.size());

        node.setLeft(buildBalancedRecursive(new ArrayList<>(leftList), depth + 1));
        node.setRight(buildBalancedRecursive(new ArrayList<>(rightList), depth + 1));

        return node;
    }

    // ═══════════════════════════════════════════════════════════════════
    // LAZY DELETION — O(log n) average
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Marks a facility as inactive (lazy deletion).
     *
     * <p>The facility remains in the tree structure but is excluded from
     * all search results. If the ratio of inactive nodes exceeds
     * {@value #REBUILD_THRESHOLD} (30%), the tree automatically rebuilds
     * itself to reclaim space.</p>
     *
     * @param facility the facility to remove
     * @return true if the facility was found and deactivated, false otherwise
     */
    public boolean delete(Facility facility) {
        if (facility == null || root == null) {
            return false;
        }

        boolean found = markInactive(root, facility, 0);

        if (found) {
            activeSize--;
            rebuildIfNeeded();
        }

        return found;
    }

    /**
     * Searches the tree for the target facility and marks it inactive.
     *
     * @param node     current node being examined
     * @param target   the facility to deactivate
     * @param depth    current depth
     * @return true if the facility was found
     */
    private boolean markInactive(KDNode node, Facility target, int depth) {
        if (node == null) {
            return false;
        }

        // Check if this node is the target
        if (node.getFacility().isActive() && node.getFacility().equals(target)) {
            node.getFacility().setActive(false);
            return true;
        }

        // Determine which subtree to search based on split dimension
        int dimension = node.getSplitDimension();
        double targetValue = target.getCoordinate(dimension);
        double nodeValue = node.getSplitValue();

        if (targetValue < nodeValue) {
            return markInactive(node.getLeft(), target, depth + 1);
        } else {
            // Check right subtree first, but also check left for equal values
            boolean found = markInactive(node.getRight(), target, depth + 1);
            if (!found && targetValue == nodeValue) {
                found = markInactive(node.getLeft(), target, depth + 1);
            }
            return found;
        }
    }

    /**
     * Rebuilds the tree if the ratio of inactive nodes exceeds the threshold.
     * This is the "garbage collection" step of lazy deletion.
     */
    private void rebuildIfNeeded() {
        if (totalSize > 0 && (double) (totalSize - activeSize) / totalSize > REBUILD_THRESHOLD) {
            List<Facility> activeFacilities = getAllFacilities();
            buildBalanced(activeFacilities);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // TRAVERSAL & UTILITY METHODS
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Returns a list of all <em>active</em> facilities in the tree.
     *
     * <p>Performs an in-order traversal, collecting only facilities
     * where {@code isActive() == true}.</p>
     *
     * @return list of active facilities (never null, may be empty)
     */
    public List<Facility> getAllFacilities() {
        List<Facility> result = new ArrayList<>();
        collectActive(root, result);
        return result;
    }

    /**
     * In-order traversal that collects active facilities.
     */
    private void collectActive(KDNode node, List<Facility> result) {
        if (node == null) {
            return;
        }

        collectActive(node.getLeft(), result);

        if (node.getFacility().isActive()) {
            result.add(node.getFacility());
        }

        collectActive(node.getRight(), result);
    }

    /**
     * Checks if the tree contains a specific active facility.
     *
     * @param facility the facility to search for
     * @return true if the facility exists and is active
     */
    public boolean contains(Facility facility) {
        return contains(root, facility);
    }

    private boolean contains(KDNode node, Facility facility) {
        if (node == null) {
            return false;
        }

        if (node.getFacility().isActive() && node.getFacility().equals(facility)) {
            return true;
        }

        int dimension = node.getSplitDimension();
        double targetValue = facility.getCoordinate(dimension);
        double nodeValue = node.getSplitValue();

        if (targetValue < nodeValue) {
            return contains(node.getLeft(), facility);
        } else {
            boolean found = contains(node.getRight(), facility);
            if (!found && targetValue == nodeValue) {
                found = contains(node.getLeft(), facility);
            }
            return found;
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // SEARCH OPERATIONS (to be implemented in Phases 5 & 6)
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Finds the nearest active facility to the given (x, y) coordinates.
     *
     * <p><strong>Algorithm (Nearest Neighbor Backtracking):</strong></p>
     * <ol>
     *   <li>Descend the tree to find the leaf node where the target WOULD be inserted.</li>
     *   <li>On the way back up (backtracking), update the best known distance if the current node is closer.</li>
     *   <li><strong>Pruning step:</strong> Calculate the perpendicular distance to the splitting plane.
     *       If this distance is LESS than the best known distance, the other subtree might contain a closer point,
     *       so we must search it. Otherwise, we can safely prune (skip) the entire other subtree.</li>
     * </ol>
     *
     * @param x the target X coordinate
     * @param y the target Y coordinate
     * @return a SearchResult containing the nearest facility and its distance, or null if tree is empty
     */
    public SearchResult nearestNeighbor(double x, double y) {
        if (root == null || activeSize == 0) {
            return null;
        }

        KDNode bestNode = nearestRecursive(root, x, y, 0, null);

        if (bestNode == null) {
            return null; // Should only happen if all nodes are inactive
        }

        double distance = DistanceCalculator.euclideanDistance(
                x, y, bestNode.getFacility().getX(), bestNode.getFacility().getY());

        return new SearchResult(bestNode.getFacility(), distance);
    }

    private KDNode nearestRecursive(KDNode node, double targetX, double targetY, int depth, KDNode currentBest) {
        if (node == null) {
            return currentBest;
        }

        KDNode best = currentBest;

        // Check if the current node is active and closer than the current best
        if (node.getFacility().isActive()) {
            if (best == null) {
                best = node;
            } else {
                double currentDistSq = DistanceCalculator.squaredDistance(
                        targetX, targetY, node.getFacility().getX(), node.getFacility().getY());
                double bestDistSq = DistanceCalculator.squaredDistance(
                        targetX, targetY, best.getFacility().getX(), best.getFacility().getY());

                if (currentDistSq < bestDistSq) {
                    best = node;
                }
            }
        }

        // Determine which subtree to search first (the one that naturally contains the target)
        int dimension = node.getSplitDimension();
        double targetVal = (dimension == 0) ? targetX : targetY;
        double nodeVal = node.getSplitValue();

        KDNode firstSide = (targetVal < nodeVal) ? node.getLeft() : node.getRight();
        KDNode secondSide = (targetVal < nodeVal) ? node.getRight() : node.getLeft();

        // 1. Always search the side the target point belongs to
        best = nearestRecursive(firstSide, targetX, targetY, depth + 1, best);

        // 2. Check if we need to search the other side
        if (best != null) {
            double bestDistSq = DistanceCalculator.squaredDistance(
                    targetX, targetY, best.getFacility().getX(), best.getFacility().getY());

            // Perpendicular distance from target to the splitting plane (squared)
            double axisDistSq = (targetVal - nodeVal) * (targetVal - nodeVal);

            // If the splitting plane is closer than our current best, the other side might have a better point
            if (axisDistSq < bestDistSq) {
                best = nearestRecursive(secondSide, targetX, targetY, depth + 1, best);
            }
        } else {
            // If best is still null (e.g., all nodes visited so far were inactive), we must search the other side
            best = nearestRecursive(secondSide, targetX, targetY, depth + 1, best);
        }

        return best;
    }

    /**
     * Finds the K nearest active facilities to the given (x, y) coordinates.
     *
     * <p>Uses the same backtracking logic as nearestNeighbor, but maintains a
     * Max-Heap (PriorityQueue) of size K to keep track of the closest points found so far.</p>
     *
     * @param x the target X coordinate
     * @param y the target Y coordinate
     * @param k the maximum number of facilities to return
     * @return a sorted list of the K nearest facilities
     */
    public List<SearchResult> kNearestNeighbors(double x, double y, int k) {
        if (root == null || activeSize == 0 || k <= 0) {
            return new ArrayList<>();
        }

        // Max-Heap: The furthest of the K-nearest elements sits at the root of the PriorityQueue.
        // Collections.reverseOrder() ensures that the largest distance is polled first.
        PriorityQueue<SearchResult> pq = new PriorityQueue<>(Collections.reverseOrder());

        kNearestRecursive(root, x, y, k, 0, pq);

        // Extract results from Max-Heap into a list
        List<SearchResult> result = new ArrayList<>();
        while (!pq.isEmpty()) {
            result.add(pq.poll());
        }

        // The Max-Heap gives us elements from furthest to closest. Reverse to get closest first.
        Collections.reverse(result);
        return result;
    }

    private void kNearestRecursive(KDNode node, double targetX, double targetY, int k, int depth, PriorityQueue<SearchResult> pq) {
        if (node == null) {
            return;
        }

        if (node.getFacility().isActive()) {
            double dist = DistanceCalculator.euclideanDistance(
                    targetX, targetY, node.getFacility().getX(), node.getFacility().getY());

            if (pq.size() < k) {
                pq.offer(new SearchResult(node.getFacility(), dist));
            } else if (dist < pq.peek().getDistance()) {
                // We found a closer point. Remove the furthest point in our top K, and add this new one.
                pq.poll();
                pq.offer(new SearchResult(node.getFacility(), dist));
            }
        }

        int dimension = node.getSplitDimension();
        double targetVal = (dimension == 0) ? targetX : targetY;
        double nodeVal = node.getSplitValue();

        KDNode firstSide = (targetVal < nodeVal) ? node.getLeft() : node.getRight();
        KDNode secondSide = (targetVal < nodeVal) ? node.getRight() : node.getLeft();

        kNearestRecursive(firstSide, targetX, targetY, k, depth + 1, pq);

        // Do we need to explore the other side?
        if (pq.size() < k) {
            // We haven't even found K elements yet, we MUST search the other side
            kNearestRecursive(secondSide, targetX, targetY, k, depth + 1, pq);
        } else {
            // Check if the splitting plane is closer than the FURTHEST point in our top K
            double worstDist = pq.peek().getDistance();
            double axisDist = Math.abs(targetVal - nodeVal);

            if (axisDist < worstDist) {
                kNearestRecursive(secondSide, targetX, targetY, k, depth + 1, pq);
            }
        }
    }

    /**
     * Finds all active facilities within a specific radius of the target coordinates.
     *
     * <p><strong>Algorithm (Range Search):</strong></p>
     * <ol>
     *   <li>At each node, check if the facility is within the radius. If so, add it.</li>
     *   <li><strong>Pruning step:</strong> Check if the search circle intersects the left/right sub-spaces.
     *       If {@code target - radius <= splitValue}, search the left child.
     *       If {@code target + radius >= splitValue}, search the right child.</li>
     * </ol>
     *
     * @param x the target X coordinate
     * @param y the target Y coordinate
     * @param radius the maximum Euclidean distance to search
     * @return a list of facilities within the radius, sorted by distance (closest first)
     */
    public List<SearchResult> radiusSearch(double x, double y, double radius) {
        List<SearchResult> results = new ArrayList<>();
        if (root == null || activeSize == 0 || radius < 0) {
            return results;
        }

        double radiusSq = radius * radius; // Use squared radius to avoid Math.sqrt during traversal
        radiusRecursive(root, x, y, radius, radiusSq, 0, results);

        // Sort by distance (closest first)
        Collections.sort(results);
        return results;
    }

    private void radiusRecursive(KDNode node, double targetX, double targetY, double radius, double radiusSq, int depth, List<SearchResult> results) {
        if (node == null) {
            return;
        }

        if (node.getFacility().isActive()) {
            double distSq = DistanceCalculator.squaredDistance(
                    targetX, targetY, node.getFacility().getX(), node.getFacility().getY());
            
            if (distSq <= radiusSq) {
                // Point is inside the circle. Calculate true distance for the SearchResult.
                results.add(new SearchResult(node.getFacility(), Math.sqrt(distSq)));
            }
        }

        int dimension = node.getSplitDimension();
        double targetVal = (dimension == 0) ? targetX : targetY;
        double nodeVal = node.getSplitValue();

        // Does the search circle overlap with the left half-plane?
        if (targetVal - radius <= nodeVal) {
            radiusRecursive(node.getLeft(), targetX, targetY, radius, radiusSq, depth + 1, results);
        }

        // Does the search circle overlap with the right half-plane?
        if (targetVal + radius >= nodeVal) {
            radiusRecursive(node.getRight(), targetX, targetY, radius, radiusSq, depth + 1, results);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // ACCESSORS
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Returns the root node of the tree.
     * Used by the visualization module to draw the tree structure.
     *
     * @return the root node, or null if empty
     */
    public KDNode getRoot() {
        return root;
    }

    /**
     * Returns the number of active facilities.
     *
     * @return the active facility count
     */
    public int getSize() {
        return activeSize;
    }

    /**
     * Returns the total node count including inactive (lazily deleted) nodes.
     *
     * @return total node count
     */
    public int getTotalSize() {
        return totalSize;
    }

    /**
     * Returns true if the tree has no active facilities.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return activeSize == 0;
    }

    /**
     * Removes all nodes from the tree.
     */
    public void clear() {
        root = null;
        activeSize = 0;
        totalSize = 0;
    }

    /**
     * Computes the height of the tree.
     * Useful for debugging and complexity analysis.
     *
     * @return the height (0 for empty tree, 1 for root-only)
     */
    public int getHeight() {
        return computeHeight(root);
    }

    private int computeHeight(KDNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(computeHeight(node.getLeft()), computeHeight(node.getRight()));
    }

    @Override
    public String toString() {
        return String.format("KDTree[active=%d, total=%d, height=%d]",
                activeSize, totalSize, getHeight());
    }
}
