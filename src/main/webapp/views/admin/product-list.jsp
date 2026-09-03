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
            <a class="secondary" href="<c:url value='/admin/categories'/>">Categories</a>
            <a class="secondary" href="<c:url value='/admin/videos'/>">Videos</a>
            <a href="<c:url value='/admin/products'/>">Products</a>
        </nav>
    </div>

    <div class="toolbar">
        <a class="button" href="<c:url value='/admin/product/add'/>">Add Product</a>
    </div>

    <table>
        <thead>
        <tr>
            <th>STT</th>
            <th>Images</th>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${products}" var="product" varStatus="status">
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
            <tr>
                <td>${status.index + 1}</td>
                <td><img class="thumb" src="${imgUrl}" alt="${product.productName}"></td>
                <td>${product.productName}</td>
                <td>${empty product.category ? '' : product.category.categoryname}</td>
                <td>${product.price}</td>
                <td>${product.status == 1 ? 'Active' : 'Locked'}</td>
                <td>
                    <div class="actions">
                        <a href="<c:url value='/admin/product/edit?id=${product.productId}'/>">Edit</a>
                        <a href="<c:url value='/admin/product/delete?id=${product.productId}'/>"
                           onclick="return confirm('Delete this product?')">Delete</a>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
</body>
</html>
