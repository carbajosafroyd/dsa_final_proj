================================================================================
  DNSC SMART CAMPUS FACILITY FINDER
  IT221 Data Structures and Algorithms — Final Project
  Davao del Norte State College | S.Y. 2024-2025, 2nd Semester
================================================================================

GROUP MEMBERS
-------------
  1. FROYD D. CARBAJOSA
  2. ETHAN MAR D. DEMETERIO
 


PROJECT DESCRIPTION
-------------------
  A JavaFX desktop application that enables spatial search across DNSC
  campus facilities using a KD-Tree data structure. The system supports
  Nearest Neighbor, K-Nearest Neighbors, and Radius Search — all
  achieving O(log n) average-case performance compared to O(n) linear
  search.


PREREQUISITES
-------------
  - Java Development Kit (JDK) 17 or higher
  - Internet connection (for first-time Maven dependency download only)
  - No additional installations required — Maven wrapper is included


HOW TO RUN
----------
  1. Open a terminal/command prompt.

  2. Navigate to the project folder:
     cd path\to\dsa_final_proj

  3. Run the application:
     .\mvnw.cmd javafx:run

  4. The application window will open automatically.

  NOTE: The first run may take 1-2 minutes to download dependencies.
        Subsequent runs will start immediately.


PROJECT STRUCTURE
-----------------
  src/main/java/com/mycompany/dsa_final_proj/
  |
  |-- model/              Data classes (Facility, SearchResult, BenchmarkResult)
  |-- tree/               KD-Tree implementation (KDNode, KDTree)
  |-- util/               Utility classes (DistanceCalculator, SampleDataGenerator)
  |-- persistence/        JSON data persistence (DataStore)
  |-- service/            Business logic (FacilityService, SearchService, BenchmarkService)
  |-- ui/                 JavaFX application entry and controllers
  |   |-- controller/     FXML controllers for each screen
  |
  src/main/resources/
  |-- views/              FXML layout files
  |-- styles/             CSS stylesheets
  |-- images/             Map and logo assets
  |-- data/               JSON facility data file


KEY FEATURES
------------
  - Full CRUD operations for campus facilities
  - Interactive campus map with point-and-click spatial search
  - Three search modes: Nearest Neighbor, K-Nearest, Radius Search
  - Live performance benchmarking (KD-Tree vs Linear Search)
  - JSON-based data persistence
  - Modern, dark-green themed UI


TECHNOLOGIES USED
-----------------
  - Java 21
  - JavaFX 21 (GUI framework)
  - Gson 2.10.1 (JSON serialization)
  - Maven (build automation)


CORE DATA STRUCTURE
-------------------
  KD-Tree (K-Dimensional Tree)
  - A space-partitioning binary tree that organizes 2D coordinate data
  - Alternates splitting axis (X, Y) at each depth level
  - Supports efficient spatial queries via subtree pruning
  - All search algorithms are custom implementations (no external libraries)


NOTES FOR THE INSTRUCTOR
-------------------------
  - The KD-Tree and all spatial search algorithms (Nearest Neighbor,
    K-Nearest Neighbors, Radius Search) are fully implemented from
    scratch. No third-party spatial libraries were used.

  - The benchmark module generates up to 1,000,000 synthetic facilities
    to demonstrate O(log n) vs O(n) scaling. Results are displayed
    in a live chart within the application.

  - Facility data is persisted in JSON format. On application startup,
    the tree is rebuilt using a balanced construction algorithm
    (median partitioning) to guarantee optimal search performance.

  - The project uses Maven for dependency management. The included
    Maven wrapper (mvnw.cmd) means no global Maven installation is
    needed — just run the command listed above.

================================================================================
