CREATE DATABASE IF NOT EXISTS servletjpa
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE servletjpa;

DROP TABLE IF EXISTS videos;
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

INSERT INTO categories (CategoryName, Images, Status)
VALUES
    ('Cong nghe', 'https://picsum.photos/seed/technology/300/200', 1),
    ('Giao duc', 'https://picsum.photos/seed/education/300/200', 1),
    ('Giai tri', 'https://picsum.photos/seed/entertainment/300/200', 0);

INSERT INTO videos (VideoId, Active, Description, Poster, Title, Views, CategoryId)
VALUES
    ('java-jpa-01', 1, 'Gioi thieu JPA va Hibernate', 'https://picsum.photos/seed/jpa/300/200', 'JPA co ban', 120, 1),
    ('servlet-01', 1, 'CRUD Servlet JSP', 'https://picsum.photos/seed/servlet/300/200', 'Servlet JSP CRUD', 85, 2);
