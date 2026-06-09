# Phase 4 — CRUD Operations & Persistence

> **Status:** ✅ Complete
> **Date Completed:** June 9, 2026
> **Owner:** Dev 1 (The Engine)

---

## 1. Objectives

- Establish the `FacilityService` as the interface for creating, reading, updating, and deleting facilities.
- Ensure that the KD-Tree structure correctly updates when facility data changes.
- Persist data to a JSON file (`dnsc_facilities.json`) using Gson, so changes are not lost when the app closes.
- Automatically rebuild a perfectly balanced KD-Tree from the JSON data when the application starts.

---

## 2. System Architecture & Design Decisions

### 2.1 The DataStore (JSON Persistence)

**File:** [`DataStore.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/persistence/DataStore.java)

**Design Decision:** We **do not** save the KD-Tree itself to JSON. 
Saving a tree structure directly is messy and prone to serialization errors due to nested left/right pointers. Instead, `DataStore` simply saves a flat list of `Facility` objects. 

When the app launches, `DataStore` loads the list from JSON, and we pass it to `KDTree.buildBalanced()`. This guarantees that every time the app opens, the tree is rebuilt perfectly balanced (O(log n) height). The file is saved in the user's home directory so it works reliably on any computer without permission errors.

### 2.2 The Service Layer (Facade Pattern)

**File:** [`FacilityService.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/service/FacilityService.java)

The `FacilityService` encapsulates the interaction between the tree and the storage system. 
The UI controllers must use this class instead of modifying the KD-Tree directly. This implements the **Facade Pattern** and the **Single Responsibility Principle**.

Every time a modification is made (`addFacility`, `removeFacility`, `updateFacility`), the service automatically calls `DataStore.save()` to immediately synchronize the memory state with the disk state.

### 2.3 Handling "Updates" in a KD-Tree

KD-Trees organize data based on spatial coordinates (X and Y). If a user edits a facility's coordinates while it is already inside the tree, it would violate the structural invariants (the node might now belong in the left branch instead of the right).

**Solution:** The `updateFacility` method does not modify the object in place. Instead, it:
1. `delete()`s the old facility.
2. `insert()`s the new facility with the updated data.
This guarantees the tree structure remains mathematically valid.

---

## 3. Verification & Testing

**File:** [`CRUDTest.java`](file:///C:/Users/Admin/Documents/NetBeansProjects/dsa_final_proj/src/main/java/com/mycompany/dsa_final_proj/service/CRUDTest.java)

A dedicated test suite was built to verify persistence. It creates a temporary JSON file and verifies the following:
- ✅ **Add & Save:** Inserting facilities successfully generates the JSON file.
- ✅ **Load & Rebuild:** Simulating an app restart successfully loads the JSON file and reconstructs the KD-Tree.
- ✅ **Update:** Updating a facility correctly swaps the nodes and updates the active size.
- ✅ **Remove:** Deleting a facility triggers lazy deletion and immediately writes the updated list to JSON.

---

## 4. Notes for the Manuscript

For the documentation and defense:
- Emphasize the **separation of concerns**. The UI knows nothing about `DataStore` or `KDTree`. It only knows about `FacilityService`.
- Highlight the **rebuilding strategy**. By saving a flat list and calling `buildBalanced()` on startup, the system essentially performs an automatic "defragmentation" and optimization of the index every time the program runs.
