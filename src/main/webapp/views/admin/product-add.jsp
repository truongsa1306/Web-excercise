<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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

    <form class="form" action="<c:url value='/admin/product/insert'/>" method="post" enctype="multipart/form-data">
        <div class="field">
            <label for="productName">Product name</label>
            <input type="text" id="productName" name="productName" required>
        </div>
        <div class="field">
            <label for="categoryId">Category</label>
            <select id="categoryId" name="categoryId">
                <option value="">-- Select category --</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.categoryid}">${category.categoryname}</option>
                </c:forEach>
            </select>
        </div>
        <div class="field">
            <label for="price">Price</label>
            <input type="number" id="price" name="price" min="0" step="0.01" value="0">
        </div>
        <div class="field">
            <label for="description">Description</label>
            <textarea id="description" name="description"></textarea>
        </div>
        <div class="field">
            <label for="images">Image link</label>
            <input type="text" id="images" name="images">
        </div>
        <div class="field">
            <label for="imageFile">Upload image</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*">
        </div>
        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="status" value="1" checked> Active</label>
                <label><input type="radio" name="status" value="0"> Locked</label>
            </div>
        </div>
        <button type="submit">Insert</button>
    </form>
</main>
</body>
</html>
