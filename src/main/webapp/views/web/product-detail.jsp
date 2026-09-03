<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${product.productName}</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>${product.productName}</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/product'/>">Products</a>
        </nav>
    </div>

    <c:set var="imageValue" value="${product.images}"/>
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

    <section class="product-detail">
        <img class="product-detail__image" src="${imgUrl}" alt="${product.productName}">
        <div class="product-detail__body">
            <p class="product-category">${empty product.category ? '' : product.category.categoryname}</p>
            <p class="product-price">${product.price}</p>
            <p>${product.description}</p>
        </div>
    </section>
</main>
</body>
</html>
