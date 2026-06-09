# How to Run & Start — Step-by-Step Guide
> **For:** Dev 2 (UI Co-Dev) | **Project:** DNSC Smart Campus Facility Finder

---

## PART 1 — Running the Project

### Step 1: Open a Terminal in the Project Folder

In **Windows**, open **PowerShell** (or use the NetBeans terminal):

```powershell
# Navigate to your project folder
cd "C:\Users\NITRO V\NetBeansProjects\dsa_final_proj"
```

> [!TIP]
> In NetBeans, you can right-click the project → **Open in Terminal** to skip this step.

---

### Step 2: Run the App

You do NOT have `mvn` installed globally — that's fine. The project ships with a **Maven Wrapper** (`mvnw.cmd`). Always use this instead of `mvn`:

```powershell
.\mvnw.cmd javafx:run
```

**The very first time you run this**, Maven will download all dependencies (JavaFX, Gson, plugins). This can take **2–5 minutes** depending on your internet speed. After that, every subsequent run is fast (under 10 seconds).

#### What you should see:
```
[INFO] --- javafx:0.0.8:run ---
[INFO] BUILD SUCCESS
```
...and a dark window pops up with the title **"DNSC Smart Campus Facility Finder"**.

---

### Step 3: Close the App

Just close the window normally, or press `Ctrl+C` in the terminal to kill the process.

---

### Troubleshooting Common Errors

| Error Message | What It Means | Fix |
|---------------|--------------|-----|
| `'mvn' is not recognized` | You're using `mvn` instead of `.\mvnw.cmd` | Use `.\mvnw.cmd javafx:run` |
| `BUILD FAILURE — Could not find class` | A Java file has a compile error | Read the error line, fix the file |
| `NullPointerException on CSS path` | A resource file path is wrong | Check the path in `MainApp.java` line 77 |
| Window opens then immediately closes | Runtime exception in `start()` | Check the terminal for the stack trace |
| `Error: JavaFX runtime components are missing` | Module path issue | Always use `.\mvnw.cmd javafx:run`, never `java -jar` |

---

## PART 2 — Setting Up Git

> [!IMPORTANT]
> Do this BEFORE you write any code. Never commit directly to `main`.

### Step 4: Verify Git Status

```powershell
git status
git log --oneline -5
```

This shows your current branch and recent commits.

---

### Step 5: Create Your First Branch

```powershell
# Always start from main
git checkout main
git pull origin main

# Create your Phase 7a branch
git checkout -b dev-2/phase-7a-navigation
```

You should see: `Switched to a new branch 'dev-2/phase-7a-navigation'`

---

## PART 3 — Starting Phase 7a (Navigation Shell)

This is where you actually begin coding. Follow these steps in order.

---

### Step 6: Create the Folder Structure

In PowerShell:
```powershell
# Create the controller package folder
mkdir "src\main\java\com\mycompany\dsa_final_proj\ui\controller"

# Create the views folder in resources
mkdir "src\main\resources\views"

# Create the images folder
mkdir "src\main\resources\images"
```

Your folder structure should now look like:
```
ui/
├── MainApp.java          ← already exists
└── controller/           ← you just created this

resources/
├── styles/
│   └── application.css   ← already exists
├── views/                ← you just created this
└── images/               ← you just created this
```

---

### Step 7: Create the Stub Service

Before writing any UI, create the stub so your UI has fake data to display.

Create this file: `src/main/java/com/mycompany/dsa_final_proj/ui/StubServices.java`

