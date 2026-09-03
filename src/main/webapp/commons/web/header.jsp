<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="site-header">
    <div class="site-header__inner">
        <a class="site-brand" href="<c:url value='/'/>">WebExcercise</a>
        <nav class="site-nav" aria-label="Main navigation">
            <a href="<c:url value='/'/>">Home</a>
            <a href="<c:url value='/product'/>">Products</a>
            <a href="<c:url value='/admin/categories'/>">Categories</a>
            <a href="<c:url value='/admin/videos'/>">Videos</a>
            <a href="<c:url value='/admin/products'/>">Manage Products</a>
            <a href="<c:url value='/profile'/>">Profile</a>
            <c:choose>
                <c:when test="${not empty sessionScope.currentUserId}">
                    <a href="<c:url value='/logout'/>">Logout</a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value='/login'/>">Login</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>
