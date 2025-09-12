# Inventory Management System

A full-stack database application designed for retail inventory tracking and management. Built collaboratively by a team of four students as part of academic coursework, demonstrating both technical implementation and project coordination skills.

## 🚀 Project Overview

This application provides comprehensive CRUD (Create, Read, Update, Delete) operations for managing retail inventory through an intuitive Java Swing interface connected to a MySQL database backend.

## 💻 Technologies Used

- **Frontend:** Java Swing (GUI Framework)
- **Backend:** Java (Core Application Logic)
- **Database:** MySQL
- **Connectivity:** JDBC (Java Database Connectivity)
- **Development Tools:** IntelliJ IDEA
- **Database Design:** ER Diagrams, Relational Schema

## ✨ Key Features

- **Complete CRUD Operations:** Add, view, update, and delete inventory items
- **Database Integration:** Seamless connection between GUI and MySQL database
- **Data Validation:** Input validation and error handling for data integrity
- **User-Friendly Interface:** Intuitive Java Swing GUI for easy navigation
- **Professional Database Design:** Normalized database schema with proper relationships
- **Team Collaboration:** Coordinated development across four team members

## 🏗️ System Architecture

```
[Java Swing GUI] ←→ [Application Logic] ←→ [JDBC] ←→ [MySQL Database]
```

**Database Schema:**
- Designed with ER diagrams following normalization principles
- Implemented relational schema with proper foreign key relationships
- Optimized for inventory tracking and reporting

## 🎯 My Role & Contributions

- **Team Leader:** Coordinated development efforts across four team members
- **Database Architect:** Led database design and schema implementation
- **Integration Specialist:** Implemented JDBC connectivity between frontend and backend
- **Project Manager:** Organized final presentation showcasing system capabilities

## 📋 What I Learned

**Technical Skills:**
- Database design principles and normalization
- Java Swing GUI development and event handling
- JDBC programming and database connectivity
- SQL query optimization and data management

**Soft Skills:**
- Team leadership and coordination
- Cross-functional collaboration between UI/UX and database teams
- Project planning and milestone management
- Professional presentation and demonstration skills

## 🔧 Setup & Installation

1. **Prerequisites:**
   - Java JDK 8 or higher
   - MySQL Server
   - JDBC MySQL Connector

2. **Database Setup:**
   ```sql
   CREATE DATABASE inventory_management;
   -- Import provided schema and sample data
   ```

3. **Application Configuration:**
   ```java
   // Update database connection settings in config file
   String url = "jdbc:mysql://localhost:3306/inventory_management";
   String username = "your_username";
   String password = "your_password";
   ```

4. **Run Application:**
   ```bash
   javac -cp ".:mysql-connector-java.jar" *.java
   java -cp ".:mysql-connector-java.jar" MainApplication
   ```


## 🎪 Final Presentation

Successfully delivered comprehensive project presentation demonstrating:
- System architecture and design decisions
- Live demonstration of all CRUD operations
- Database integrity and performance optimization
- Team collaboration and development process

## 🚧 Future Enhancements

- Web-based interface using modern frameworks
- RESTful API development for mobile integration
- Advanced reporting and analytics features
- User authentication and role-based access control

## 👥 Team Credits

This project was developed collaboratively by a team of four BTech IT students at Kwantlen Polytechnic University. My role focused on team coordination, database design, and system integration.

---

