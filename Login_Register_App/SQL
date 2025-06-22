-- 1. Create the database
CREATE DATABASE IF NOT EXISTS sign_in_sign_up_db;

-- 2. Select the database
USE sign_in_sign_up_db;

-- 3. Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Optional: Insert a test user
INSERT INTO users (username, email, password) 
VALUES ('testuser', 'test@example.com', 'testpassword');

-- 5. Optional: Create indexes for faster queries
CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_email ON users(email);

-- 6. View table structure
DESCRIBE users;

-- 7. View sample data
SELECT * FROM users;
