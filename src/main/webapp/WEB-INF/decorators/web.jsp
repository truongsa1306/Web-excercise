<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/></title>
    <link rel="stylesheet" href="<c:url value='/assets/layout.css'/>">
    <sitemesh:write property="head"/>
</head>
<body>
<div class="site-shell">
    <%@ include file="/commons/web/header.jsp" %>

    <div class="site-content">
        <sitemesh:write property="body"/>
    </div>

    <%@ include file="/commons/web/footer.jsp" %>
</div>
</body>
</html>
