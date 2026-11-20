USE lifeos_db;

-- 1. USERS TABLE
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. TASKS TABLE
CREATE TABLE tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    priority ENUM('HIGH', 'MEDIUM', 'LOW') DEFAULT 'MEDIUM',
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED') DEFAULT 'PENDING',
    due_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- 3. HABITS TABLE
CREATE TABLE habits (
    habit_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    habit_name VARCHAR(150) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

-- 4. HABIT RECORDS TABLE
CREATE TABLE habit_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    habit_id INT NOT NULL,
    completion_date DATE NOT NULL,
    completed BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (habit_id) REFERENCES habits(habit_id)
        ON DELETE CASCADE,
    UNIQUE (habit_id, completion_date)
);

-- 5. PRODUCTIVITY LOGS TABLE
CREATE TABLE productivity_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    activity VARCHAR(255) NOT NULL,
    log_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);