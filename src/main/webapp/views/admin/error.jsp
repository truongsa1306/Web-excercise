<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Error</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/categories'/>">Categories</a>
            <a class="secondary" href="<c:url value='/admin/videos'/>">Videos</a>
        </nav>
    </div>

    <div class="error">
        <c:choose>
            <c:when test="${not empty error}">${error}</c:when>
            <c:otherwise>Thao tác không thành công.</c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>
