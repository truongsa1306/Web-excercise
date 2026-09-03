<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Edit Product</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Edit Product</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/products'/>">Back</a>
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

    <form class="form" action="<c:url value='/admin/product/update'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="productId" value="${product.productId}">
        <div class="field">
            <label for="productName">Product name</label>
            <input type="text" id="productName" name="productName" value="${product.productName}" required>
        </div>
        <div class="field">
            <label for="categoryId">Category</label>
            <select id="categoryId" name="categoryId">
                <option value="">-- Select category --</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.categoryid}" ${not empty product.category and product.category.categoryid == category.categoryid ? 'selected' : ''}>
                            ${category.categoryname}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label for="price">Price</label>
            <input type="number" id="price" name="price" min="0" step="0.01" value="${product.price}">
        </div>
        <div class="field">
            <label for="description">Description</label>
            <textarea id="description" name="description">${product.description}</textarea>
        </div>
        <div class="field">
            <label for="images">Image link</label>
            <input type="text" id="images" name="images" value="${product.images}">
        </div>
        <img class="thumb" src="${imgUrl}" alt="${product.productName}">
        <div class="field">
            <label for="imageFile">Upload image</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*">
        </div>
        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="status" value="1" ${product.status == 1 ? 'checked' : ''}> Active</label>
                <label><input type="radio" name="status" value="0" ${product.status != 1 ? 'checked' : ''}> Locked</label>
            </div>
        </div>
        <button type="submit">Update</button>
    </form>
</main>
</body>
</html>
