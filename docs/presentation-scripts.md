# System Demo — Simplified Scripts

> **Total Duration:** ~15 minutes
> **Rule:** Show faces, use your own voices, upload to YouTube/Facebook, max 30 min

---

## MEMBER 1 — Intro (~2 min)

**[Slide: Title, group number, names]**

"Good day, Sir. We are Group ___, and our project is the **DNSC Smart Campus Facility Finder**.

Our members are: [say all 5 names].

**[Slide: Problem → Solution]**

The problem is simple — if you're standing somewhere on campus and you need to find the nearest clinic, or the nearest canteen, how do you figure that out fast?

The basic approach is to store everything in a list and check the distance to every facility one by one. That's O(n) — it gets slow when the data grows.

Our solution is to use a **KD-Tree**, which is a binary tree that organizes points by their X and Y coordinates. It can skip entire sections of the map during a search, so instead of checking everything, it only checks around log n nodes. Way faster.

Let me pass it to [Member 2] to explain how the KD-Tree works."

---

## MEMBER 2 — Algorithm (~3 min)

**[Slide: KD-Tree diagram showing alternating X/Y splits]**

"So a KD-Tree works like a binary search tree, but it alternates between comparing X and Y at each level.

At depth 0, we split by X. At depth 1, we split by Y. Depth 2, X again. And so on. This divides the map into smaller and smaller regions.

**[Slide: Nearest Neighbor — 3 steps]**

The main algorithm is Nearest Neighbor Search, and it has three steps:

**First**, we go down the tree like we're inserting the point — that gives us an initial guess.

**Second**, as we go back up, we check if the current node is closer than our guess. If yes, we update.

**Third** — and this is the important part — at each node, we check the distance from our target to the splitting line. If that distance is bigger than our current best, we skip the entire other side of the tree. That's called **pruning**, and that's what makes it O(log n).

We also implemented K-Nearest using a Max-Heap to track the top K results, and Radius Search which checks if the search circle crosses each splitting line.

[Member 3] will now show the system."

---

## MEMBER 3 — Live Demo (~5 min)

**[Screen: Launch the app]**

"Alright, let me run the system.

**[App opens to Dashboard]**

This is the dashboard — it shows total facilities, tree status, and a distribution chart.

**[Click Facilities tab]**

Here's our facility manager. I'll add a new one — 'Student Lounge', Food Service, coordinates 350 and 200.

**[Add it]**

It shows up in the table. Behind the scenes, it was inserted into the KD-Tree and saved to a JSON file.

I can also edit and delete. When we edit coordinates, the system actually deletes the old node and inserts a new one since the tree is organized by position.

**[Click Map tab]**

Now the map. Each dot is a facility, color-coded by type.

I'll click somewhere — and it finds the nearest facility with a dashed line.

**[Click map — nearest neighbor]**

Now let me switch to K-Nearest, set K to 3, and click again.

**[Switch, click]**

Three lines, three results, sorted by distance.

And Radius Search — I'll set it to 150 and click.

**[Switch, click]**

You can see the circle and all the facilities inside it highlighted.

That's the core system. Now [Member 4] will show the performance proof."

---

## MEMBER 4 — Benchmark (~3 min)

**[Click Benchmark tab]**

"So is the KD-Tree actually faster? Let's test it.

**[Click Run Benchmark]**

This runs a Nearest Neighbor search using both a regular ArrayList loop and our KD-Tree, at different dataset sizes up to 1 million.

Look at the chart — the gray line is Linear Search, the green line is KD-Tree. The gray line goes up fast. The green line stays almost flat.

The numbers: at 1 million facilities, Linear Search took about 6.3 million nanoseconds. The KD-Tree took about 10,900. That's roughly **580 times faster**.

Why? Because Linear Search is O(n) — more data means more time. KD-Tree is O(log n) — even with a million entries, it only visits around 20 nodes because of pruning.

That proves our data structure works. [Member 5] will close us out."

---

## MEMBER 5 — Code & Conclusion (~2 min)

**[Slide: Package structure]**

"Quick rundown of our code structure. We have six packages:

- `model` for data classes
- `tree` for the KD-Tree itself
- `util` for distance calculations
- `persistence` for saving to JSON
- `service` as the middle layer between UI and tree
- `ui` for the JavaFX interface

The UI never touches the KD-Tree directly — it goes through the service layer. That's Separation of Concerns.



**[Slide: Conclusion]**

So in summary — we built a working facility finder that uses a KD-Tree for fast spatial search. It supports full CRUD, three search modes with map visualization, and our benchmark proves it's up to 580 times faster than linear search.

Thank you, Sir. 

---
