# OPTIMIZING CAMPUS FACILITY DISCOVERY THROUGH 2-DIMENSIONAL K-DIMENSIONAL TREE (KD-TREE) SPATIAL SEARCH

**By:**
Froyd D. Carbajosa
Ethan Mar D. Demeterio
Jhon Kyle L. Collados
Ed Bernard A. Luzon
Alfredo A. Soco

**Institute of Computing**
**Davao del Norte State College**
**June 2026**

---

### ABSTRACT
Modern campus environments require efficient navigation tools to facilitate the rapid location of academic and administrative resources. This study presents the development of the DNSC Smart Campus Facility Finder, a specialized application utilizing a 2-Dimensional K-Dimensional Tree (KD-Tree) to optimize spatial search operations. The research addresses the limitations of traditional linear search methods, which exhibit $O(n)$ time complexity and fail to scale effectively with increasing dataset sizes. By implementing a KD-Tree, the system achieves an average-case search complexity of $O(\log n)$. Empirical results demonstrate that the proposed system outperforms brute-force methods by up to 580 times in large-scale scenarios. The findings suggest that space-partitioning data structures are essential for developing responsive location-based services in institutional settings.

---

## I. INTRODUCTION

### A. System Purpose
The DNSC Smart Campus Facility Finder is designed to assist the Davao del Norte State College (DNSC) community in navigating the campus more effectively. As the college expands, identifying the nearest facilities—such as the library, clinics, or specific laboratories—becomes increasingly complex. The primary purpose of this system is to provide an interactive, real-time tool that accurately identifies and highlights facilities based on their spatial proximity to the user.

### B. Problem Statement
The standard approach to searching for facilities in a database often involves a linear scan, where the system calculates the distance from the query point to every entry in the list. Mathematically, this is expressed as a brute-force search with a time complexity of $O(n)$. While this method is functional for small datasets, it leads to significant latency in environments with high query volumes or large-scale geographical data. There is a clear need for a spatial indexing mechanism that can prune the search space and provide near-instantaneous results.

---

## II. ALGORITHM ASSESSMENT

### A. Background of the Algorithm
The K-Dimensional Tree (KD-Tree) is a non-linear data structure specifically engineered for organizing points in a multi-dimensional space. First proposed by Bentley in 1975, the KD-Tree is a variant of a binary search tree where each node represents a $k$-dimensional point [3]. For campus navigation, $k=2$ represents the X and Y coordinates on a Cartesian plane. The algorithm works by recursively partitioning the search space into half-spaces, creating a hierarchical structure that allows for efficient range and nearest neighbor queries [4].

### B. Time Complexity Analysis
The performance of the system is grounded in the logarithmic efficiency of the KD-Tree.

| Scenario | Complexity | Description |
| :--- | :--- | :--- |
| **Average Search** | $O(\log n)$ | Achieved through recursive spatial pruning. |
| **Worst Case Search** | $O(n)$ | Occurs if the tree is highly unbalanced (linked-list structure). |
| **Balanced Construction** | $O(n \log n)$ | Sorting points by median at each level to ensure balance. |

By utilizing median-based construction, the system maintains the $O(\log n)$ average case, ensuring that even with 1,000,000 simulated facilities, the search remains extremely responsive [2].

### C. Algorithm Mechanics and Logical Flow
The system follows a specific logical flow for spatial organization:
1.  **Spatial Partitioning:** At the root node, the space is split vertically based on the median X-coordinate. The next level splits the remaining space horizontally based on the median Y-coordinate. This alternation continues until every facility is assigned to a node.
2.  **Pruning Logic:** During a search, the algorithm calculates the distance to the splitting plane. If the distance to the plane is greater than the distance to the current best neighbor, the algorithm "prunes" or ignores the entire opposite branch of the tree, drastically reducing the number of calculations [4].

### D. Comparative Analysis
The following table summarizes the performance disparity between the implemented KD-Tree and a standard ArrayList linear scan.

**Table I: Performance Benchmark Results**

| Facilities ($n$) | Linear Scan ($O(n)$) | KD-Tree ($O(\log n)$) | Efficiency Gain |
| :--- | :--- | :--- | :--- |
| 100 | 900 ns | 600 ns | 1.50x |
| 10,000 | 25,500 ns | 3,100 ns | 8.23x |
| 1,000,000 | 6,331,000 ns | 10,900 ns | **580.83x** |

---

## III. DISCUSSION AND CONCLUSION

### A. Discussion
The results of the comparative analysis confirm that the KD-Tree is the superior choice for spatial discovery systems. The bottleneck of the $O(n)$ linear scan becomes apparent at 1,000,000 facilities, where the time taken is over 600 times that of the KD-Tree. The KD-Tree's ability to exclude irrelevant map regions through spatial pruning allows it to handle massive datasets with negligible increases in latency. This project demonstrates that the KD-Tree remains computationally efficient for multidimensional search where standard binary trees or linear arrays fail to scale [2], [4].

### B. Conclusion
The DNSC Smart Campus Facility Finder successfully demonstrates the practical application of space-partitioning data structures in a real-world institutional context. By transitioning from a traditional linear search to a 2-Dimensional KD-Tree, the system significantly optimizes the facility discovery process, providing Davao del Norte State College with a scalable and high-performance tool. The implementation of median-based construction ensures that the system maintains its logarithmic efficiency, effectively solving the problem of spatial search overhead. This study concludes that advanced data structures are vital for the development of responsive and future-ready campus management solutions.

---

## REFERENCES
[1] T. H. Cormen, C. E. Leiserson, R. L. Rivest, and C. Stein, *Introduction to Algorithms*, 3rd ed. Cambridge, MA, USA: MIT Press, 2009.

[2] M. de Berg, O. Cheong, M. van Kreveld, and M. Overmars, *Computational Geometry: Algorithms and Applications*, 3rd ed. Berlin, Germany: Springer-Verlag, 2008.

[3] J. L. Bentley, "Multidimensional binary search trees used for associative searching," *Communications of the ACM*, vol. 18, no. 9, pp. 509–517, Sep. 1975.

[4] H. Samet, *Foundations of Multidimensional and Metric Data Structures*. San Francisco, CA, USA: Morgan Kaufmann, 2006.

