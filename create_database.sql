CREATE DATABASE IF NOT EXISTS jobmanager CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- user表
CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    phone_number VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    create_time DATETIME NOT NULL,
    permission INT NOT NULL COMMENT '0-学生，1-兼职发布者，2-管理员'
);

-- 创建job表
CREATE TABLE IF NOT EXISTS job (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    publisher_name VARCHAR(100) NOT NULL,
    salary INT NOT NULL,
    create_time DATETIME NOT NULL,
    update_time DATETIME NOT NULL
);

-- 创建wish_job表
CREATE TABLE IF NOT EXISTS wish_job (
    id INT AUTO_INCREMENT PRIMARY KEY,
    job_name VARCHAR(100) NOT NULL,
    user_name VARCHAR(100) NOT NULL,
    publisher_name VARCHAR(100) NOT NULL,
    salary INT NOT NULL,
    status INT NOT NULL COMMENT '0-审核中，1-通过'
);

-- 创建register_user表
CREATE TABLE IF NOT EXISTS register_user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    publisher_name VARCHAR(100) NOT NULL,
    job_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    create_time DATETIME NOT NULL
);