```java
package com.mycompany.dsa_final_proj.ui;

import java.util.List;

/**
 * TEMPORARY stub services — provides fake data so the UI can be built
 * before Dev 1 finishes the real service implementations.
 *
 * DELETE this file on Integration Day (Day 7) and replace with real services.
 */
public class StubServices {

    // ── Inner class: Facility (temporary, until real model package exists) ──
    public static class Facility {
        public final String name;
        public final double x, y;
        public final String type;
        public final String description;

        public Facility(String name, double x, double y, String type, String description) {
            this.name = name; this.x = x; this.y = y;
            this.type = type; this.description = description;
        }

        @Override public String toString() { return name; }
    }

    // ── Stub FacilityService ──
    public static class StubFacilityService {
        private final List<Facility> data = new java.util.ArrayList<>(List.of(
            new Facility("Main Library",       200, 150, "ACADEMIC",       "Central library building"),
            new Facility("Campus Clinic",      350, 280, "MEDICAL",        "Health services center"),
            new Facility("Athletics Center",   500, 100, "SPORTS",         "Gym and sports complex"),
            new Facility("Main Cafeteria",     300, 400, "FOOD_SERVICE",   "Student canteen"),
            new Facility("Admin Building",     150, 300, "ADMINISTRATIVE", "Administrative offices"),
            new Facility("Science Lab",        420, 200, "ACADEMIC",       "Physics & Chemistry lab"),
            new Facility("Computer Lab",       250, 320, "ACADEMIC",       "IT laboratory"),
            new Facility("Chapel",             180, 450, "ADMINISTRATIVE", "Campus chapel"),
            new Facility("Bookstore",          310, 160, "FOOD_SERVICE",   "School supplies shop"),
            new Facility("Security Office",    80,  120, "ADMINISTRATIVE", "Campus security")
        ));

        public List<Facility> getAllFacilities() { return data; }
        public int getSize() { return data.size(); }
        public void addFacility(Facility f) { data.add(f); }
        public void removeFacility(Facility f) { data.remove(f); }
    }

    // ── Stub SearchResult ──
    public static class SearchResult {
        public final Facility facility;
        public final double distance;
        public SearchResult(Facility f, double d) { this.facility = f; this.distance = d; }
    }

    // ── Stub SearchService ──
    public static class StubSearchService {
        private final StubFacilityService fs;
        public StubSearchService(StubFacilityService fs) { this.fs = fs; }

        public SearchResult findNearest(double x, double y) {
            return fs.getAllFacilities().stream()
                .map(f -> new SearchResult(f, Math.hypot(f.x - x, f.y - y)))
                .min(java.util.Comparator.comparingDouble(r -> r.distance))
                .orElse(null);
        }

        public List<SearchResult> findKNearest(double x, double y, int k) {
            return fs.getAllFacilities().stream()
                .map(f -> new SearchResult(f, Math.hypot(f.x - x, f.y - y)))
                .sorted(java.util.Comparator.comparingDouble(r -> r.distance))
                .limit(k)
                .collect(java.util.stream.Collectors.toList());
        }

        public List<SearchResult> findWithinRadius(double x, double y, double radius) {
            return fs.getAllFacilities().stream()
                .map(f -> new SearchResult(f, Math.hypot(f.x - x, f.y - y)))
                .filter(r -> r.distance <= radius)
                .collect(java.util.stream.Collectors.toList());
        }
    }
}
```

> [!TIP]
> This stub **actually does real math** (Euclidean distance) — so your search results will be realistic even before Dev 1's KD-Tree is ready. On Integration Day, you just swap the import.

---

### Step 8: Create the Dashboard FXML

Create: `src/main/resources/views/dashboard.fxml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.control.*?>
<?import javafx.geometry.*?>

<VBox xmlns="http://javafx.com/javafx/21"
      xmlns:fx="http://javafx.com/fxml/1"
      fx:controller="com.mycompany.dsa_final_proj.ui.controller.DashboardController"
      spacing="20"
      alignment="CENTER"
      styleClass="dashboard-root">

    <padding><Insets top="40" right="40" bottom="40" left="40"/></padding>

    <!-- Title -->
    <Label text="🏛 DNSC Smart Campus" styleClass="dash-title"/>
    <Label text="Facility Finder" styleClass="dash-subtitle"/>
    <Label text="Powered by KD-Tree Spatial Search" styleClass="dash-tagline"/>

    <!-- Stats Row -->
    <HBox spacing="20" alignment="CENTER">
        <VBox styleClass="stat-card" alignment="CENTER" spacing="8">
            <Label fx:id="facilityCountLabel" text="0" styleClass="stat-number"/>
            <Label text="Total Facilities" styleClass="stat-desc"/>
        </VBox>
        <VBox styleClass="stat-card" alignment="CENTER" spacing="8">
            <Label text="6" styleClass="stat-number"/>
            <Label text="Facility Types" styleClass="stat-desc"/>
        </VBox>
        <VBox styleClass="stat-card" alignment="CENTER" spacing="8">
            <Label text="KD-Tree" styleClass="stat-number stat-green"/>
            <Label text="Search Engine" styleClass="stat-desc"/>
        </VBox>
    </HBox>

    <!-- Status -->
    <Label fx:id="statusLabel" text="System Ready" styleClass="dash-status"/>

</VBox>
```

---

### Step 9: Create DashboardController.java

Create: `src/main/java/com/mycompany/dsa_final_proj/ui/controller/DashboardController.java`

