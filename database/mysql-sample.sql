CREATE DATABASE IF NOT EXISTS servletjpa
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE servletjpa;

DROP TABLE IF EXISTS videos;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS categories;

CREATE TABLE categories (
    CategoryId INT AUTO_INCREMENT PRIMARY KEY,
    CategoryName VARCHAR(50) NOT NULL,
    Images VARCHAR(500) NULL,
    Status INT NULL
);

CREATE TABLE videos (
    VideoId VARCHAR(50) PRIMARY KEY,
    Active INT NULL,
    Description VARCHAR(500) NULL,
    Poster VARCHAR(500) NULL,
    Title VARCHAR(500) NULL,
    Views INT NULL,
    CategoryId INT NULL,
    CONSTRAINT FK_videos_categories
        FOREIGN KEY (CategoryId) REFERENCES categories(CategoryId)
);

CREATE TABLE users (
    UserId INT AUTO_INCREMENT PRIMARY KEY,
    FullName VARCHAR(100) NULL,
    Phone VARCHAR(20) NULL,
    Images VARCHAR(500) NULL,
    Email VARCHAR(120) NULL UNIQUE,
    PasswordHash VARCHAR(200) NULL,
    Active INT NULL,
    OtpCode VARCHAR(10) NULL,
    OtpPurpose VARCHAR(30) NULL,
    OtpExpiresAt DATETIME NULL,
    CreatedAt DATETIME NULL
);

CREATE TABLE products (
    ProductId INT AUTO_INCREMENT PRIMARY KEY,
    ProductName VARCHAR(150) NOT NULL,
    Images VARCHAR(500) NULL,
    Price DECIMAL(18,2) NULL,
    Description VARCHAR(1000) NULL,
    Status INT NULL,
    CreatedAt DATETIME NULL,
    CategoryId INT NULL,
    CONSTRAINT FK_products_categories
        FOREIGN KEY (CategoryId) REFERENCES categories(CategoryId)
);

INSERT INTO categories (CategoryName, Images, Status)
VALUES
    ('Cong nghe', 'https://picsum.photos/seed/technology/300/200', 1),
    ('Giao duc', 'https://picsum.photos/seed/education/300/200', 1),
    ('Giai tri', 'https://picsum.photos/seed/entertainment/300/200', 0);

INSERT INTO videos (VideoId, Active, Description, Poster, Title, Views, CategoryId)
VALUES
    ('java-jpa-01', 1, 'Gioi thieu JPA va Hibernate', 'https://picsum.photos/seed/jpa/300/200', 'JPA co ban', 120, 1),
    ('servlet-01', 1, 'CRUD Servlet JSP', 'https://picsum.photos/seed/servlet/300/200', 'Servlet JSP CRUD', 85, 2);

INSERT INTO products (ProductName, Images, Price, Description, Status, CreatedAt, CategoryId)
VALUES
    ('Laptop Java Pro', 'https://picsum.photos/seed/product01/500/350', 15000000, 'Laptop phuc vu hoc lap trinh Java', 1, NOW(), 1),
    ('Ban phim co', 'https://picsum.photos/seed/product02/500/350', 1200000, 'Ban phim co cho lap trinh vien', 1, DATE_SUB(NOW(), INTERVAL 1 DAY), 1),
    ('Chuot khong day', 'https://picsum.photos/seed/product03/500/350', 450000, 'Chuot gon nhe', 1, DATE_SUB(NOW(), INTERVAL 2 DAY), 1),
    ('Man hinh 24 inch', 'https://picsum.photos/seed/product04/500/350', 3200000, 'Man hinh lam viec va hoc tap', 1, DATE_SUB(NOW(), INTERVAL 3 DAY), 1),
    ('Khoa hoc Servlet JSP', 'https://picsum.photos/seed/product05/500/350', 799000, 'Khoa hoc web Java co ban', 1, DATE_SUB(NOW(), INTERVAL 4 DAY), 2),
    ('Sach JPA Hibernate', 'https://picsum.photos/seed/product06/500/350', 250000, 'Tai lieu hoc JPA Hibernate', 1, DATE_SUB(NOW(), INTERVAL 5 DAY), 2),
    ('Webcam hoc online', 'https://picsum.photos/seed/product07/500/350', 650000, 'Webcam full HD', 1, DATE_SUB(NOW(), INTERVAL 6 DAY), 2),
    ('Tai nghe hoc tap', 'https://picsum.photos/seed/product08/500/350', 500000, 'Tai nghe co microphone', 1, DATE_SUB(NOW(), INTERVAL 7 DAY), 2),
    ('Loa bluetooth', 'https://picsum.photos/seed/product09/500/350', 900000, 'Loa giai tri nho gon', 1, DATE_SUB(NOW(), INTERVAL 8 DAY), 3),
    ('Den LED ban hoc', 'https://picsum.photos/seed/product10/500/350', 350000, 'Den LED tiet kiem dien', 1, DATE_SUB(NOW(), INTERVAL 9 DAY), 2),
    ('USB 64GB', 'https://picsum.photos/seed/product11/500/350', 180000, 'USB luu tru bai tap', 1, DATE_SUB(NOW(), INTERVAL 10 DAY), 1),
    ('Balo laptop', 'https://picsum.photos/seed/product12/500/350', 600000, 'Balo chong soc laptop', 1, DATE_SUB(NOW(), INTERVAL 11 DAY), 1);
