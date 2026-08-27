<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Add Category</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Add Category</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/categories'/>">Back</a>
        </nav>
    </div>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form class="form" action="<c:url value='/admin/category/insert'/>" method="post" enctype="multipart/form-data">
        <div class="field">
            <label for="categoryname">Category name</label>
            <input type="text" id="categoryname" name="categoryname" required>
        </div>

        <div class="field">
            <label for="images">Link images</label>
            <input type="text" id="images" name="images">
        </div>

        <div class="field">
            <label for="images1">Upload images</label>
            <input type="file" id="images1" name="images1" accept="image/*">
        </div>

        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="status" value="1" checked> Hoạt động</label>
                <label><input type="radio" name="status" value="0"> Khóa</label>
            </div>
        </div>

        <button type="submit">Insert</button>
    </form>
</main>
</body>
</html>
