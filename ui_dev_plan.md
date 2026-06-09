# UI Developer Plan — DNSC Smart Campus Facility Finder
> **Your Role:** Co-Dev / "The Interface" — Dev 2  
> **Stack:** JavaFX 21 · FXML · CSS  
> **Your Branch Prefix:** `dev-2/phase-7x-...`

---

## 🗺️ Where You Are Right Now

The project is at **Phase 2 Complete**. When you run `mvn javafx:run`, you'll see a blank dark window with just a title label. That's your starting point. Your job is to transform that blank shell into the full interactive application.

**What already exists in your territory:**
- [`MainApp.java`](file:///C:/Users/NITRO%20V/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/ui/MainApp.java) — The JavaFX entry point. You will heavily modify this.
- [`application.css`](file:///C:/Users/NITRO%20V/NetBeansProjects/dsa_final_proj/src/main/resources/styles/application.css) — Bare-bones CSS with just a dark background. You expand this massively.

**What you need to create (your full file list):**
```
ui/
├── MainApp.java              ← MODIFY (already exists)
└── controller/               ← CREATE this folder + all files
    ├── DashboardController.java
    ├── FacilityFormController.java
    ├── SearchController.java
    ├── MapController.java
    └── BenchmarkController.java

resources/
├── styles/
│   └── application.css       ← MODIFY (already exists)
├── views/                    ← CREATE this folder + all files
│   ├── dashboard.fxml
│   ├── facility_form.fxml
│   ├── search.fxml
│   ├── map.fxml
│   └── benchmark.fxml
└── images/                   ← CREATE this folder (add icons etc.)
```

---

## 🔑 The Golden Rule: Stub-First Development

Dev 1 (your co-dev) is still building the engine (KDTree, services). **You cannot wait for them.** Instead, you build a stub class that fakes the data, so your UI works immediately.

```java
// Create this file: ui/StubFacilityService.java (temporary, deleted on Day 7)
public class StubFacilityService {
    public List<Facility> getAllFacilities() {
        return List.of(
            new Facility("Library", 200, 150, FacilityType.ACADEMIC, "Main Library"),
            new Facility("Clinic", 350, 280, FacilityType.MEDICAL, "Campus Clinic"),
            new Facility("Gym", 500, 100, FacilityType.SPORTS, "Athletics Center"),
            new Facility("Canteen", 300, 400, FacilityType.FOOD_SERVICE, "Main Cafeteria"),
            new Facility("Admin", 150, 300, FacilityType.ADMINISTRATIVE, "Admin Building")
        );
    }
}
```

> [!IMPORTANT]
> On **Day 7 (Integration Day)**, you simply replace every `new StubFacilityService()` with the real `new FacilityService()`. If you coded to the agreed method signatures, this takes under 30 minutes.

---

## 📅 Phase-by-Phase Plan

---

### Phase 7a — Navigation Shell
**Branch:** `dev-2/phase-7a-navigation`  
**Days:** 1–3  
**Goal:** A working app window with a sidebar and the ability to switch between screens.

#### What to Build
1. **Refactor `MainApp.java`** — Replace the placeholder `VBox` with a real `BorderPane` layout:
   - `LEFT` = sidebar navigation panel
   - `CENTER` = dynamic content area (swaps screens)
   - `BOTTOM` = status bar (optional)

2. **Create `DashboardController.java`** — The first screen shown on launch. It should display:
   - App name / logo area
   - Quick stats cards: *Total Facilities*, *Facility Types*, *System Status*
   - Navigation buttons to other screens

3. **Create `dashboard.fxml`** — The layout for the Dashboard screen.

4. **Build the Sidebar Navigation** — Either in `MainApp.java` or its own controller. The sidebar should have clickable buttons that swap the center content:
   - 🏠 Dashboard
   - 🗺️ Map View
   - 🏛️ Facility Manager
   - 🔍 Search
   - ⚡ Benchmark

#### Key JavaFX Concepts You'll Use
| Concept | Used For |
|---------|----------|
| `BorderPane` | Root layout (left sidebar + center content) |
| `VBox` / `HBox` | Sidebar buttons layout |
| `FXMLLoader` | Loading `.fxml` files for each screen |
| `Node.setVisible()` | Hiding/showing screens |
| CSS classes | Styling sidebar, active button highlight |

#### Acceptance Criteria
- [ ] App launches and shows the Dashboard by default
- [ ] Clicking sidebar buttons switches the center content area
- [ ] Active sidebar button has a different visual style (highlighted)
- [ ] CSS dark theme is consistent across the shell

---

### Phase 7b — Facility Manager Screen
**Branch:** `dev-2/phase-7b-facility`  
**Days:** 3–5  
**Goal:** A complete CRUD interface for managing facilities using stub data.

#### What to Build
1. **Create `facility_form.fxml`** — Layout with:
   - `TableView<Facility>` listing all facilities
   - Column headers: Name | Type | X | Y | Description | Status
   - Toolbar with: **Add**, **Edit**, **Delete** buttons + Type filter `ComboBox`

2. **Create `FacilityFormController.java`** — Wires the FXML to logic:
   - Populates the `TableView` from `StubFacilityService.getAllFacilities()`
   - **Add**: opens a dialog/inline form to create a new `Facility`
   - **Edit**: pre-fills the form with the selected row's data
   - **Delete**: removes the selected row (with confirmation dialog)
   - **Filter**: the `ComboBox` filters the table by `FacilityType`

#### Key JavaFX Concepts You'll Use
| Concept | Used For |
|---------|----------|
| `TableView<T>` + `TableColumn<T,S>` | Displaying facilities in rows |
| `ObservableList<Facility>` | Auto-updating the table |
| `Dialog<ButtonType>` | Add/Edit/Delete confirmation popups |
| `ComboBox<FacilityType>` | Dropdown for type filtering |
| `TextField` validation | Preventing invalid coordinate input |

#### Acceptance Criteria
- [ ] Table shows all stub facilities on load
- [ ] Add button opens a form; submitting adds a row to the table
- [ ] Edit button pre-fills the form with selected facility data
- [ ] Delete button removes the row (with a confirmation prompt)
- [ ] Type filter works correctly (shows only matching rows)

---

### Phase 7c — Map View Screen
**Branch:** `dev-2/phase-7c-map`  
**Days:** 5–7  
**Goal:** An interactive 2D canvas that shows facilities as colored dots and responds to mouse clicks.

#### What to Build
1. **Create `map.fxml`** — Layout with:
   - `Canvas` (the main map drawing area — make it large, e.g., 800×600)
   - Right panel: search mode toggle (`RadioButton` group: Nearest / K-Nearest / Radius)
   - K value `Spinner` (for K-nearest mode)
   - Radius `Slider` or `TextField` (for radius mode)
   - Results label area
   - Toggle checkboxes: Show Partition Lines | Show Radius Circle

2. **Create `MapController.java`** — The most complex controller:
   - `drawFacilities()`: iterate stub list, draw a colored dot per facility type
   - `handleCanvasClick(MouseEvent)`: capture `(x, y)` click position, run the selected search mode, highlight results
   - `drawSearchResult()`: draw a line from click point to nearest facility, or highlight K results in a different color
   - `drawRadiusCircle()`: draw a circle centered on the click point
   - `drawPartitionLines()` *(optional for now, connect to real tree later)*: draw mock horizontal/vertical lines

#### Color Coding for Facility Types
| Type | Color |
|------|-------|
| ACADEMIC | `#4a9eff` (blue) |
| MEDICAL | `#ff4a6a` (red) |
| SPORTS | `#4affa0` (green) |
| ADMINISTRATIVE | `#ffd84a` (yellow) |
| FOOD_SERVICE | `#ff974a` (orange) |
| UTILITY | `#b04aff` (purple) |

#### Key JavaFX Concepts You'll Use
| Concept | Used For |
|---------|----------|
| `Canvas` + `GraphicsContext` | Drawing dots, lines, circles |
| `Canvas.setOnMouseClicked()` | Capturing click coordinates |
| `gc.fillOval()` / `gc.strokeOval()` | Drawing facility dots and radius circle |
| `gc.strokeLine()` | Drawing line from query to nearest facility |
| `RadioButton` + `ToggleGroup` | Search mode selection |
| `Spinner<Integer>` | K value input |

#### Acceptance Criteria
- [ ] All stub facilities appear as colored dots on the canvas
- [ ] Clicking the canvas captures the `(x, y)` coordinates and shows a result label
- [ ] Nearest mode: draws a line from the click point to the closest dot
- [ ] Radius mode: draws a circle and highlights all dots inside it
- [ ] K-nearest mode: highlights the K closest dots in a different color

---

### Phase 7d — Search Panel Screen
**Branch:** `dev-2/phase-7d-search`  
**Days:** 7–8  
**Goal:** A dedicated text-input search panel that complements the map's click-to-search.

#### What to Build
1. **Create `search.fxml`** — Layout with:
   - `TextField` for X coordinate
   - `TextField` for Y coordinate
   - `ComboBox` for search type: Nearest / K-Nearest / Radius
   - `Spinner` for K value (shown only when K-Nearest is selected)
   - `TextField` for radius (shown only when Radius is selected)
   - **Search** button
   - Results `ListView<SearchResult>` or formatted text area

2. **Create `SearchController.java`** — Wires the form to logic:
   - On search button click, validate inputs, call stub search method
   - Display results: facility name, type, distance
   - Show/hide K and Radius inputs based on selected search type

#### Stub Search Method (Temporary)
```java
// Add to your StubFacilityService or create a StubSearchService
public SearchResult findNearest(double x, double y) {
    // Return hardcoded nearest for now
    return new SearchResult(getAllFacilities().get(0), 42.5);
}
public List<SearchResult> findKNearest(double x, double y, int k) {
    return getAllFacilities().stream()
        .limit(k)
        .map(f -> new SearchResult(f, Math.random() * 200))
        .collect(Collectors.toList());
}
```

#### Acceptance Criteria
- [ ] Input validation: X/Y must be numbers, K must be ≥ 1, Radius must be > 0
- [ ] Correct input fields show/hide based on search type selection
- [ ] Results are displayed with facility name, type, and distance
- [ ] Empty result case is handled gracefully (e.g., "No facilities found")

---

### Phase 7e — Benchmark Screen
**Branch:** `dev-2/phase-7e-benchmark`  
**Days:** 8–9  
**Goal:** A screen that shows timing data comparing KD-Tree vs. ArrayList performance.

#### What to Build
1. **Create `benchmark.fxml`** — Layout with:
   - Dataset size `Spinner` or preset buttons: 100 / 500 / 1000 / 5000
   - Query X/Y `TextField`
   - **Run Benchmark** button
   - Results `TableView` with columns: Dataset Size | KD-Tree (ms) | ArrayList (ms) | Speedup
   - A simple bar chart or progress indicators (optional but impressive)

2. **Create `BenchmarkController.java`**:
   - On button click, call `BenchmarkService.compareNearestNeighbor(size, x, y)` *(stub for now)*
   - Populate the results table
   - Display a summary: "KD-Tree was Xx faster"

#### Stub for Now
```java
public class StubBenchmarkService {
    public BenchmarkResult compareNearestNeighbor(int datasetSize, double x, double y) {
        // Return fake timing data for UI testing
        long kdTreeMs = (long)(Math.log(datasetSize) * 2);
        long arrayListMs = datasetSize / 10;
        return new BenchmarkResult(kdTreeMs, arrayListMs);
    }
}
```

#### Acceptance Criteria
- [ ] Clicking "Run Benchmark" populates the results table with mock data
- [ ] Results table clearly shows KD-Tree time, ArrayList time, and speedup ratio
- [ ] Multiple runs accumulate in the table (don't clear on each run)

---

### Phase 7f — CSS Polish & Integration
**Branch:** `dev-2/phase-7f-polish`  
**Days:** 9–10 (overlaps with Integration Day)  
**Goal:** Full visual polish and swap stubs → real services.

#### CSS Work
Expand [`application.css`](file:///C:/Users/NITRO V/NetBeansProjects/dsa_final_proj/src/main/resources/styles/application.css) with:
- Sidebar button styles (normal + active/hover states)
- Table row hover and selection styles
- Card-style stat boxes for the Dashboard
- Form input styling (dark inputs with blue focus border)
- Dialog/popup styling
- Status bar styling
- Color variables for all facility types

#### Integration Day Checklist (Day 7)
When Dev 1 signals their phase is complete:
- [ ] Replace `StubFacilityService` with `FacilityService`
- [ ] Replace `StubSearchService` with `SearchService`
- [ ] Replace `StubBenchmarkService` with `BenchmarkService`
- [ ] Test every feature end-to-end with real data
- [ ] Fix any method signature mismatches (communicate immediately with Dev 1)

---

## 🤝 Integration Contract — The Methods You Call

These are the **agreed method signatures** from Dev 1. Do NOT call KDTree directly. Always go through services:

```java
// FacilityService — for CRUD (FacilityFormController)
facilityService.addFacility(Facility facility);
facilityService.removeFacility(Facility facility);
facilityService.updateFacility(Facility oldFacility, Facility newFacility);
facilityService.getAllFacilities();  // returns List<Facility>
facilityService.getSize();           // returns int

// SearchService — for search (MapController + SearchController)
searchService.findNearest(double x, double y);              // returns SearchResult
searchService.findKNearest(double x, double y, int k);     // returns List<SearchResult>
searchService.findWithinRadius(double x, double y, double r); // returns List<SearchResult>

// BenchmarkService — for benchmark (BenchmarkController)
benchmarkService.compareNearestNeighbor(int datasetSize, double queryX, double queryY);
```

> [!WARNING]
> If Dev 1 changes any of these signatures, they MUST notify you immediately. Likewise, if you discover a method needs to return additional data, communicate before assuming.

---

## 🗃️ Git Workflow

```
# Starting a new phase:
git checkout main
git pull origin main
git checkout -b dev-2/phase-7a-navigation

# Committing your work:
git add src/main/java/com/mycompany/dsa_final_proj/ui/
git add src/main/resources/
git commit -m "7a: Add navigation shell with sidebar and screen switching"

# When a phase is done and tested, merge to main:
git checkout main
git merge dev-2/phase-7a-navigation
git push origin main
# Then notify Dev 1: "Phase 7a merged, pull main"
```

---

## 📚 Learning Resources (If You're New to JavaFX)

| Topic | What to Learn |
|-------|--------------|
| **FXML Basics** | How to define UI in XML and link it to a controller via `@FXML` annotations |
| **FXMLLoader** | How `MainApp.java` loads `.fxml` files and gets the controller |
| **ObservableList** | How `TableView` auto-updates when you add/remove items |
| **Canvas drawing** | `GraphicsContext.fillOval()`, `strokeLine()`, `clearRect()` |
| **Event Handling** | `setOnAction()` for buttons, `setOnMouseClicked()` for canvas |
| **CSS in JavaFX** | Properties like `-fx-background-color`, `-fx-font-size`, `-fx-text-fill` |

> [!TIP]
> Start with the [JavaFX official documentation](https://openjfx.io/) and focus on the FXML tutorial first. Understanding how `FXMLLoader` connects `.fxml` files to controller classes is the #1 thing to learn before writing any UI code.

---

## ✅ Master Checklist

| Phase | Task | Status |
|-------|------|--------|
| 7a | Navigation shell with sidebar | ⬜ Not started |
| 7a | Screen switching (center content swaps) | ⬜ Not started |
| 7a | Dashboard screen with stats cards | ⬜ Not started |
| 7b | Facility table with all columns | ⬜ Not started |
| 7b | Add facility form/dialog | ⬜ Not started |
| 7b | Edit facility form/dialog | ⬜ Not started |
| 7b | Delete with confirmation | ⬜ Not started |
| 7b | Filter by type | ⬜ Not started |
| 7c | Canvas with colored facility dots | ⬜ Not started |
| 7c | Click-to-search on canvas | ⬜ Not started |
| 7c | Nearest neighbor line drawn | ⬜ Not started |
| 7c | Radius circle drawn | ⬜ Not started |
| 7c | K-nearest highlights | ⬜ Not started |
| 7d | Search form with all inputs | ⬜ Not started |
| 7d | Input validation | ⬜ Not started |
| 7d | Results display | ⬜ Not started |
| 7e | Benchmark results table | ⬜ Not started |
| 7e | Run benchmark button | ⬜ Not started |
| 7f | Full CSS polish | ⬜ Not started |
| 7f | Integration: swap stubs → real services | ⬜ Not started |
| 7f | End-to-end testing with real data | ⬜ Not started |

---

## 🚀 Your Very First Step

1. **Run the project** to verify it compiles:
   ```powershell
   mvn javafx:run
   ```
   You should see a dark window with "DNSC Smart Campus Facility Finder" text.

2. **Create your first branch:**
   ```powershell
   git checkout -b dev-2/phase-7a-navigation
   ```

3. **Create the `controller/` directory** under `src/main/java/com/mycompany/dsa_final_proj/ui/`

4. **Create the `views/` directory** under `src/main/resources/`

5. **Start with `DashboardController.java` + `dashboard.fxml`** — the simplest screen.

6. **Modify `MainApp.java`** to load `dashboard.fxml` instead of the placeholder `VBox`.

That's it. One step at a time. 🎯
