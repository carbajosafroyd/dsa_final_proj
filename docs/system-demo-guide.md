# System Demo Presentation Guide — 20/20 Strategy

> **Project:** DNSC Smart Campus Facility Finder
> **Course:** IT221 Data Structures and Algorithms
> **Target Score:** 20/20 (System Video Presentation)

---

## Understanding the Rubric

The PDF contains **two rubric systems**. Here's what matters for the demo:

### Rubric A (General Instructions PDF — Page 5): System Demo Presentation — 20 pts

| Criteria | Max | What Gets You Full Marks |
|---|---|---|
| **Clarity of Presentation** | 5 pts | Logical flow: Intro → Data Structure → Live Demo → Q&A |
| **Live Demo Quality** | 5 pts | System runs smoothly, all features shown and explained |
| **Technical Mastery (Q&A)** | 5 pts | **ALL members** can explain code, data structure, and logic |
| **Team Participation** | 5 pts | **ALL members** actively participate and contribute |

### Rubric B (DNSC Rubric — Page 7): System Video Presentation — 20 pts

| Score | Description |
|---|---|
| **20** | Exceptional presentation, optimal demonstration of key features, compelling communication |
| **15** | Effective presentation, clear demonstration, good communication |
| **10** | Basic presentation with room for improvement |
| **5** | Ineffective, lack of clarity, poor communication |

> [!IMPORTANT]
> Both rubrics share the same theme: **Every member must speak, every member must explain something technical, and the demo must be smooth.** The video must show your faces and use your own voices. It must not exceed 30 minutes.

---

## Presentation Structure (5 Members, ~25 Minutes)

### MEMBER 1 — "The Opener" (~4 minutes)
**Role:** Project Introduction & Problem Statement

**Script outline:**

1. **Greeting & Title Slide** (30 sec)
   - "Good day! We are Group ___, presenting our IT221 Final Project: **DNSC Smart Campus Facility Finder**."
   - Introduce all 5 members by name.

2. **The Problem** (1.5 min)
   - "Imagine you're a new student at DNSC. You feel sick and need to find the nearest clinic. Or you're hungry and want to know what food places are nearby. Currently, there's no system that answers: *What is the closest facility to where I am right now?*"
   - "A simple list of facilities can't answer spatial questions efficiently. If DNSC had 1,000 facilities, you'd have to calculate the distance to every single one — that's O(n) time."

3. **The Solution** (1.5 min)
   - "We built a spatial search engine powered by a **KD-Tree** — a binary tree that organizes data by spatial coordinates."
   - "Instead of checking every facility, our KD-Tree mathematically prunes entire regions of the campus, achieving **O(log n)** search time."
   - Show the system architecture diagram (slide).

4. **Transition**
   - "Now, let me hand it over to [Member 2] who will explain how the KD-Tree actually works."

---

### MEMBER 2 — "The Algorithm Expert" (~5 minutes)
**Role:** KD-Tree Data Structure Explanation

> [!TIP]
> This is the most critical section for the "Technical Mastery" rubric criterion. The professor is testing if you truly understand the data structure.

**Script outline:**

1. **What is a KD-Tree?** (1 min)
   - "A KD-Tree is a space-partitioning data structure. It's a binary tree where each level alternates between splitting on the X-axis and the Y-axis."
   - Show a diagram of how nodes are split (prepare a slide with a visual).

2. **How Insertion Works** (1 min)
   - "When we insert a facility, we compare its X coordinate at depth 0, its Y coordinate at depth 1, X again at depth 2, and so on."
   - "We also implemented `buildBalanced()` which takes a list, sorts it by the median, and recursively builds a perfectly balanced tree in O(n log n) time."

3. **How Nearest Neighbor Search Works** (2 min) — THE KEY EXPLANATION
   - "This is the algorithm that makes our project special."
   - **Step 1 — Greedy Descent:** "We traverse the tree like a normal insert to find an initial guess."
   - **Step 2 — Backtracking:** "As the recursion unwinds, we check if the current node is closer than our best guess."
   - **Step 3 — Pruning:** "We calculate the perpendicular distance from our target to the splitting plane. If this distance is greater than our current best distance, we **skip the entire other subtree**. This is what makes it O(log n)."
   - Use a whiteboard, slide, or draw on screen.

