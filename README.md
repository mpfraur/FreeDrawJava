# Pràctica 2: Entorns Servidor — Project Requirements

This document outlines the functional, technical, and delivery requirements for **Pràctica 2 (Entorns Servidor)**, building upon the foundation of Pràctica 1.

---

## ⚡ TL;DR — Quick Summary Checklist

### Core Features
- [ ] **Auto-Save:** Automatically save images when objects are added/removed (or periodically, e.g., every minute).
- [ ] **Version History:** Keep previous versions with timestamps every time an image is saved; allow viewing and restoring past versions.
- [ ] **Soft Delete (Trash Bin):** Deleting an image moves it to the user's trash; permanently deleting it from the trash removes the image and all its version history from the database.
- [ ] **Permissions & Sharing:** Images are private by default. Owners can share images publicly or with specific users using **Read** (`lectura`) or **Write** (`escriptura`) permissions.
- [ ] **Image Duplication:** Users can clone/copy any accessible image (own or shared) without copying its version history.

### Tech Stack & Architecture
- [ ] **Framework:** Spring Web (`spring-web`).
- [ ] **Database & Persistence:** MySQL database accessed via `JdbcTemplate` (data must persist across server restarts).
- [ ] **Session Management:** Use `HttpSession` to isolate and secure user actions/drawings.
- [ ] **Architecture & UI:** Apply class-taught design patterns (MVC, DAO/Repository, Service, etc.) and style the frontend nicely (Bootstrap recommended).

### Deliverables
- [ ] **Source Code:** `.zip` archive ready to be opened in **IntelliJ IDEA**.
- [ ] **Documentation:** `.pdf` file explaining app functionality and architecture (classes, patterns, dependencies).

---

## 📋 Detailed Requirements Breakdown

### 1. Auto-Saving & Version Control
* **Automatic Persistence:** The application must automatically save the canvas state to the server whenever an object is added or deleted (alternatively, via a background timer such as every 60 seconds).
* **Version History Storage:** Every save operation must preserve the previous state rather than overwriting it, building an incremental change history.
* **Timestamps & Current Version:** 
  * Each saved snapshot must have an associated **timestamp**.
  * The most recent save is always designated as the **"Current" (`actual`)** version.
* **Viewing & Restoring:** While viewing or editing an image, the user must be able to browse its version history and **restore** any previous version to make it active again.

### 2. Deletion & Trash Bin Management
* **Soft Delete:** When a user deletes an image from their main workspace, it is **not** removed from the database; instead, it is moved to the user's **Trash (`paperera`)**.
* **Permanent Delete & Cascade:** If the user deletes the image from the Trash, it is permanently removed from the database along with **all of its historical versions**.

### 3. Sharing, Visibility & Permissions
* **Default Privacy:** Newly created images are **private** by default (visible and editable only by the creator/owner).
* **Visibility Scopes:** A user can only view images that are:
  1. Owned by them.
  2. Set to **Public** (shared with everyone).
  3. Explicitly shared with them by another user.
* **Permission Levels:** Shared images must enforce two distinct access levels:
  * **Read (`lectura`):** User can view the image (and clone it), but cannot modify or delete it.
  * **Write (`escriptura`):** User can view, edit/modify, and delete the image.
* **Ownership Restriction:** Only the **owner (`propietari`)** of an image can configure its sharing settings. Users with shared access (even with write permissions) cannot re-share the image with others.

### 4. Image Duplication (Cloning)
* Any user can make a personal copy of any image they have access to (either their own or one shared with them).
* **Exclusion of History:** When an image is copied, **only the current version** is duplicated. The original image's version history must **not** be carried over to the copy.

### 5. Technical Stack & Architecture
* **Backend Framework:** Must be built using **Spring Web** following the methodologies covered in class.
* **Persistence Layer:** Data must be stored in a **MySQL** database so state persists across server restarts. All database queries and operations must be implemented using **`JdbcTemplate`**.
* **Session & Security:** Must utilize **`HttpSession`** to track authenticated users and ensure users can only access and manipulate drawings according to their session identity and permissions.
* **Design Patterns:** Organize the codebase cleanly using architectural and design patterns covered in class (e.g., MVC, DAO/Repository, Service Layer, DTOs).
* **Frontend & UI/UX:** Aesthetic quality and usability will be evaluated. Using **Bootstrap** is strongly recommended for a clean, responsive interface.

---

## 📦 Evaluation & Submission Guidelines

To complete the assignment, submit the following two items:
1. **IntelliJ IDEA Project (`.zip`):** A compressed archive containing the complete, runnable project structured to open directly in IntelliJ IDEA (ensure database connection properties/scripts are clear).
2. **Technical Report (`.pdf`):** An attached PDF document covering:
   * A functional walkthrough of the application (features, user flows, screenshots).
   * A detailed architectural breakdown (class diagrams/descriptions, design patterns used, database schema, and Maven/Gradle dependencies).