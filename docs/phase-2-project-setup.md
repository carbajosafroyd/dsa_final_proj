# Phase 2 — Project Setup & Configuration

> **Status:** ✅ Complete
> **Date Completed:** June 9, 2026
> **Owner:** Dev 1 & Dev 2 (joint setup)

---

## 1. Objectives

- Initialize the Maven project with all required dependencies
- Configure JavaFX 21.0.2 as the GUI framework
- Establish the launcher pattern for JavaFX compatibility
- Create the application entry point with a styled window
- Set up a local Maven installation for command-line builds
- Verify that the project compiles and the JavaFX window launches

---

## 2. Technology Stack

| Component | Choice | Version | Rationale |
|-----------|--------|---------|-----------|
| **Language** | Java | 24 (targeting 21 bytecode) | Java 24 is installed; targeting 21 LTS for compatibility |
| **GUI Framework** | JavaFX | 21.0.2 | Modern, hardware-accelerated, supports CSS styling. Preferred over Swing for professional appearance. |
| **Build Tool** | Maven | 3.9.6 (local) | Industry-standard Java build tool. Bundled locally via `.mvn/` so no global install is needed. |
| **JSON Library** | Gson | 2.10.1 | Lightweight JSON serialization for facility data persistence. |
| **IDE** | NetBeans | 25 | Team's existing IDE. Maven integration is built-in. |

---

## 3. Deliverables

### 3.1 Maven Configuration — `pom.xml`

**File:** [`pom.xml`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/pom.xml)

Key configuration:

```xml
<maven.compiler.release>21</maven.compiler.release>
<javafx.version>21.0.2</javafx.version>
```

**Dependencies added:**
- `org.openjfx:javafx-controls:21.0.2` — UI controls (Button, Label, TableView, etc.)
- `org.openjfx:javafx-fxml:21.0.2` — FXML layout support
- `com.google.code.gson:gson:2.10.1` — JSON persistence

**Plugins configured:**
- `maven-compiler-plugin:3.11.0` — Compiles with `--release 21`
- `javafx-maven-plugin:0.0.8` — Handles JavaFX module path for `mvnw.cmd javafx:run`

### 3.2 Launcher Class — `Dsa_final_proj.java`

**File:** [`Dsa_final_proj.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/Dsa_final_proj.java)

**Design Decision — The Launcher Pattern:**

JavaFX applications require the main class to extend `javafx.application.Application`. However, when JavaFX is loaded via the classpath (not the module path), Java's launcher performs a module check that fails if the main class extends `Application`.

The standard workaround is the **launcher pattern**:
- `Dsa_final_proj` (the main class) does **NOT** extend `Application`
- It calls `Application.launch(MainApp.class, args)` to start the JavaFX runtime
- `MainApp` (which extends `Application`) handles the actual window setup

This pattern is used in production JavaFX applications and is documented in the OpenJFX FAQ.

```java
public class Dsa_final_proj {
    public static void main(String[] args) {
        Application.launch(MainApp.class, args);
    }
}
```

### 3.3 JavaFX Application — `MainApp.java`

**File:** [`MainApp.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/ui/MainApp.java)

- Uses `BorderPane` as the root layout (supports top/left/center/right/bottom regions)
- Window dimensions: 1200×750 (minimum 900×600)
- Loads `application.css` for dark theme styling
- Currently shows placeholder content (to be replaced in Phase 7)

### 3.4 Application Stylesheet — `application.css`

**File:** [`application.css`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/resources/styles/application.css)

Establishes the visual design language:
- Dark background: `#0f0f1a`
- Text color: `#e8e8f0`
- Accent color: `#4a9eff` (blue)
- Font family: Segoe UI (Windows native)

### 3.5 Maven Wrapper — `mvnw.cmd`

**File:** [`mvnw.cmd`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/mvnw.cmd)

Provides a local Maven installation at `.mvn/maven/apache-maven-3.9.6/` so the project can be built from any terminal without requiring a global Maven installation.

**Usage:**
```bash
.\mvnw.cmd compile          # Compile the project
.\mvnw.cmd javafx:run       # Compile and launch the application
```

---

## 4. Project Structure After Phase 2

```
dsa_final_proj/
├── .mvn/
│   ├── maven/                          # Local Maven 3.9.6 installation
│   └── wrapper/
│       └── maven-wrapper.properties
├── src/
│   └── main/
│       ├── java/com/mycompany/dsa_final_proj/
│       │   ├── Dsa_final_proj.java     # Launcher (entry point)
│       │   └── ui/
│       │       └── MainApp.java        # JavaFX Application
│       └── resources/
│           └── styles/
│               └── application.css     # Dark theme stylesheet
├── mvnw.cmd                            # Maven Wrapper script
└── pom.xml                             # Maven configuration
```

---

## 5. Verification

| Check | Result |
|-------|--------|
| `mvnw.cmd compile` | ✅ BUILD SUCCESS |
| `mvnw.cmd javafx:run` | ✅ Window opens with dark theme |
| Window title | ✅ "DNSC Smart Campus Facility Finder" |
| Minimum window size enforced | ✅ 900×600 |

---

## 6. How to Build and Run

**From the terminal:**
```bash
cd C:\Users\Admin\Documents\NetBeansProjects\dsa_final_proj
.\mvnw.cmd javafx:run
```

**From NetBeans:**
1. Open the project (File → Open Project)
2. Right-click project → Run (NetBeans uses its internal Maven)

---

## 7. Notes for the Manuscript

This phase establishes the following points for the **Methodology** section:

- The project uses **Java 21** (compiled with Java 24) and **JavaFX 21.0.2**
- The build system is **Apache Maven 3.9.6**
- The application follows the **MVC architectural pattern** natively supported by JavaFX
- The launcher pattern ensures compatibility across different Java configurations
- CSS-based styling enables a professional, modern visual design