4. **Other Algorithms** (1 min)
   - K-Nearest Neighbors: "Uses a Max-Heap (PriorityQueue) to maintain the top K results."
   - Radius Search: "Checks if the search circle overlaps with each splitting plane before descending."

5. **Transition**
   - "Now let's see this in action. [Member 3] will demonstrate the system live."

---

### MEMBER 3 — "The Demo Lead" (~7 minutes)
**Role:** Live System Demonstration

> [!IMPORTANT]
> Practice this section at least 3 times before recording. The "Live Demo Quality" criterion requires a smooth, crash-free demonstration.

**Demo Flow:**

1. **Launch the Application** (30 sec)
   - Open terminal, run `.\mvnw.cmd javafx:run`.
   - Show the Dashboard loading. Point out: "The dashboard shows real-time stats — total facilities, tree balance status, and the facility distribution chart."

2. **Show Facility Management — CRUD** (2 min)
   - Navigate to the **Facilities** page.
   - **Create:** Add a new facility (e.g., "New Canteen" at specific coordinates). Explain: "When I click Add, the facility is inserted into the KD-Tree and immediately saved to a JSON file."
   - **Read:** Show the table listing all facilities.
   - **Update:** Edit a facility's name or coordinates. Explain: "Because KD-Trees are organized by coordinates, updating coordinates requires deleting the old node and reinserting the new one. Our service handles this automatically."
   - **Delete:** Remove a facility. Explain: "We use lazy deletion — the node is marked as inactive rather than physically removed, which preserves the tree structure."

3. **Show the Interactive Map — Spatial Search** (3 min)
   - Navigate to the **Map** page.
   - **Nearest Neighbor:** Click a point on the map. Show the dashed line connecting to the nearest facility. "The KD-Tree found the nearest facility by pruning entire subtrees — it didn't check every single dot."
   - **K-Nearest:** Switch to K-Nearest mode, set K=3. Click the map. Show 3 lines drawn to the 3 closest facilities. "This uses a Max-Heap internally to efficiently track the top K results."
   - **Radius Search:** Switch to Radius mode, set radius. Click the map. Show the translucent circle and all facilities within it highlighted. "The tree checks if the search circle overlaps each splitting plane before descending into a subtree."
   - **Right-click to Add:** Right-click the map to add a facility directly on the canvas. "This demonstrates the real-time CRUD — the dot appears instantly because the KD-Tree insertion is O(log n)."

4. **Transition**
   - "Now, the critical question: Is the KD-Tree actually faster? [Member 4] will prove it."

---

### MEMBER 4 — "The Data Scientist" (~5 minutes)
**Role:** Performance Benchmark & Complexity Analysis

> [!TIP]
> This section directly scores points on the "Technical Manuscript and Complexity Analysis" criterion (20 pts) and makes the demo exceptional.

**Script outline:**

1. **Navigate to the Benchmark Page** (30 sec)
   - Click the Benchmark tab in the sidebar.

2. **Run the Benchmark Live** (2 min)
   - Click "Run Benchmark" and let the chart populate in real time.
   - Show the line chart: Linear Search (gray line going up steeply) vs KD-Tree (green line staying nearly flat).
   - Read the numbers: "At 1 million facilities, the linear search took over 6 million nanoseconds. The KD-Tree? Only 10,900 nanoseconds. That's **580 times faster**."

