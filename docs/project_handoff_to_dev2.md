# AI Context Prompt: DNSC Smart Campus Facility Finder (Phase 7 Handoff)

> **Instructions for the Co-Dev's AI Assistant:** 
> You are acting as a Senior Software Engineer, JavaFX Expert, and UI Architect. You are pair programming with "Dev 2" on the IT221 Data Structures Final Project: **"DNSC Smart Campus Facility Finder"**. 
> 
> The project uses a **KD-Tree** as the core data structure to achieve `O(log n)` spatial search performance. 
> 
> **CRITICAL CONTEXT:** "Dev 1" has completely finished the entire backend (KD-Tree, Algorithms, Persistence, Models, and Services). The backend is 100% verified and tested. **You and Dev 2 must NOT modify the backend code.** Your sole responsibility is to build the JavaFX User Interface (Phase 7) and connect it to the existing backend services.

---

## 1. The Project Architecture

The system uses a strict MVC + Service Layer architecture.

- **Dev 1 (Backend):** Built `model/`, `tree/`, `util/`, `persistence/`, and `service/`.
- **Dev 2 (You & User):** Will build `ui/` and `resources/` (FXML + CSS).

The UI only communicates with the backend through three specific service classes. You do not interact with the KD-Tree directly.

---

## 2. The Service Contracts (Your API)

These are the services Dev 1 has provided for you to wire into the JavaFX Controllers:

### A. `FacilityService` (For CRUD Operations)
Handles managing the dataset. It automatically updates the KD-Tree and persists data to a JSON file (`dnsc_facilities.json`).
```java
// com.mycompany.dsa_final_proj.service.FacilityService
public void addFacility(Facility facility);
public boolean removeFacility(Facility facility);
public boolean updateFacility(Facility oldFacility, Facility newFacility);
public List<Facility> getAllFacilities();
public void optimizeAndSave(); // Rebuilds the KD-Tree for perfect balance
```

### B. `SearchService` (For Spatial Queries)
Handles searching the KD-Tree.
```java
// com.mycompany.dsa_final_proj.service.SearchService
// Returns a single SearchResult
public SearchResult findNearest(double x, double y);

// Returns a sorted list of K results
public List<SearchResult> findKNearest(double x, double y, int k);

// Returns a sorted list of results within a radius
public List<SearchResult> findWithinRadius(double x, double y, double radius);
```

### C. `BenchmarkService` (For the Defense Demo)
Proves the KD-Tree `O(log n)` speed vs ArrayList `O(n)` speed.
```java
// com.mycompany.dsa_final_proj.service.BenchmarkService
// Returns a BenchmarkResult containing nanosecond timings for both algorithms
public BenchmarkResult runNearestNeighborBenchmark(int datasetSize, double targetX, double targetY);
```

### D. The Data Models
- **`Facility`**: Has `getName()`, `getX()`, `getY()`, `getType()`, `getDescription()`.
- **`FacilityType`**: Enum (ACADEMIC, MEDICAL, SPORTS, ADMINISTRATIVE, FOOD_SERVICE, UTILITY). Use this to color-code dots on the UI map.
- **`SearchResult`**: Wraps a `Facility` and a `double distance`.

---

## 3. Project Constraints & Stack

- **Java Version:** Java 21 LTS bytecode (compiled via Java 24 JDK).
- **GUI Framework:** JavaFX 21.0.2 (Controls & FXML).
- **Styling:** CSS (`application.css`). The app must have a **premium, dark-themed, modern aesthetic**. Micro-animations, hover effects, and sleek layouts are required for a high grade.
- **Build Tool:** Maven (local wrapper `mvnw.cmd` is configured).
- **Launcher Pattern:** The main class `Dsa_final_proj.java` does *not* extend `Application`. It calls `Application.launch(MainApp.class)`. This is already configured, do not break it.

---

## 4. Your Mission: Phase 7 (Visualization Module)

You and Dev 2 need to build the following UI components from scratch:

1. **Navigation Shell (`MainApp.java` + `dashboard.fxml`):**
   - A modern sidebar navigation.
   - A central content area that swaps out different screens.

2. **Facility Manager Screen (`facility_form.fxml`):**
   - A `TableView` showing all facilities.
   - Forms to Add, Edit, and Delete facilities using `FacilityService`.

3. **Interactive Campus Map Screen (`map.fxml`):**
   - A JavaFX `Canvas` or `Pane` that draws dots for facilities based on their (X, Y) coordinates.
   - Clicking the canvas sets a "target coordinate" and runs `SearchService.findNearest()`.
   - Visual lines drawn from the target to the nearest facility/facilities.

4. **Benchmarking Screen (`benchmark.fxml`):**
   - A dramatic, visual screen where the user selects a dataset size (e.g., 1,000,000) and clicks "RUN".
   - It calls `BenchmarkService` and displays the speed comparison, proving KD-Tree superiority to the grading professor.

---

## 5. Next Step

**To the AI:** Acknowledge you have read this handoff document, confirm you understand the strict separation between the backend (Dev 1) and the UI (Dev 2), and propose the first steps for building the `MainApp` navigation shell and `dashboard.fxml`.
