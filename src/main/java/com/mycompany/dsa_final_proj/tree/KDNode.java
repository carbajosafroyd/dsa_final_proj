/*
 * DNSC Smart Campus Facility Finder
 * IT221 Data Structures Final Project
 */

package com.mycompany.dsa_final_proj.tree;

import com.mycompany.dsa_final_proj.model.Facility;

/**
 * A single node in the KD-Tree.
 *
 * <p>Each node holds one {@link Facility} and has two children (left, right).
 * The {@code depth} field determines which coordinate dimension this node
 * splits on:</p>
 *
 * <ul>
 *   <li>Even depth (0, 2, 4, ...): splits on <strong>X</strong> coordinate</li>
 *   <li>Odd depth  (1, 3, 5, ...): splits on <strong>Y</strong> coordinate</li>
 * </ul>
 *
 * <p><strong>Splitting Rule:</strong> For a node at depth {@code d}, all facilities
 * in the <em>left</em> subtree have {@code coordinate[d % 2] < this.coordinate[d % 2]},
 * and all facilities in the <em>right</em> subtree have
 * {@code coordinate[d % 2] >= this.coordinate[d % 2]}.</p>
 *
 * @author DNSC IT221 Team
 */
public class KDNode {

    /** Number of dimensions (2 for a 2D campus map). */
    static final int DIMENSIONS = 2;

    private final Facility facility;
    private KDNode left;
    private KDNode right;
    private final int depth;

    /**
     * Constructs a new KD-Tree node.
     *
     * @param facility the facility stored at this node
     * @param depth    the depth of this node in the tree (root = 0)
     */
    public KDNode(Facility facility, int depth) {
        this.facility = facility;
        this.depth = depth;
        this.left = null;
        this.right = null;
    }


    public Facility getFacility() {
        return facility;
    }

    public KDNode getLeft() {
        return left;
    }

    public KDNode getRight() {
        return right;
    }

    public int getDepth() {
        return depth;
    }

    /**
     * Returns which dimension (0 = X, 1 = Y) this node splits on.
     *
     * <p>This is the core of the KD-Tree's alternating dimension logic.
     * By using {@code depth % DIMENSIONS}, we cycle through X and Y
     * at each level of the tree.</p>
     *
     * @return 0 for X-axis split, 1 for Y-axis split
     */
    public int getSplitDimension() {
        return depth % DIMENSIONS;
    }

    /**
     * Returns the coordinate value that this node splits on.
     *
     * <p>For example, if this node is at depth 0 (splits on X) and the
     * facility is at (150, 200), this returns 150.</p>
     *
     * @return the split coordinate value
     */
    public double getSplitValue() {
        return facility.getCoordinate(getSplitDimension());
    }

    /**
     * Returns true if this node has no children.
     *
     * @return true if both left and right are null
     */
    public boolean isLeaf() {
        return left == null && right == null;
    }


    public void setLeft(KDNode left) {
        this.left = left;
    }

    public void setRight(KDNode right) {
        this.right = right;
    }

    @Override
    public String toString() {
        String dim = (getSplitDimension() == 0) ? "X" : "Y";
        return String.format("KDNode[%s, split=%s, depth=%d]",
                facility.getName(), dim, depth);
    }
}
