<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>



<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/carbajosafroyd/dsa_final_proj">
    <img src="src/main/resources/images/logo.png" alt="Logo" width="120" height="120">
  </a>

  <h3 align="center">DNSC Smart Campus Facility Finder</h3>

  <p align="center">
    A spatial search engine powered by a KD-Tree that enables fast nearest-facility lookups across the DNSC campus map.
    <br />
    <a href="https://github.com/carbajosafroyd/dsa_final_proj"><strong>Explore the repository »</strong></a>
    <br />
    <br />
    <a href="#usage">How to Run</a>
    ·
    <a href="https://github.com/carbajosafroyd/dsa_final_proj/issues">Report Bug</a>
    ·
    <a href="https://github.com/carbajosafroyd/dsa_final_proj/issues">Request Feature</a>
  </p>
</div>

<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#features">Features</a></li>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#core-algorithm">Core Algorithm</a></li>
    <li><a href="#troubleshooting">Common Issues & Troubleshooting</a></li>
    <li><a href="#project-structure">Project Structure</a></li>
    <li><a href="#team">Team</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://github.com/carbajosafroyd/dsa_final_proj)

The **DNSC Smart Campus Facility Finder** is a JavaFX desktop application developed as a final project for IT221 Data Structures and Algorithms. It solves a real-world spatial problem: given a user's position on the DNSC campus, the system can instantly locate the nearest facilities — clinics, canteens, academic buildings, and more — using a custom-built **KD-Tree** data structure instead of brute-force linear search.

At 1,000,000 facilities, the KD-Tree finds the nearest neighbor **580x faster** than a standard ArrayList scan, empirically proving O(log n) performance.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Features

* **Spatial Search Engine:**
  * **Nearest Neighbor** — Find the single closest facility to any point on the map.
  * **K-Nearest Neighbors** — Find the top K closest facilities using a Max-Heap.
  * **Radius Search** — Find all facilities within a specified distance.
* **Full CRUD Operations:**
  * Create, read, update, and delete campus facilities through a clean form interface.
  * All changes are persisted to a JSON file automatically.
* **Interactive Campus Map:**
  * Point-and-click search directly on a 2D campus map.
  * Visual feedback with dashed connecting lines and highlighted result markers.
  * Right-click to plot new facilities at any map coordinate.
* **Live Performance Benchmarking:**
  * Head-to-head comparison of KD-Tree vs Linear Search at scales up to 1,000,000 records.
  * Real-time chart visualization of O(log n) vs O(n) scaling.
* **Modern UI:**
  * Dark forest-green themed interface with minimalist card-based dashboard.
  * Responsive sidebar navigation with SVG icons.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Built With

This project is built entirely in Java with the following technologies:

* [![Java][Java.com]][Java-url]
* [![JavaFX][JavaFX.com]][JavaFX-url]
* [![Maven][Maven.com]][Maven-url]
* [![Gson][Gson.com]][Gson-url]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- GETTING STARTED -->
## Getting Started

To get a local copy of the project up and running, follow these simple steps.

### Prerequisites

Ensure you have the following installed on your local machine:
* **Java Development Kit (JDK) 17** or higher
* **Internet connection** (first run only, to download Maven dependencies)

No other installations required — the Maven wrapper is included in the project.

### Installation

1. **Clone the repository**
   ```sh
   git clone https://github.com/carbajosafroyd/dsa_final_proj.git
   cd dsa_final_proj
   ```

2. **Run the application**
   ```sh
   .\mvnw.cmd javafx:run
   ```

3. **That's it.** The application window will launch automatically.

> **Note:** The first run may take 1–2 minutes to download dependencies. Subsequent runs will start immediately.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- USAGE -->
## Usage

Once the application launches, you'll see the Dashboard with a sidebar navigation:

* **Dashboard** — Overview of total facilities, tree balance status, and facility type distribution chart.
* **Facilities** — Add, edit, or delete campus facilities. All changes are saved to `facilities.json`.
* **Map** — Click anywhere on the campus map to perform spatial searches. Toggle between Nearest Neighbor, K-Nearest, and Radius Search modes on the right panel.
* **Search** — Text-based search across facility names and descriptions with type filtering.
* **Benchmark** — Run live performance tests comparing KD-Tree vs Linear Search at various dataset sizes.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CORE ALGORITHM -->
## Core Algorithm

