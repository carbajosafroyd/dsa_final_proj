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
Efficient campus navigation depends on the ability to quickly locate academic and administrative facilities. This paper discusses the development of the DNSC Smart Campus Facility Finder, an application that utilizes a 2-Dimensional K-Dimensional Tree (KD-Tree) to optimize spatial search operations. We addressed the scaling issues found in traditional linear search methods, which often lead to high latency as datasets grow. By implementing a KD-Tree, the system achieved an average-case search complexity of $O(\log n)$. Our tests showed that this implementation is significantly faster than brute-force methods, particularly when handling thousands of simulated data points. This study highlights the practical importance of using space-partitioning algorithms for local institutional applications.

---

## I. SYSTEM OVERVIEW

### a. System Purpose and Problem Statement
The primary goal of the DNSC Smart Campus Facility Finder is to provide the Davao del Norte State College (DNSC) community with a reliable tool for identifying facility locations in real-time. As campuses expand and become more densely packed with buildings and offices, simple list-based search becomes inefficient. Users need a way to find the closest facility to their current coordinates without experiencing significant software lag.

The technical problem we identified is the inefficiency of the "Nearest Neighbor Search" (NNS) when using standard array-based data structures. In many basic applications, locations are stored in a simple list, requiring the system to calculate the Euclidean distance for every single entry during a search query. This brute-force method results in a time complexity of $O(n)$, which means the search time increases linearly with the number of facilities [1]. To solve this, our team developed a system that organizes facility coordinates into a KD-Tree, allowing the software to prune unnecessary calculations and provide instantaneous feedback even as more facilities are added to the campus map.

---

## II. ALGORITHM ASSESSMENT

### a. Background of the Algorithm
The K-Dimensional Tree (KD-Tree) is a specialized binary search tree used for organizing points in a $k$-dimensional space. It was first developed by Jon Bentley in 1975 to handle multi-dimensional search keys [3]. In our project, we focused on $k=2$, where the dimensions represent the X and Y coordinates on the campus map. Unlike a standard BST that only splits data once, the KD-Tree partitions space into recursive regions, making it highly effective for spatial indexing and proximity-based queries [4].

### b. Time Complexity
The core benefit of the KD-Tree is its ability to reduce search time from linear to logarithmic.

| Scenario | Complexity | Logic |
| :--- | :--- | :--- |
| **Average Search** | $O(\log n)$ | The algorithm eliminates half of the search space at each step. |
| **Worst Case Search** | $O(n)$ | Occurs if the points are inserted in a way that creates a deep, unbalanced tree. |
| **Balanced Construction** | $O(n \log n)$ | Required to ensure the tree remains efficient for searching. |

To avoid the worst-case scenario during our demonstration, we implemented a median-based construction method. This ensures the tree is always balanced from the start, keeping our search operations within the $O(\log n)$ range [2].

### c. Algorithm Mechanics and Logical Flow
The KD-Tree functions by alternating the splitting dimension at each level of the tree. In our 2D implementation, the root node splits the dataset based on the X-coordinate. Its children then split the remaining data based on the Y-coordinate, and so on. This creates a "checkerboard" of regions that the system can navigate.

When a user clicks on the map to find the nearest facility, the algorithm performs the following:
1.  **Binary Search:** It traverses down the tree to find the "leaf" node that would theoretically contain the click point.
2.  **Backtracking:** It works its way back up the tree, checking if any other nodes are closer than the current best.
3.  **Spatial Pruning:** If the distance to a splitting line is greater than the distance to the current best neighbor, the algorithm completely skips the other side of that line. This is the "secret" to its speed, as it allows the system to ignore thousands of facilities that are obviously too far away [4].

### d. Comparative Analysis
We tested our KD-Tree implementation against a standard ArrayList (Linear Search) to verify its efficiency. We used a randomized dataset with coordinates ranging from 0 to 1,000 to simulate a large campus environment.

**Table I: Benchmarking KD-Tree vs. Linear Search**

| Dataset Size ($n$) | Linear Search ($O(n)$) | KD-Tree Search ($O(\log n)$) | Efficiency Gain |
| :--- | :--- | :--- | :--- |
| 100 | 900 ns | 600 ns | 1.5x faster |
| 10,000 | 25,500 ns | 3,100 ns | 8.2x faster |
| 1,000,000 | 6,331,000 ns | 10,900 ns | **580.8x faster** |

The data shows that while a linear search is acceptable for 100 facilities, it becomes significantly slower as the count reaches one million. The KD-Tree, however, remains consistently fast, proving that the $O(\log n)$ complexity is highly effective for large-scale spatial data.

---

## III. DISCUSSION

Our implementation of the 2D KD-Tree for the DNSC Smart Campus Facility Finder successfully addressed the performance bottlenecks of traditional search methods. By utilizing spatial partitioning, we were able to provide a search experience that is both scalable and responsive. The most significant finding from our testing was the impact of "pruning"—the ability to ignore entire sections of the map during a search.

While the KD-Tree is more complex to implement than a simple list, the performance gain of over 500x in large-scale scenarios justifies the effort. We found that using median-based construction was essential to prevent the tree from becoming unbalanced, which would have degraded the performance back to $O(n)$. Overall, the system demonstrates that choosing the right data structure is critical when building tools that rely on real-time spatial calculations. This study confirms that the KD-Tree is a vital component for any modern campus navigation system [2], [4].

## IV. CONCLUSION

In conclusion, the DNSC Smart Campus Facility Finder successfully achieved its goal of optimizing facility discovery through advanced spatial indexing. By transitioning from a linear array-based search to a 2-Dimensional KD-Tree, we reduced the search complexity to a logarithmic scale, ensuring the system remains high-performing even as the campus dataset grows. The empirical data collected during our benchmarking phase confirmed that our implementation is significantly more efficient than brute-force methods, especially in high-density scenarios. This project highlights the practical necessity of space-partitioning data structures in solving real-world geographical search problems and provides Davao del Norte State College with a robust framework for future campus navigation developments.

---

## REFERENCES
[1] T. H. Cormen, C. E. Leiserson, R. L. Rivest, and C. Stein, *Introduction to Algorithms*, 3rd ed. Cambridge, MA, USA: MIT Press, 2009.

[2] M. de Berg, O. Cheong, M. van Kreveld, and M. Overmars, *Computational Geometry: Algorithms and Applications*, 3rd ed. Berlin, Germany: Springer-Verlag, 2008.

[3] J. L. Bentley, "Multidimensional binary search trees used for associative searching," *Communications of the ACM*, vol. 18, no. 9, pp. 509–517, Sep. 1975.

[4] H. Samet, *Foundations of Multidimensional and Metric Data Structures*. San Francisco, CA, USA: Morgan Kaufmann, 2006.


