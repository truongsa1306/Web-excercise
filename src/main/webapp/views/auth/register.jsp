<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" href="<c:url value='/assets/admin.css'/>">
</head>
<body>
<main class="page auth-page">
    <div class="topbar">
        <h1>Register</h1>
        <nav class="nav">
            <a class="secondary" href="<c:url value='/login'/>">Login</a>
        </nav>
    </div>

    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <form class="form auth-form" action="<c:url value='/register'/>" method="post">
        <div class="field">
            <label for="fullName">Full name</label>
            <input class="${not empty errors.fullName ? 'invalid' : ''}" type="text" id="fullName" name="fullName"
                   value="${fn:escapeXml(fullName)}" maxlength="100" required>
            <c:if test="${not empty errors.fullName}">
                <span class="field-error">${errors.fullName}</span>
            </c:if>
        </div>
        <div class="field">
            <label for="email">Email</label>
            <input class="${not empty errors.email ? 'invalid' : ''}" type="email" id="email" name="email"
                   value="${fn:escapeXml(email)}" maxlength="120" required>
            <c:if test="${not empty errors.email}">
                <span class="field-error">${errors.email}</span>
            </c:if>
        </div>
        <div class="field">
            <label for="password">Password</label>
            <input class="${not empty errors.password ? 'invalid' : ''}" type="password" id="password" name="password"
                   minlength="3" required>
            <c:if test="${not empty errors.password}">
                <span class="field-error">${errors.password}</span>
            </c:if>
        </div>
        <button type="submit">Register</button>
    </form>
</main>
</body>
</html>
