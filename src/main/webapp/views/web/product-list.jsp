<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Products</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Products</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/'/>">Home</a>
        </nav>
    </div>

    <div class="product-grid">
        <c:forEach items="${products}" var="product">
            <%@ include file="/views/web/product-card.jspf" %>
        </c:forEach>
    </div>

    <c:if test="${totalPages > 1}">
        <nav class="pagination" aria-label="Product pages">
            <c:forEach begin="1" end="${totalPages}" var="page">
                <a class="${page == currentPage ? 'active' : ''}" href="<c:url value='/product?page=${page}'/>">${page}</a>
            </c:forEach>
        </nav>
    </c:if>
</main>
</body>
</html>
