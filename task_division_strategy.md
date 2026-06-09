# Task Division Strategy — 2 Developers, Zero Conflicts

## The Core Principle

The reason teams get merge conflicts is because **two people edit the same file at the same time**. Our architecture was designed to prevent this. Each package is a clean boundary — one person owns it.

---

## Developer Assignments

### Dev 1 (You) — "The Engine"
**You own the brain of the system.**

```
src/main/java/com/mycompany/dsa_final_proj/
├── model/                    ← YOU OWN THIS
│   ├── Facility.java
│   ├── FacilityType.java
│   └── SearchResult.java
│
├── tree/                     ← YOU OWN THIS (the star of the project)
│   ├── KDNode.java
│   └── KDTree.java
│
├── service/                  ← YOU OWN THIS
│   ├── FacilityService.java
│   ├── SearchService.java
│   └── BenchmarkService.java
│
├── persistence/              ← YOU OWN THIS
│   └── DataStore.java
│
└── util/                     ← YOU OWN THIS
    ├── DistanceCalculator.java
    └── SampleDataGenerator.java
```

**Your phases:** 3 → 4 → 5 → 6 → 8 (partial)

| Phase | What You Build | Timeline |
|-------|---------------|----------|
| Phase 3 | `Facility`, `FacilityType`, `KDNode`, `KDTree` (insert + buildBalanced) | Days 1–3 |
| Phase 4 | `FacilityService`, `DataStore`, `facilities.json` | Days 3–4 |
| Phase 5 | `SearchService.findNearest()`, `SearchService.findKNearest()` | Days 4–6 |
| Phase 6 | `SearchService.findWithinRadius()` | Days 6–7 |
| Phase 8 | `BenchmarkService`, correctness validation | Days 8–9 |

---

### Dev 2 (Co-Dev) — "The Interface"
**They own everything the user sees and touches.**

```
src/main/java/com/mycompany/dsa_final_proj/
├── ui/                       ← CO-DEV OWNS THIS
│   ├── MainApp.java
│   └── controller/
│       ├── DashboardController.java
│       ├── FacilityFormController.java
│       ├── SearchController.java
│       ├── MapController.java
│       └── BenchmarkController.java

src/main/resources/           ← CO-DEV OWNS THIS
├── styles/
│   └── application.css
├── views/
│   ├── dashboard.fxml
│   ├── facility_form.fxml
│   ├── search.fxml
│   ├── map.fxml
│   └── benchmark.fxml
└── images/
    └── (any icons or campus map image)
```