3. **Explain the Complexity** (2 min)
   - "Linear search is O(n) — if you double the data, you double the time."
   - "KD-Tree nearest neighbor is O(log n) on average — if you go from 100,000 to 1,000,000 facilities (10x more data), the search time barely moved. That's logarithmic scaling."
   - "We also performed a JVM warmup of 50 iterations before timing to ensure our measurements are fair and accurate."
   - Show the comparison table (can be on a slide):

   | Dataset | Linear Search | KD-Tree | Speedup |
   |---:|---:|---:|---:|
   | 100 | 900 ns | 600 ns | 1.5x |
   | 1,000 | 7,400 ns | 800 ns | 9.25x |
   | 10,000 | 25,500 ns | 3,100 ns | 8.23x |
   | 100,000 | 223,600 ns | 3,200 ns | 69.88x |
   | 1,000,000 | 6,331,000 ns | 10,900 ns | **580.83x** |

4. **Transition**
   - "To wrap up, [Member 5] will discuss our code quality and what we learned."

---

### MEMBER 5 — "The Closer" (~4 minutes)
**Role:** Code Quality, OOP Design & Conclusion

**Script outline:**

1. **Code Architecture** (2 min)
   - Show the package structure briefly (can be a slide or the IDE):
     - `model/` — Data classes (Facility, SearchResult, BenchmarkResult)
     - `tree/` — KD-Tree implementation (KDNode, KDTree)
     - `util/` — Distance calculations, data generation
     - `persistence/` — JSON save/load with Gson
     - `service/` — FacilityService, SearchService, BenchmarkService
     - `ui/` — JavaFX controllers and FXML views
   - "We followed the **MVC + Service Layer** architecture. The UI never touches the KD-Tree directly — it communicates through service classes. This is the Single Responsibility Principle in action."
   - "Our code is fully documented with Javadoc comments on every public method."

2. **Key OOP Principles Used** (1 min)
   - Encapsulation: "The KDTree class hides its internal node structure."
   - Separation of Concerns: "Each package has one responsibility."
   - Facade Pattern: "FacilityService wraps tree operations AND persistence into one clean API."

3. **Conclusion** (1 min)
   - "We successfully demonstrated that the KD-Tree is a powerful spatial data structure that achieves O(log n) nearest neighbor search."
   - "Our system is fully functional with CRUD operations, three types of spatial search, interactive map visualization, and empirical performance benchmarking."
   - "Thank you. We are now open for questions."

---

## Preparation Checklist

### Before Recording Day

- [ ] **Every member rehearses their part** at least twice
- [ ] **Member 3 does a full dry-run** of the live demo (practice the exact click sequence)
- [ ] Prepare **5–8 slides** (Title, Problem, KD-Tree Diagram, Architecture, Complexity Table, Conclusion)
- [ ] Pre-load the system with **20–30 real DNSC facilities** so the map looks populated
- [ ] **Test the benchmark** to make sure it runs smoothly on the recording machine
- [ ] Ensure all faces are visible and voices are clear (as per the PDF requirement)

### Common Q&A Questions to Prepare For

Every member should be able to answer at least 3 of these:

| Question | Who Should Lead the Answer |
|---|---|
| "Why did you choose KD-Tree over other data structures?" | Member 2 |
| "What is the time complexity of insertion?" | Member 2 |
| "How does nearest neighbor search work?" | Member 2 |
| "What happens when you delete a facility?" | Member 3 |
| "Why not just use a HashMap or ArrayList?" | Member 4 |
| "What is the worst-case time complexity of KD-Tree?" | Member 2 or 4 |
| "How do you persist data?" | Member 3 or 5 |
| "What design patterns did you use?" | Member 5 |
| "What would you improve if you had more time?" | Member 5 |
| "How does radius search differ from nearest neighbor?" | Member 2 or 3 |

> [!CAUTION]
> The rubric explicitly states: **"Members who cannot explain their part or are absent will receive a significantly lower individual grade."** Make sure every single member can explain at least the basic concept of what a KD-Tree is and how nearest neighbor search works — even the documentation members.

---

## Video Recording Tips

- **Max length:** 30 minutes (aim for 22–25 minutes to leave buffer)
- **Must show faces** and use your own voices (per the DNSC instructions)
- Use screen-sharing + webcam overlay (OBS Studio is free and works great)
- Upload to YouTube or Facebook as required
- Record in one take if possible — it looks more professional and "live"
