# EventPro — Event Management System
### Java Swing + MySQL | College DBMS Mini Project

---

## ✨ Features
- 🔐 Login System (username + password)
- 📊 Dashboard with live statistics
- ➕ Add / ✏️ Edit / 🗑️ Delete Events
- 👤 Register Participants (with event dropdown)
- 📋 View Events in styled JTable
- 🎨 Modern dark UI theme

---

## 🗂 Project Structure

```
EventManagementSystem/
├── src/
│   ├── DBConnection.java        ← MySQL connection
│   ├── Event.java               ← Event model
│   ├── EventDAO.java            ← DB operations (CRUD)
│   ├── UITheme.java             ← Shared UI theme & components
│   ├── LoginForm.java           ← Login screen
│   ├── Dashboard.java           ← Main dashboard with sidebar
│   ├── AddEventForm.java        ← Add & Edit event dialog
│   ├── ViewEvents.java          ← Table view with Edit/Delete
│   └── RegisterParticipant.java ← Register participant dialog
├── database_setup.sql           ← MySQL setup script
└── README.md
```

---

## ⚙️ Setup Instructions

### Step 1 — MySQL Database

Open **MySQL Workbench** or **MySQL command line** and run:

```bash
mysql -u root -p < database_setup.sql
```

Or paste the contents of `database_setup.sql` directly.

---

### Step 2 — Configure DB Password

Open `src/DBConnection.java` and change line:

```java
private static final String PASSWORD = "password"; // ← your MySQL password
```

---

### Step 3 — Download MySQL Connector JAR

Download from: https://dev.mysql.com/downloads/connector/j/

Choose: **Platform Independent → ZIP Archive**

Extract and find: `mysql-connector-j-x.x.x.jar`

---

### Step 4 — Compile & Run

#### Using VS Code:
1. Add the JAR to your Java Build Path
2. Open `src/LoginForm.java`
3. Press `F5` to run

#### Using Command Line:
```bash
cd EventManagementSystem/src

# Compile (replace path to your JAR)
javac -cp ".;path/to/mysql-connector-j.jar" *.java

# Run
java -cp ".;path/to/mysql-connector-j.jar" LoginForm
```

---

## 🔑 Default Login

| Field    | Value      |
|----------|------------|
| Username | `admin`    |
| Password | `admin123` |

---

## 🖥️ UI Flow

```
Login Screen
     │
     ▼
Dashboard (sidebar navigation)
     │
     ├──▶ Add Event    → Dialog to add new event
     ├──▶ View Events  → Table with Edit / Delete buttons
     └──▶ Add Participant → Dropdown to select event
```

---

## 🔧 Troubleshooting

| Problem | Fix |
|---|---|
| `Database connection failed` | Check MySQL is running, password is correct |
| `Class not found: com.mysql.cj.jdbc.Driver` | Add MySQL Connector JAR to classpath |
| `Table doesn't exist` | Run `database_setup.sql` first |

---

## 📚 Tech Stack
- **Language**: Java 11+
- **UI**: Java Swing (custom dark theme)
- **Database**: MySQL 8.x
- **Driver**: MySQL Connector/J

---

*Built as a DBMS mini-project. Ready for viva demonstration.*
