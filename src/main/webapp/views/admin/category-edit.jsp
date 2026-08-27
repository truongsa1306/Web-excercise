<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Edit Category</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Edit Category</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/categories'/>">Back</a>
        </nav>
    </div>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <c:set var="imageValue" value="${cate.images}"/>
    <c:choose>
        <c:when test="${not empty imageValue and (fn:startsWith(imageValue, 'http://') or fn:startsWith(imageValue, 'https://'))}">
            <c:set var="imgUrl" value="${imageValue}"/>
        </c:when>
        <c:otherwise>
            <c:url value="/image" var="imgUrl">
                <c:param name="fname" value="${imageValue}"/>
            </c:url>
        </c:otherwise>
    </c:choose>

    <form class="form" action="<c:url value='/admin/category/update'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="categoryid" value="${cate.categoryid}">

        <div class="field">
            <label for="categoryname">Category name</label>
            <input type="text" id="categoryname" name="categoryname" value="${cate.categoryname}" required>
        </div>

        <div class="field">
            <label for="images">Link images</label>
            <input type="text" id="images" name="images" value="${cate.images}">
        </div>

        <img class="thumb" src="${imgUrl}" alt="${cate.categoryname}">

        <div class="field">
            <label for="images1">Upload images</label>
            <input type="file" id="images1" name="images1" accept="image/*">
        </div>

        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="status" value="1" ${cate.status == 1 ? 'checked' : ''}> Hoạt động</label>
                <label><input type="radio" name="status" value="0" ${cate.status != 1 ? 'checked' : ''}> Khóa</label>
            </div>
        </div>

        <button type="submit">Update</button>
    </form>
</main>
</body>
</html>