```java
package com.mycompany.dsa_final_proj.ui.controller;

import com.mycompany.dsa_final_proj.ui.StubServices.StubFacilityService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the Dashboard screen.
 * Shows quick stats: total facilities, system status.
 */
public class DashboardController {

    @FXML private Label facilityCountLabel;
    @FXML private Label statusLabel;

    private final StubFacilityService facilityService = new StubFacilityService();

    /** Called automatically by JavaFX after the FXML is loaded. */
    @FXML
    public void initialize() {
        facilityCountLabel.setText(String.valueOf(facilityService.getSize()));
        statusLabel.setText("✅ System Ready — " + facilityService.getSize() + " facilities loaded");
    }
}
```

---

### Step 10: Update MainApp.java to Load the Dashboard

Replace the placeholder `VBox` in `MainApp.java` with a proper layout that loads `dashboard.fxml`:

```java
// Replace the start() method body in MainApp.java with this:

@Override
public void start(Stage primaryStage) throws Exception {

    // Load the root layout (sidebar + center)
    BorderPane root = new BorderPane();
    root.getStyleClass().add("root-pane");

    // Build sidebar
    VBox sidebar = buildSidebar(root);
    root.setLeft(sidebar);

    // Load Dashboard as the default center screen
    loadScreen(root, "/views/dashboard.fxml");

    // Scene setup
    Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    String cssPath = getClass().getResource("/styles/application.css").toExternalForm();
    scene.getStylesheets().add(cssPath);

    primaryStage.setTitle(APP_TITLE);
    primaryStage.setScene(scene);
    primaryStage.setMinWidth(MIN_WIDTH);
    primaryStage.setMinHeight(MIN_HEIGHT);
    primaryStage.centerOnScreen();
    primaryStage.show();
}

private VBox buildSidebar(BorderPane root) {
    VBox sidebar = new VBox(8);
    sidebar.getStyleClass().add("sidebar");
    sidebar.setPrefWidth(200);

    String[][] navItems = {
        {"🏠", "Dashboard",  "/views/dashboard.fxml"},
        {"🗺", "Map View",   "/views/map.fxml"},
        {"🏛", "Facilities", "/views/facility_form.fxml"},
        {"🔍", "Search",     "/views/search.fxml"},
        {"⚡", "Benchmark",  "/views/benchmark.fxml"},
    };

    for (String[] item : navItems) {
        Button btn = new Button(item[0] + "  " + item[1]);
        btn.getStyleClass().add("nav-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        String fxmlPath = item[2];
        btn.setOnAction(e -> loadScreen(root, fxmlPath));
        sidebar.getChildren().add(btn);
    }

    return sidebar;
}

private void loadScreen(BorderPane root, String fxmlPath) {
    try {
        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
            getClass().getResource(fxmlPath)
        );
        root.setCenter(loader.load());
    } catch (Exception e) {
        // Show a placeholder if FXML doesn't exist yet
        javafx.scene.control.Label placeholder = new javafx.scene.control.Label(
            "🚧  Screen not built yet: " + fxmlPath
        );
        placeholder.setStyle("-fx-text-fill: #7a7aaf; -fx-font-size: 18px;");
        javafx.scene.layout.StackPane p = new javafx.scene.layout.StackPane(placeholder);
        root.setCenter(p);
    }
}
```

> [!IMPORTANT]
> The `loadScreen()` method has a **try-catch fallback** — if a screen's FXML doesn't exist yet, it shows a placeholder instead of crashing. This means you can build and test one screen at a time without breaking the whole app.

---

### Step 11: Update application.css

Add these new styles to `application.css`:

```css
/* ── SIDEBAR ── */
.sidebar {
    -fx-background-color: #13132a;
    -fx-padding: 16 8 16 8;
    -fx-border-color: #2a2a4a;
    -fx-border-width: 0 1 0 0;
}

.nav-btn {
    -fx-background-color: transparent;
    -fx-text-fill: #9090c0;
    -fx-font-size: 13px;
    -fx-font-family: "Segoe UI", sans-serif;
    -fx-padding: 10 16 10 16;
    -fx-alignment: CENTER_LEFT;
    -fx-cursor: hand;
    -fx-background-radius: 8;
}

.nav-btn:hover {
    -fx-background-color: #1e1e3a;
    -fx-text-fill: #ffffff;
}

/* ── DASHBOARD ── */
.dashboard-root {
    -fx-background-color: #0f0f1a;
}

.dash-title {
    -fx-font-size: 32px;
    -fx-font-weight: bold;
    -fx-text-fill: #e8e8f0;
    -fx-font-family: "Segoe UI", sans-serif;
}

.dash-subtitle {
    -fx-font-size: 28px;
    -fx-font-weight: bold;
    -fx-text-fill: #4a9eff;
    -fx-font-family: "Segoe UI", sans-serif;
}

.dash-tagline {
    -fx-font-size: 13px;
    -fx-text-fill: #7a7aaf;
    -fx-font-family: "Segoe UI", sans-serif;
}

.stat-card {
    -fx-background-color: #1a1a2e;
    -fx-background-radius: 12;
    -fx-border-color: #2a2a4a;
    -fx-border-radius: 12;
    -fx-border-width: 1;
    -fx-padding: 24 32 24 32;
    -fx-min-width: 150;
}

.stat-number {
    -fx-font-size: 36px;
    -fx-font-weight: bold;
    -fx-text-fill: #4a9eff;
    -fx-font-family: "Segoe UI", sans-serif;
}

.stat-green {
    -fx-text-fill: #4affa0;
    -fx-font-size: 22px;
}

.stat-desc {
    -fx-font-size: 12px;
    -fx-text-fill: #7a7aaf;
    -fx-font-family: "Segoe UI", sans-serif;
}

.dash-status {
    -fx-font-size: 13px;
    -fx-text-fill: #4affa0;
    -fx-font-family: "Segoe UI", sans-serif;
}
```

