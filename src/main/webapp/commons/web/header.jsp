<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="site-header navbar navbar-expand-lg bg-white border-bottom">
    <div class="container">
        <a class="navbar-brand site-brand" href="<c:url value='/'/>">WebExcercise</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavigation"
                aria-controls="mainNavigation" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <nav id="mainNavigation" class="collapse navbar-collapse" aria-label="Main navigation">
            <div class="navbar-nav ms-auto site-nav">
                <a class="nav-link" href="<c:url value='/'/>">Home</a>
                <a class="nav-link" href="<c:url value='/product'/>">Products</a>
                <a class="nav-link" href="<c:url value='/admin/categories'/>">Categories</a>
                <a class="nav-link" href="<c:url value='/admin/videos'/>">Videos</a>
                <a class="nav-link" href="<c:url value='/admin/products'/>">Manage Products</a>
                <a class="nav-link" href="<c:url value='/profile'/>">Profile</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.currentUserId}">
                        <a class="nav-link" href="<c:url value='/logout'/>">Logout</a>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="<c:url value='/login'/>">Login</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </nav>
    </div>
</header>
