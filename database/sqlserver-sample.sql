IF DB_ID(N'jakartaJPA') IS NULL
BEGIN
    CREATE DATABASE jakartaJPA;
END
GO

USE jakartaJPA;
GO

IF OBJECT_ID(N'dbo.videos', N'U') IS NOT NULL
BEGIN
    DROP TABLE dbo.videos;
END
GO

IF OBJECT_ID(N'dbo.categories', N'U') IS NOT NULL
BEGIN
    DROP TABLE dbo.categories;
END
GO

CREATE TABLE dbo.categories (
    CategoryId INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName NVARCHAR(50) NOT NULL,
    Images NVARCHAR(500) NULL,
    Status INT NULL
);
GO

CREATE TABLE dbo.videos (
    VideoId NVARCHAR(50) PRIMARY KEY,
    Active INT NULL,
    Description NVARCHAR(500) NULL,
    Poster NVARCHAR(500) NULL,
    Title NVARCHAR(500) NULL,
    Views INT NULL,
    CategoryId INT NULL,
    CONSTRAINT FK_videos_categories
        FOREIGN KEY (CategoryId) REFERENCES dbo.categories(CategoryId)
);
GO

INSERT INTO dbo.categories (CategoryName, Images, Status)
VALUES
    (N'Cong nghe', N'https://picsum.photos/seed/technology/300/200', 1),
    (N'Giao duc', N'https://picsum.photos/seed/education/300/200', 1),
    (N'Giai tri', N'https://picsum.photos/seed/entertainment/300/200', 0);
GO

INSERT INTO dbo.videos (VideoId, Active, Description, Poster, Title, Views, CategoryId)
VALUES
    (N'java-jpa-01', 1, N'Gioi thieu JPA va Hibernate', N'https://picsum.photos/seed/jpa/300/200', N'JPA co ban', 120, 1),
    (N'servlet-01', 1, N'CRUD Servlet JSP', N'https://picsum.photos/seed/servlet/300/200', N'Servlet JSP CRUD', 85, 2);
GO
