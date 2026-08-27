<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Categories</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Categories</h1>
        <nav class="nav">
            <a href="<c:url value='/admin/categories'/>">Categories</a>
            <a class="secondary" href="<c:url value='/admin/videos'/>">Videos</a>
        </nav>
    </div>

    <div class="toolbar">
        <a class="button" href="<c:url value='/admin/category/add'/>">Add Category</a>
        <form class="search" action="<c:url value='/admin/categories'/>" method="get">
            <input type="text" name="keyword" value="${fn:escapeXml(keyword)}" placeholder="Search category name">
            <button type="submit">Search</button>
        </form>
    </div>

    <table>
        <thead>
        <tr>
            <th>STT</th>
            <th>Images</th>
            <th>Category name</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listcate}" var="cate" varStatus="status">
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
            <tr>
                <td>${status.index + 1}</td>
                <td><img class="thumb" src="${imgUrl}" alt="${cate.categoryname}"></td>
                <td>${cate.categoryname}</td>
                <td>
                    <c:choose>
                        <c:when test="${cate.status == 1}">Hoạt động</c:when>
                        <c:otherwise>Khóa</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <div class="actions">
                        <a href="<c:url value='/admin/category/edit?id=${cate.categoryid}'/>">Sửa</a>
                        <a href="<c:url value='/admin/category/delete?id=${cate.categoryid}'/>"
                           onclick="return confirm('Xóa danh mục này?')">Xóa</a>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
</body>
</html>
