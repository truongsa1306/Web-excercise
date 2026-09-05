<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Add Video</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Add Video</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/videos'/>">Back</a>
        </nav>
    </div>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form class="form" action="<c:url value='/admin/video/insert'/>" method="post" enctype="multipart/form-data">
        <div class="field">
            <label for="videoId">Video ID</label>
            <input class="${not empty errors.videoId ? 'invalid' : ''}" type="text" id="videoId" name="videoId"
                   value="${fn:escapeXml(video.videoId)}" maxlength="50" required>
            <c:if test="${not empty errors.videoId}">
                <span class="field-error">${errors.videoId}</span>
            </c:if>
        </div>

        <div class="field">
            <label for="title">Title</label>
            <input class="${not empty errors.title ? 'invalid' : ''}" type="text" id="title" name="title"
                   value="${fn:escapeXml(video.title)}" maxlength="500" required>
            <c:if test="${not empty errors.title}">
                <span class="field-error">${errors.title}</span>
            </c:if>
        </div>

        <div class="field">
            <label for="categoryId">Category</label>
            <select class="${not empty errors.categoryId ? 'invalid' : ''}" id="categoryId" name="categoryId" required>
                <option value="">-- Select category --</option>
                <c:forEach items="${categories}" var="category">
                    <option value="${category.categoryid}" ${not empty video.category and video.category.categoryid == category.categoryid ? 'selected' : ''}>
                            ${category.categoryname}
                    </option>
                </c:forEach>
            </select>
            <c:if test="${not empty errors.categoryId}">
                <span class="field-error">${errors.categoryId}</span>
            </c:if>
        </div>

        <div class="field">
            <label for="description">Description</label>
            <textarea class="${not empty errors.description ? 'invalid' : ''}" id="description" name="description"
                      maxlength="500">${fn:escapeXml(video.description)}</textarea>
            <c:if test="${not empty errors.description}">
                <span class="field-error">${errors.description}</span>
            </c:if>
        </div>

        <div class="field">
            <label for="poster">Poster link</label>
            <input class="${not empty errors.poster ? 'invalid' : ''}" type="text" id="poster" name="poster"
                   value="${fn:escapeXml(video.poster)}" maxlength="500">
            <c:if test="${not empty errors.poster}">
                <span class="field-error">${errors.poster}</span>
            </c:if>
        </div>

        <div class="field">
            <label for="posterFile">Upload poster</label>
            <input type="file" id="posterFile" name="posterFile" accept="image/*">
        </div>

        <div class="field">
            <label for="views">Views</label>
            <input class="${not empty errors.views ? 'invalid' : ''}" type="number" id="views" name="views"
                   min="0" value="${empty viewsValue ? '0' : fn:escapeXml(viewsValue)}" required>
            <c:if test="${not empty errors.views}">
                <span class="field-error">${errors.views}</span>
            </c:if>
        </div>

        <div class="field">
            <label>Status</label>
            <div class="radio-group">
                <label><input type="radio" name="active" value="1" ${empty video or video.active == 1 ? 'checked' : ''}> Hoat dong</label>
                <label><input type="radio" name="active" value="0" ${not empty video and video.active == 0 ? 'checked' : ''}> Khoa</label>
            </div>
            <c:if test="${not empty errors.active}">
                <span class="field-error">${errors.active}</span>
            </c:if>
        </div>

        <button type="submit">Insert</button>
    </form>
</main>
</body>
</html>
