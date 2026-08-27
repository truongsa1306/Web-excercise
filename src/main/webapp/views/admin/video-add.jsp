<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Add Video</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Add Video</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/videos'/>">Back</a>
        </nav>
    </div>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form class="form" action="<c:url value='/admin/video/insert'/>" method="post" enctype="multipart/form-data">
        <div class="field">
            <label for="videoId">Video ID</label>
            <input type="text" id="videoId" name="videoId" required>
        </div>

        <div class="field">
            <label for="title">Title</label>
            <input type="text" id="title" name="title" required>
        </div>

        <div class="field">
            <label for="categoryId">Category</label>
            <select id="categoryId" name="categoryId">
                <option value="">-- Select category --</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.categoryid}">${category.categoryname}</option>
                </c:forEach>
            </select>
        </div>

        <div class="field">
            <label for="description">Description</label>
            <textarea id="description" name="description"></textarea>
        </div>

        <div class="field">
            <label for="poster">Poster link</label>
            <input type="text" id="poster" name="poster">
        </div>

        <div class="field">
            <label for="posterFile">Upload poster</label>
            <input type="file" id="posterFile" name="posterFile" accept="image/*">
        </div>

        <div class="field">
            <label for="views">Views</label>
            <input type="number" id="views" name="views" min="0" value="0">
        </div>

        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="active" value="1" checked> Hoạt động</label>
                <label><input type="radio" name="active" value="0"> Khóa</label>
            </div>
        </div>

        <button type="submit">Insert</button>
    </form>
</main>
</body>
</html>
