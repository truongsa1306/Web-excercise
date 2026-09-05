<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Videos</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page">
    <div class="topbar">
        <h1>Videos</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/admin/categories'/>">Categories</a>
            <a href="<c:url value='/admin/videos'/>">Videos</a>
        </nav>
    </div>

    <div class="toolbar">
        <a class="button" href="<c:url value='/admin/video/add'/>">Add Video</a>
        <form class="search" action="<c:url value='/admin/videos'/>" method="get">
            <input type="text" name="keyword" value="${fn:escapeXml(keyword)}" placeholder="Search video title" maxlength="500">
            <button type="submit">Search</button>
        </form>
    </div>

    <table>
        <thead>
        <tr>
            <th>STT</th>
            <th>Poster</th>
            <th>Video ID</th>
            <th>Title</th>
            <th>Category</th>
            <th>Views</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listvideo}" var="video" varStatus="status">
            <c:set var="posterValue" value="${video.poster}"/>
            <c:choose>
                <c:when test="${not empty posterValue and (fn:startsWith(posterValue, 'http://') or fn:startsWith(posterValue, 'https://'))}">
                    <c:set var="posterUrl" value="${posterValue}"/>
                </c:when>
                <c:otherwise>
                    <c:url value="/image" var="posterUrl">
                        <c:param name="fname" value="${posterValue}"/>
                    </c:url>
                </c:otherwise>
            </c:choose>
            <tr>
                <td>${status.index + 1}</td>
                <td><img class="thumb" src="${posterUrl}" alt="${video.title}"></td>
                <td>${video.videoId}</td>
                <td>${video.title}</td>
                <td>${video.category.categoryname}</td>
                <td>${video.views}</td>
                <td>
                    <c:choose>
                        <c:when test="${video.active == 1}">Hoạt động</c:when>
                        <c:otherwise>Khóa</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <div class="actions">
                        <a href="<c:url value='/admin/video/edit?id=${video.videoId}'/>">Sửa</a>
                        <a href="<c:url value='/admin/video/delete?id=${video.videoId}'/>"
                           onclick="return confirm('Xóa video này?')">Xóa</a>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
</body>
</html>
