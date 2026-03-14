-- EventPro Database Setup Script
-- Run this in MySQL Workbench or MySQL command line

CREATE DATABASE IF NOT EXISTS event_management;

USE event_management;

CREATE TABLE IF NOT EXISTS users (
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(100) NOT NULL
);

-- Default admin account
INSERT INTO users(username, password) VALUES('admin', 'admin123')
ON DUPLICATE KEY UPDATE username=username;

CREATE TABLE IF NOT EXISTS events (
    event_id     INT AUTO_INCREMENT PRIMARY KEY,
    event_name   VARCHAR(100) NOT NULL,
    organizer    VARCHAR(100),
    venue        VARCHAR(100),
    event_date   DATE,
    participants INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS participants (
    participant_id INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    email          VARCHAR(100),
    event_id       INT,
    FOREIGN KEY (event_id) REFERENCES events(event_id) ON DELETE CASCADE
);

-- Sample data
INSERT INTO events(event_name, organizer, venue, event_date, participants) VALUES
('Annual Tech Fest 2025',  'CS Department',    'Main Auditorium',   '2025-08-15', 300),
('Cultural Night',         'Arts Club',         'Open Air Theatre',  '2025-09-20', 500),
('Hackathon 2025',         'Innovation Cell',  'Lab Block A',       '2025-10-05', 150);
