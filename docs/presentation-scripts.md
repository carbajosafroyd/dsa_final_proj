# System Demo — Simplified Scripts

> **Total Duration:** ~15 minutes
> **Rule:** Show faces, use your own voices, upload to YouTube/Facebook, max 30 min

---

## MEMBER 1 — Intro (~2 min)

**[Slide: Title, group number, names]**

"Good day, Sir. We are Group KD-Tree, and our project is the **DNSC Facility Finder**.

Our members are: 
JHON KYLE L. COLLADOS
ED BERNARD A. LUZON
FROYD D. CARBAJOSA
ALFREDO A. SOCO
ETHAN MAR D. DEMETERIO    

**[Slide: Problem → Solution]**
so why we chose this project "DNSC Facility Finder", because we noticed that some incoming first year students encoutered difficulties in finding some facilities in the campus like for example a student is standing somewhere on campus and you need to find the nearest clinic, or the nearest canteen, or the OSDS for their admission process. and also if you ask some seniors in the campus it will take time.

so the traditional or basic approach is to store everything in a list and check the distance to every facility one by one. That's O(n) time complexity — it gets slow when the data grows.

Our solution is to use a **KD-Tree**, which is a binary tree that organizes points by their X and Y coordinates. It can skip entire sections of the map during a search, so instead of checking everything, it only checks around log n nodes. Way faster and more efficient.

Let me pass it to Froyd to explain how the KD-Tree works."

---

## MEMBER 2 — Algorithm (~3 min)
[Show the diagram on screen]                           
                                                         
  "So this is how a KD-Tree is structured. It looks like 
  a regular binary tree, but with one key difference —     
  notice the alternating colors.                         
                                                         
  Pink nodes compare X. Blue nodes compare Y.            
                                                         
  At depth 0 — the root — it's pink, so we compare X     
  coordinates. If a facility's X is smaller, we go left. 
  If it's bigger, we go right.                           
                                                         
  At depth 1 — it switches to blue — now we compare Y.   
  Smaller Y goes left, bigger goes right.                
                                                         
  At depth 2 — back to pink — X again. And it just keeps 
  alternating. That's literally the whole structure of a 
  KD-Tree. Each level cuts the map in a different        
  direction.                                             
                                                         
  [Pause, then explain the search]                       
                                                         
  Now, why is this fast? Let's say I'm searching for the 
  nearest facility. I start at the top and go down — left
  or right — based on my coordinates. That gives me an   
  initial guess.                                         
                                                         
  Then as I go back up, at each node I ask: Could there  
  be something closer on the other side? I check the     
  distance to the splitting line. If my current best is  
  already closer than that line — I skip the entire other
  side. That's called pruning.                           
                                                         
  That's why it's O(log n). Instead of checking every    
  facility, we skip half the tree at almost every level. 
                                                         
  That's the KD-Tree. [Member 3] will now show the actual
  system."             

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

That proves our data structure works. Now will proceed to the last part and kyle will close us out."

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
