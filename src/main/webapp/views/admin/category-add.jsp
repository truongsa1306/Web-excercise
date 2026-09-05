<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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
            <input class="${not empty errors.categoryname ? 'invalid' : ''}" type="text" id="categoryname"
                   name="categoryname" value="${fn:escapeXml(cate.categoryname)}" maxlength="50" required>
            <c:if test="${not empty errors.categoryname}">
                <span class="field-error">${errors.categoryname}</span>
            </c:if>
        </div>

        <div class="field">
            <label for="images">Link images</label>
            <input class="${not empty errors.images ? 'invalid' : ''}" type="text" id="images" name="images"
                   value="${fn:escapeXml(cate.images)}" maxlength="500">
            <c:if test="${not empty errors.images}">
                <span class="field-error">${errors.images}</span>
            </c:if>
        </div>

        <div class="field">
            <label for="images1">Upload images</label>
            <input type="file" id="images1" name="images1" accept="image/*">
        </div>

        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="status" value="1" ${empty cate or cate.status == 1 ? 'checked' : ''}> Hoat dong</label>
                <label><input type="radio" name="status" value="0" ${not empty cate and cate.status == 0 ? 'checked' : ''}> Khoa</label>
            </div>
            <c:if test="${not empty errors.status}">
                <span class="field-error">${errors.status}</span>
            </c:if>
        </div>

        <button type="submit">Insert</button>
    </form>
</main>
</body>
</html>
