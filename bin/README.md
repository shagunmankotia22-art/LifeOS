# LifeOS – Personal Productivity & Task Management System

LifeOS is a Java-based console application designed to help users manage their personal productivity.

## Technologies Used

- Java 21
- Eclipse IDE
- Maven
- MySQL 8.0
- JDBC
- MySQL Connector/J
- Git and GitHub

## Features

- User registration and login
- Secure password hashing
- MySQL database connectivity
- Multi-user support
- Task management
- Habit tracking
- Productivity analytics

## Project Structure

```text
LifeOS/
├── src/
│   ├── main/java/com/lifeos/
│   │   ├── model/
│   │   ├── database/
│   │   ├── dao/
│   │   ├── service/
│   │   ├── menu/
│   │   └── utilities/
│   └── test/java/
├── pom.xml
├── .gitignore
└── README.md
```

## Database Setup

1. Install MySQL Server 8.0.
2. Open MySQL Workbench.
3. Create the database:

```sql
CREATE DATABASE lifeos_db;
```

4. Create the required tables using the SQL setup script shared with the project.

## Database Configuration

LifeOS uses environment variables to protect database credentials.

Before running the application, configure these two environment variables on your computer:

| Variable | Value |
|---|---|
| `DB_USER` | Your MySQL username (for example, root) |
| `DB_PASSWORD` | Your MySQL password |

**Important:** Never add your actual MySQL password to Java source code or commit it to GitHub.

### Windows Environment Variables

1. Search for **Edit the system environment variables**.
2. Open **Environment Variables**.
3. Under User Variables, click New.
4. Add `DB_USER` and your MySQL username.
5. Add `DB_PASSWORD` and your MySQL password.
6. Restart Eclipse.

## Running the Project

1. Clone or download the LifeOS repository.
2. Open the project in Eclipse as an existing Maven project.
3. Configure the database and environment variables.
4. Update Maven dependencies.
5. Run the required Java class.

## Team Development

This project is developed by a team of three members.

Each member is responsible for a separate module, with all modules integrated into one application.

## Project Status

Currently implemented:
- Maven project setup
- MySQL database configuration
- JDBC connection
- User model
- Password hashing
- User registration and login
- Authentication testing