**Their phases:** 7 (but broken into sub-phases so they're not idle)

| Phase | What They Build | Timeline |
|-------|----------------|----------|
| 7a | Navigation shell — sidebar + screen switching | Days 1–3 |
| 7b | Facility Manager screen — table + add/edit/delete forms | Days 3–5 |
| 7c | Map View — Canvas with dots, click handler | Days 5–7 |
| 7d | Search Panel — input fields + results display | Days 7–8 |
| 7e | Benchmark screen — timing display | Days 8–9 |
| 7f | Polish — CSS, colors, transitions | Days 9–10 |

---

## The Integration Contract

> [!IMPORTANT]
> This is the key to zero conflicts. Dev 1 and Dev 2 **never** edit each other's files. They communicate through **interfaces** (the service classes).

### How It Works

```
┌─────────────────────┐         ┌─────────────────────┐
│     Dev 2 (UI)      │         │   Dev 1 (Engine)    │
│                     │         │                     │
│  MapController      │────────▶│  FacilityService    │
│  SearchController   │────────▶│  SearchService      │
│  BenchmarkController│────────▶│  BenchmarkService   │
│                     │         │                     │
│  Calls methods on   │         │  Returns data to    │
│  the Service layer  │         │  the UI layer       │
└─────────────────────┘         └─────────────────────┘
```

**Dev 2 never touches KDTree.java.** They call `searchService.findNearest(x, y)` and get back a `SearchResult`. They don't care how the tree works internally.

**Dev 1 never touches FXML or Controllers.** They expose clean public methods on the Service classes and trust that the UI will call them correctly.

### The Handshake — Service Method Signatures

Before both devs start working in parallel, you **agree on the service method signatures first**. These are the contracts:

```java
// FacilityService — Dev 2 will call these methods
public class FacilityService {
    void addFacility(Facility facility);
    void removeFacility(Facility facility);
    void updateFacility(Facility oldFacility, Facility newFacility);
    List<Facility> getAllFacilities();
    int getSize();
}

// SearchService — Dev 2 will call these methods
public class SearchService {
    SearchResult findNearest(double x, double y);
    List<SearchResult> findKNearest(double x, double y, int k);
    List<SearchResult> findWithinRadius(double x, double y, double radius);
}

// BenchmarkService — Dev 2 will call these methods
public class BenchmarkService {
    BenchmarkResult compareNearestNeighbor(int datasetSize, double queryX, double queryY);
}
```

**Dev 1 implements the real logic.**
**Dev 2 can start building UI immediately using stub/mock versions** that return fake data — then swap in the real services when Dev 1 is ready.

---

## Git Branching Strategy

Keep it simple. You're 2 people, not a 50-person enterprise.

```
main (protected — never commit directly)
  │
  ├── dev-1/phase-3-kdtree      ← You work here
  ├── dev-1/phase-4-crud        ← Then here
  ├── dev-1/phase-5-search      ← Then here
  │
  ├── dev-2/phase-7a-navigation ← Co-dev works here
  ├── dev-2/phase-7b-facility   ← Then here
  ├── dev-2/phase-7c-map        ← Then here
  │
  └── (merge to main when a phase is complete and tested)
```

### Rules

| Rule | Why |
|------|-----|
| **Never commit directly to `main`** | Main should always be in a working state |
| **One branch per phase** | Small, focused branches are easier to review and merge |
| **Merge to `main` only when your phase is complete** | Don't merge half-done work |
| **Pull from `main` before creating a new branch** | Stay up to date with each other's merged work |
| **If you must edit a shared file, communicate first** | The only shared files are `pom.xml` and `Dsa_final_proj.java` — these rarely change |

### The Only Shared Files (Danger Zone)

These are the ONLY files both devs might need to touch:

| File | Who Edits | Rule |
|------|-----------|------|
| `pom.xml` | Either (rare) | Communicate before editing. Only changes if a new dependency is needed. |
| `Dsa_final_proj.java` | Neither after Phase 2 | Already done. Don't touch it. |
| `MainApp.java` | Dev 2 primarily | Dev 1 should never need to edit this. |

Everything else is cleanly separated by package ownership.

---

## Parallel Work Timeline

```
Day     Dev 1 (Engine)                    Dev 2 (Interface)
─────   ─────────────────────────         ─────────────────────────────
 1      Facility + FacilityType models    Learn JavaFX basics (tutorials)
 2      KDNode + KDTree (insert)          Navigation shell + sidebar
 3      KDTree (buildBalanced) + tests    Screen switching framework
 4      FacilityService + DataStore       Facility table (with stub data)
 5      SearchService.findNearest()       Facility add/edit/delete forms
 6      SearchService.findKNearest()      Map canvas (draw dots from stub)
 7      SearchService.findWithinRadius()  ★ INTEGRATION: swap stubs → real services
 8      BenchmarkService                  Search panel + result display
 9      Correctness validation            Benchmark screen + CSS polish
10      Final testing together            Final testing together
```

> [!TIP]
> **Day 7 is "Integration Day."** This is when Dev 2 replaces their stub services with Dev 1's real implementations. If both devs followed the agreed method signatures, this should take less than 30 minutes — just change the constructor calls.

---

## What Dev 2 Does While Waiting (Days 1–3)

Dev 2 can't use `FacilityService` yet because Dev 1 hasn't built it. So Dev 2 creates a **stub service** with hardcoded data:

```java
// Temporary stub — Dev 2 creates this to unblock themselves
public class StubFacilityService {
    public List<Facility> getAllFacilities() {
        // Return hardcoded test data so UI development can proceed
        return List.of(
            new Facility("Library", 200, 150, FacilityType.ACADEMIC, "Main Library"),
            new Facility("Clinic", 350, 280, FacilityType.MEDICAL, "Campus Clinic"),
            new Facility("Gym", 500, 100, FacilityType.SPORTS, "Athletics Center")
        );
    }
}
```

This lets Dev 2 build and test the entire UI **without waiting for the KD-Tree to be finished**. On Integration Day, they just replace `StubFacilityService` with the real `FacilityService`.

---

## Communication Rules

| When | What To Do |
|------|-----------|
| Before editing `pom.xml` | Message your co-dev: "I'm adding dependency X to pom.xml" |
| When a service method signature changes | Message immediately — both sides need to update |
| When you finish a phase | Merge to main, message co-dev: "Phase X merged, pull main" |
| When you're stuck | Ask before spending 2+ hours on a bug. Fresh eyes help. |
| Daily | Quick 5-min sync: "What I did today, what I'm doing tomorrow, any blockers" |

---

## Summary

| Aspect | Dev 1 (You) | Dev 2 (Co-Dev) |
|--------|------------|----------------|
| **Packages** | `model/`, `tree/`, `service/`, `persistence/`, `util/` | `ui/`, `resources/` |
| **Focus** | Data structure + business logic | User interface + styling |
| **Key output** | A working KD-Tree with clean service APIs | A polished JavaFX app that calls those APIs |
| **Defense knowledge** | Must explain KD-Tree algorithm in depth | Must explain MVC pattern and UI design choices |
| **Files touched** | ~12 Java files | ~10 Java files + 5 FXML + 1 CSS |
| **Conflict risk** | Zero (different packages) | Zero (different packages) |