---

### Step 12: Run the App Again to See Your Progress

```powershell
.\mvnw.cmd javafx:run
```

You should now see:
- ✅ A **sidebar** on the left with navigation buttons
- ✅ A **Dashboard** in the center with stat cards
- ✅ Clicking sidebar buttons shows "🚧 Screen not built yet" placeholders for unbuilt screens

---

### Step 13: Commit Phase 7a

Once the navigation shell is working:

```powershell
git add src/
git commit -m "7a: Add navigation shell, sidebar, dashboard screen with stub data"
```

When the entire phase is complete and tested:
```powershell
git checkout main
git merge dev-2/phase-7a-navigation
git push origin main
# Message Dev 1: "Phase 7a merged ✅ — pull main"
```

---

## PART 4 — Succeeding Phases (Quick Reference)

### Phase 7b — Facility Manager

```powershell
git checkout main && git pull origin main
git checkout -b dev-2/phase-7b-facility
```

**Files to create:**
1. `src/main/resources/views/facility_form.fxml` — TableView layout
2. `src/main/java/.../ui/controller/FacilityFormController.java` — Table + CRUD logic

**Key things to build:**
- `TableView<StubServices.Facility>` with columns: Name, Type, X, Y, Description
- Add button → opens a Dialog to input new facility data
- Edit button → pre-fills dialog with selected row
- Delete button → removes selected row with confirmation
- ComboBox for type filter

---

### Phase 7c — Map View

```powershell
git checkout -b dev-2/phase-7c-map
```

**Files to create:**
1. `src/main/resources/views/map.fxml` — Canvas + controls layout
2. `src/main/java/.../ui/controller/MapController.java` — Drawing + click handling

**Key things to build:**
- `Canvas` (800×550) that draws colored dots for each facility
- `canvas.setOnMouseClicked(e -> handleClick(e.getX(), e.getY()))`
- Draw a line from click point → nearest facility
- Draw a circle for radius search
- Highlight K dots for K-nearest

---

### Phase 7d — Search Panel

```powershell
git checkout -b dev-2/phase-7d-search
```

**Files to create:**
1. `src/main/resources/views/search.fxml` — Form layout
2. `src/main/java/.../ui/controller/SearchController.java` — Form logic

---

### Phase 7e — Benchmark Screen

```powershell
git checkout -b dev-2/phase-7e-benchmark
```

**Files to create:**
1. `src/main/resources/views/benchmark.fxml`
2. `src/main/java/.../ui/controller/BenchmarkController.java`

---

### Phase 7f — Polish + Integration

```powershell
git checkout -b dev-2/phase-7f-polish
```

**Tasks:**
1. Expand `application.css` with full styling for all screens
2. Replace all `StubServices` with real services from Dev 1:
   - `StubFacilityService` → `FacilityService`
   - `StubSearchService` → `SearchService`
   - `StubBenchmarkService` → `BenchmarkService`
3. End-to-end testing with real KD-Tree data

---

## Quick Command Cheatsheet

| Task | Command |
|------|---------|
| Run the app | `.\mvnw.cmd javafx:run` |
| Check current branch | `git branch` |
| Create a new branch | `git checkout -b dev-2/phase-7x-name` |
| Stage all your files | `git add src/` |
| Commit | `git commit -m "message"` |
| Switch to main | `git checkout main` |
| Pull latest from remote | `git pull origin main` |
| Merge your branch to main | `git merge dev-2/phase-7x-name` |
| Push to remote | `git push origin main` |
