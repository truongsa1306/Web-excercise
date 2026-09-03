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

IF OBJECT_ID(N'dbo.products', N'U') IS NOT NULL
BEGIN
    DROP TABLE dbo.products;
END
GO

IF OBJECT_ID(N'dbo.users', N'U') IS NOT NULL
BEGIN
    DROP TABLE dbo.users;
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

CREATE TABLE dbo.users (
    UserId INT IDENTITY(1,1) PRIMARY KEY,
    FullName NVARCHAR(100) NULL,
    Phone NVARCHAR(20) NULL,
    Images NVARCHAR(500) NULL,
    Email NVARCHAR(120) NULL UNIQUE,
    PasswordHash NVARCHAR(200) NULL,
    Active INT NULL,
    OtpCode NVARCHAR(10) NULL,
    OtpPurpose NVARCHAR(30) NULL,
    OtpExpiresAt DATETIME2 NULL,
    CreatedAt DATETIME2 NULL
);
GO

CREATE TABLE dbo.products (
    ProductId INT IDENTITY(1,1) PRIMARY KEY,
    ProductName NVARCHAR(150) NOT NULL,
    Images NVARCHAR(500) NULL,
    Price DECIMAL(18,2) NULL,
    Description NVARCHAR(1000) NULL,
    Status INT NULL,
    CreatedAt DATETIME2 NULL,
    CategoryId INT NULL,
    CONSTRAINT FK_products_categories
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

INSERT INTO dbo.products (ProductName, Images, Price, Description, Status, CreatedAt, CategoryId)
VALUES
    (N'Laptop Java Pro', N'https://picsum.photos/seed/product01/500/350', 15000000, N'Laptop phuc vu hoc lap trinh Java', 1, SYSDATETIME(), 1),
    (N'Ban phim co', N'https://picsum.photos/seed/product02/500/350', 1200000, N'Ban phim co cho lap trinh vien', 1, DATEADD(day, -1, SYSDATETIME()), 1),
    (N'Chuot khong day', N'https://picsum.photos/seed/product03/500/350', 450000, N'Chuot gon nhe', 1, DATEADD(day, -2, SYSDATETIME()), 1),
    (N'Man hinh 24 inch', N'https://picsum.photos/seed/product04/500/350', 3200000, N'Man hinh lam viec va hoc tap', 1, DATEADD(day, -3, SYSDATETIME()), 1),
    (N'Khoa hoc Servlet JSP', N'https://picsum.photos/seed/product05/500/350', 799000, N'Khoa hoc web Java co ban', 1, DATEADD(day, -4, SYSDATETIME()), 2),
    (N'Sach JPA Hibernate', N'https://picsum.photos/seed/product06/500/350', 250000, N'Tai lieu hoc JPA Hibernate', 1, DATEADD(day, -5, SYSDATETIME()), 2),
    (N'Webcam hoc online', N'https://picsum.photos/seed/product07/500/350', 650000, N'Webcam full HD', 1, DATEADD(day, -6, SYSDATETIME()), 2),
    (N'Tai nghe hoc tap', N'https://picsum.photos/seed/product08/500/350', 500000, N'Tai nghe co microphone', 1, DATEADD(day, -7, SYSDATETIME()), 2),
    (N'Loa bluetooth', N'https://picsum.photos/seed/product09/500/350', 900000, N'Loa giai tri nho gon', 1, DATEADD(day, -8, SYSDATETIME()), 3),
    (N'Den LED ban hoc', N'https://picsum.photos/seed/product10/500/350', 350000, N'Den LED tiet kiem dien', 1, DATEADD(day, -9, SYSDATETIME()), 2),
    (N'USB 64GB', N'https://picsum.photos/seed/product11/500/350', 180000, N'USB luu tru bai tap', 1, DATEADD(day, -10, SYSDATETIME()), 1),
    (N'Balo laptop', N'https://picsum.photos/seed/product12/500/350', 600000, N'Balo chong soc laptop', 1, DATEADD(day, -11, SYSDATETIME()), 1);
GO
