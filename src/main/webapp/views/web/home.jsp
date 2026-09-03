<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Latest Products</h1>
        <nav class="nav">
            <a href="<c:url value='/product'/>">View all</a>
        </nav>
    </div>

    <div class="product-grid">
        <c:forEach items="${products}" var="product">
            <%@ include file="/views/web/product-card.jspf" %>
        </c:forEach>
    </div>
</main>
</body>
</html>
