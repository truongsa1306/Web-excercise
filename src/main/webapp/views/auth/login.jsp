<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page auth-page">
    <div class="topbar">
        <h1>Login</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/register'/>">Register</a>
        </nav>
    </div>

    <c:if test="${not empty success}">
        <div class="success">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form class="form auth-form" action="<c:url value='/login'/>" method="post">
        <div class="field">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${email}" required>
        </div>
        <div class="field">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Login</button>
        <a class="form-link" href="<c:url value='/forgot-password'/>">Forgot password</a>
    </form>
</main>
</body>
</html>