**KD-Tree (K-Dimensional Tree)** — a space-partitioning binary search tree for organizing points in 2D space.

**How it works:**
1. Each level of the tree alternates between splitting on the **X-axis** (even depths) and the **Y-axis** (odd depths).
2. The tree is constructed using **median partitioning** (`buildBalanced`), guaranteeing O(log n) height.
3. **Nearest Neighbor Search** traverses the tree with a greedy descent, then backtracks while **pruning** subtrees that cannot contain a closer point — achieving O(log n) average-case performance.

**Benchmark Results:**

| Dataset Size | Linear Search (O(n)) | KD-Tree (O(log n)) | Speedup |
|---:|---:|---:|---:|
| 100 | 900 ns | 600 ns | 1.5x |
| 1,000 | 7,400 ns | 800 ns | 9.25x |
| 10,000 | 25,500 ns | 3,100 ns | 8.23x |
| 100,000 | 223,600 ns | 3,200 ns | 69.88x |
| **1,000,000** | **6,331,000 ns** | **10,900 ns** | **580.83x** |

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- TROUBLESHOOTING -->
## Common Issues & Troubleshooting

If you encounter any issues during setup or execution, refer to the solutions below:

* **"JAVA_HOME is not set":** Ensure JDK 17+ is installed and the `JAVA_HOME` environment variable points to it.
* **"Error: JavaFX runtime components are missing":** Do not run with `java -jar`. Always use `.\mvnw.cmd javafx:run` — the Maven plugin handles the JavaFX module path automatically.
* **Dependencies fail to download:** Check your internet connection. Maven needs to download dependencies on first run.
* **Application is slow on first launch:** This is normal — Maven is compiling and downloading. Subsequent launches are instant.
* **Map image not loading:** Ensure the `src/main/resources/images/` directory contains `map-v2.png`.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- PROJECT STRUCTURE -->
## Project Structure

A brief overview of the project's architecture:

```text
src/main/java/com/mycompany/dsa_final_proj/
├── model/                  Data classes (Facility, SearchResult, BenchmarkResult)
├── tree/                   KD-Tree implementation (KDNode, KDTree)
├── util/                   Utilities (DistanceCalculator, SampleDataGenerator)
├── persistence/            JSON persistence layer (DataStore)
├── service/                Business logic layer
│   ├── FacilityService     CRUD operations + tree synchronization
│   ├── SearchService       Nearest, K-Nearest, Radius search
│   └── BenchmarkService    Performance comparison engine
└── ui/                     JavaFX application
    ├── MainApp             Application entry point + sidebar
    └── controller/         FXML controllers for each screen

src/main/resources/
├── views/                  FXML layout files
├── styles/                 CSS stylesheets
├── images/                 Campus map and logo assets
└── data/                   JSON facility data
```

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- TEAM -->
##  Team

**DNSC Smart Campus Team** — *IT221 Final Project - Institute of Computing, DNSC*

* **FROYD D. CARBAJOSA**
* **ETHAN MAR D. DEMETERIO**


<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[contributors-shield]: https://img.shields.io/github/contributors/carbajosafroyd/dsa_final_proj.svg?style=for-the-badge&v=1
[forks-shield]: https://img.shields.io/github/forks/carbajosafroyd/dsa_final_proj.svg?style=for-the-badge&v=1
[stars-shield]: https://img.shields.io/github/stars/carbajosafroyd/dsa_final_proj.svg?style=for-the-badge&v=1
[issues-shield]: https://img.shields.io/github/issues/carbajosafroyd/dsa_final_proj.svg?style=for-the-badge&v=1

[Java.com]: https://img.shields.io/badge/Java_21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://openjdk.org/
[JavaFX.com]: https://img.shields.io/badge/JavaFX_21-007396?style=for-the-badge&logo=java&logoColor=white
[JavaFX-url]: https://openjfx.io/
[Maven.com]: https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white
[Maven-url]: https://maven.apache.org/
[Gson.com]: https://img.shields.io/badge/Gson-4285F4?style=for-the-badge&logo=google&logoColor=white
[Gson-url]: https://github.com/google/gson

[product-screenshot]: src/main/resources/images/campus_map.png
