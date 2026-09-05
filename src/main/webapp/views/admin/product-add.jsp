<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Add Product</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Add Product</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/products'/>">Back</a>
        </nav>
    </div>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form class="form" action="<c:url value='/admin/product/insert'/>" method="post" enctype="multipart/form-data">
        <div class="field">
            <label for="productName">Product name</label>
            <input class="${not empty errors.productName ? 'invalid' : ''}" type="text" id="productName"
                   name="productName" value="${fn:escapeXml(product.productName)}" maxlength="150" required>
            <c:if test="${not empty errors.productName}">
                <span class="field-error">${errors.productName}</span>
            </c:if>
        </div>
        <div class="field">
            <label for="categoryId">Category</label>
            <select class="${not empty errors.categoryId ? 'invalid' : ''}" id="categoryId" name="categoryId" required>
                <option value="">-- Select category --</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.categoryid}" ${not empty product.category and product.category.categoryid == category.categoryid ? 'selected' : ''}>
                            ${category.categoryname}
                    </option>
                </c:forEach>
            </select>
            <c:if test="${not empty errors.categoryId}">
                <span class="field-error">${errors.categoryId}</span>
            </c:if>
        </div>
        <div class="field">
            <label for="price">Price</label>
            <input class="${not empty errors.price ? 'invalid' : ''}" type="number" id="price" name="price"
                   min="0" step="0.01" value="${empty priceValue ? '0' : fn:escapeXml(priceValue)}" required>
            <c:if test="${not empty errors.price}">
                <span class="field-error">${errors.price}</span>
            </c:if>
        </div>
        <div class="field">
            <label for="description">Description</label>
            <textarea class="${not empty errors.description ? 'invalid' : ''}" id="description" name="description"
                      maxlength="1000">${fn:escapeXml(product.description)}</textarea>
            <c:if test="${not empty errors.description}">
                <span class="field-error">${errors.description}</span>
            </c:if>
        </div>
        <div class="field">
            <label for="images">Image link</label>
            <input class="${not empty errors.images ? 'invalid' : ''}" type="text" id="images" name="images"
                   value="${fn:escapeXml(product.images)}" maxlength="500">
            <c:if test="${not empty errors.images}">
                <span class="field-error">${errors.images}</span>
            </c:if>
        </div>
        <div class="field">
            <label for="imageFile">Upload image</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*">
        </div>
        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="status" value="1" ${empty product or product.status == 1 ? 'checked' : ''}> Active</label>
                <label><input type="radio" name="status" value="0" ${not empty product and product.status == 0 ? 'checked' : ''}> Locked</label>
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
