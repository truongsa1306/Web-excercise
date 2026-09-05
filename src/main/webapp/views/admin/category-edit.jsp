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

        <img class="thumb" src="${imgUrl}" alt="${fn:escapeXml(cate.categoryname)}">

        <div class="field">
            <label for="images1">Upload images</label>
            <input type="file" id="images1" name="images1" accept="image/*">
        </div>

        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="status" value="1" ${cate.status == 1 ? 'checked' : ''}> Hoat dong</label>
                <label><input type="radio" name="status" value="0" ${cate.status != 1 ? 'checked' : ''}> Khoa</label>
            </div>
            <c:if test="${not empty errors.status}">
                <span class="field-error">${errors.status}</span>
            </c:if>
        </div>

        <button type="submit">Update</button>
    </form>
</main>
</body>
</html>
