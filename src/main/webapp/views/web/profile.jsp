<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Profile</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/categories'/>">Categories</a>
            <a class="secondary" href="<c:url value='/admin/videos'/>">Videos</a>
        </nav>
    </div>

    <c:if test="${not empty success}">
        <div class="success">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <section class="profile-panel">
        <div class="profile-preview">
            <c:set var="imageValue" value="${user.images}"/>
            <c:choose>
                <c:when test="${not empty imageValue and (fn:startsWith(imageValue, 'http://') or fn:startsWith(imageValue, 'https://'))}">
                    <c:set var="avatarUrl" value="${imageValue}"/>
                </c:when>
                <c:otherwise>
                    <c:url value="/image" var="avatarUrl">
                        <c:param name="fname" value="${imageValue}"/>
                    </c:url>
                </c:otherwise>
            </c:choose>
            <img class="profile-avatar" src="${avatarUrl}" alt="${user.fullName}">
            <div>
                <h2>${user.fullName}</h2>
                <p>${empty user.phone ? 'Chua co so dien thoai' : user.phone}</p>
            </div>
        </div>

        <form class="form profile-form" action="<c:url value='/profile'/>" method="post" enctype="multipart/form-data">
            <input type="hidden" name="userId" value="${user.userId}">

            <div class="field">
                <label for="fullName">Full name</label>
                <input type="text" id="fullName" name="fullName" value="${fn:escapeXml(user.fullName)}" required>
            </div>

            <div class="field">
                <label for="phone">Phone</label>
                <input type="text" id="phone" name="phone" value="${fn:escapeXml(user.phone)}" maxlength="20">
            </div>

            <div class="field">
                <label for="images">Images</label>
                <input type="file" id="images" name="images" accept="image/*">
            </div>

            <button type="submit">Update Profile</button>
        </form>
    </section>
</main>
</body>
</html>
