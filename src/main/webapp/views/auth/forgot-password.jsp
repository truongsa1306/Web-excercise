<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Forgot Password</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page auth-page">
    <div class="topbar">
        <h1>Forgot Password</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/login'/>">Login</a>
        </nav>
    </div>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form class="form auth-form" action="<c:url value='/forgot-password'/>" method="post">
        <div class="field">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="${email}" required>
        </div>
        <button type="submit">Send OTP</button>
    </form>
</main>
</body>
</html>
