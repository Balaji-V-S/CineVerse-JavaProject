# CineVerse: Core Java Movie Booking System

A **command-line based movie booking and review system** built with **Core Java, JPA, and Hibernate**, designed to showcase a robust **three-layered architecture** without the use of frameworks like Spring.

---

# About The Project
CineVerse serves as a **practical, hands-on guide** to building backend applications from the ground up.  

It focuses on mastering fundamental concepts such as:
- Object-Oriented Programming
- Data persistence with **JPA/Hibernate**
- Clean architectural design

By **intentionally avoiding frameworks**, the project highlights the **core mechanics** of:
- Transaction management
- Dependency handling
- Separation of concerns  

This makes it an **ideal learning resource** for developers looking to strengthen their foundational Java skills.

The application simulates a **real-world movie platform**, allowing users to:
- Register and log in
- Book tickets for movies
- Write and view reviews  
with distinct roles for **Customers** and **Administrators**.

---

## Key Features

- **Role-Based User Management**  
  Clear distinction between **Admin** and **Customer** roles.

- **Full CRUD Functionality**  
  Admins can create, read, update, and delete movies & showtimes.

- **Ticket Booking System**  
  Customers can view movies, book tickets, and manage seat availability.

- **Movie Review & Rating System**  
  Customers can write reviews, rate movies, and see recalculated average ratings.

- **Custom Exception Handling**  
  Business rules enforced through a dedicated exception hierarchy (e.g., `SeatsUnavailableException`, `DuplicateReviewException`).

- **JPA-Based Persistence**  
  All data persisted to a **MySQL database** using **Hibernate ORM**.

---

## Architecture

The project follows a **classic three-layered architecture** ensuring modularity, scalability, and maintainability.
```code
+-----------------------------------+
|      Presentation Layer           |
|    (CineVerseConsoleApp.java)     |
|    Handles User Input & Output    |
+-----------------------------------+
                |
                v
+-----------------------------------+
|         Service Layer             |
| (UserService, MovieService, etc.) |
|   Business Logic & Transactions   |
+-----------------------------------+
                |
                v
+-----------------------------------+
|       Data Access Layer           |
|   DAO Interfaces & Impl Classes   |
|   Database Interactions via JPA   |
+-----------------------------------+
```
---

# Technology Stack

- **Language:** Java 11+  
- **Build Tool:** Apache Maven  
- **Persistence:** JPA & Hibernate ORM  
- **Database:** MySQL  
- **Utilities:** Project Lombok  

---

## Getting Started

### ✅ Prerequisites
Make sure you have installed:
- JDK 21 or newer  
- Apache Maven  
- MySQL Server  

### Installation & Setup

**Clone the Repository**
   ```bash
   git clone https://github.com/Balaji-V-S/CineVerse-JavaProject.git
   cd CineVerse
   ```

Distributed under the MIT License. See LICENSE for details.

