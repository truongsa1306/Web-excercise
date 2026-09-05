package com.WebExcersise.controller;

import com.WebExcersise.entity.User;
import com.WebExcersise.service.IUserService;
import com.WebExcersise.service.UserServiceImpl;
import com.WebExcersise.util.FormValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@WebServlet(urlPatterns = {
        "/register",
        "/verify-otp",
        "/login",
        "/logout",
        "/forgot-password",
        "/reset-password"
})
public class AuthController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);
        String url = request.getRequestURI();
        if (url.contains("/register")) {
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
        } else if (url.contains("/verify-otp")) {
            request.setAttribute("email", request.getParameter("email"));
            request.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(request, response);
        } else if (url.contains("/login")) {
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
        } else if (url.contains("/logout")) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
        } else if (url.contains("/forgot-password")) {
            request.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(request, response);
        } else if (url.contains("/reset-password")) {
            request.setAttribute("email", request.getParameter("email"));
            request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);
        String url = request.getRequestURI();

        try {
            if (url.contains("/register")) {
                if (hasValidationErrors(request, response, url, validateRegister(request))) {
                    return;
                }
                User user = userService.register(
                        request.getParameter("fullName"),
                        request.getParameter("email"),
                        request.getParameter("password")
                );
                response.sendRedirect(request.getContextPath() + "/verify-otp?email=" + encode(user.getEmail()));
            } else if (url.contains("/verify-otp")) {
                if (hasValidationErrors(request, response, url, validateOtpForm(request))) {
                    return;
                }
                User user = userService.activate(request.getParameter("email"), request.getParameter("otp"));
                request.getSession().setAttribute("currentUserId", user.getUserId());
                request.getSession().setAttribute("currentUserName", user.getFullName());
                response.sendRedirect(request.getContextPath() + "/profile");
            } else if (url.contains("/login")) {
                if (hasValidationErrors(request, response, url, validateLogin(request))) {
                    return;
                }
                User user = userService.login(request.getParameter("email"), request.getParameter("password"));
                request.getSession().setAttribute("currentUserId", user.getUserId());
                request.getSession().setAttribute("currentUserName", user.getFullName());
                response.sendRedirect(request.getContextPath() + "/profile");
            } else if (url.contains("/forgot-password")) {
                if (hasValidationErrors(request, response, url, validateEmailForm(request))) {
                    return;
                }
                String email = request.getParameter("email");
                userService.sendForgotPasswordOtp(email);
                response.sendRedirect(request.getContextPath() + "/reset-password?email=" + encode(email));
            } else if (url.contains("/reset-password")) {
                if (hasValidationErrors(request, response, url, validateResetPassword(request))) {
                    return;
                }
                userService.resetPassword(
                        request.getParameter("email"),
                        request.getParameter("otp"),
                        request.getParameter("password")
                );
                request.setAttribute("success", "Dat lai mat khau thanh cong");
                request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
            }
        } catch (RuntimeException exception) {
            request.setAttribute("error", exception.getMessage());
            keepAuthFormValues(request);
            request.setAttribute("email", request.getParameter("email"));
            forwardBack(request, response, url);
        }
    }

    private boolean hasValidationErrors(HttpServletRequest request, HttpServletResponse response, String url, Map<String, String> errors)
            throws ServletException, IOException {
        keepAuthFormValues(request);
        FormValidator.apply(request, errors);
        if (errors.isEmpty()) {
            return false;
        }
        forwardBack(request, response, url);
        return true;
    }

    private Map<String, String> validateRegister(HttpServletRequest request) {
        Map<String, String> errors = FormValidator.errors();
        FormValidator.required(errors, "fullName", request.getParameter("fullName"), "Ho ten khong duoc rong");
        FormValidator.maxLength(errors, "fullName", request.getParameter("fullName"), 100, "Ho ten khong duoc vuot qua 100 ky tu");
        FormValidator.email(errors, "email", request.getParameter("email"));
        FormValidator.password(errors, "password", request.getParameter("password"));
        return errors;
    }

    private Map<String, String> validateLogin(HttpServletRequest request) {
        Map<String, String> errors = FormValidator.errors();
        FormValidator.email(errors, "email", request.getParameter("email"));
        FormValidator.required(errors, "password", request.getParameter("password"), "Mat khau khong duoc rong");
        return errors;
    }

    private Map<String, String> validateEmailForm(HttpServletRequest request) {
        Map<String, String> errors = FormValidator.errors();
        FormValidator.email(errors, "email", request.getParameter("email"));
        return errors;
    }

    private Map<String, String> validateOtpForm(HttpServletRequest request) {
        Map<String, String> errors = validateEmailForm(request);
        FormValidator.otp(errors, "otp", request.getParameter("otp"));
        return errors;
    }

    private Map<String, String> validateResetPassword(HttpServletRequest request) {
        Map<String, String> errors = validateOtpForm(request);
        FormValidator.password(errors, "password", request.getParameter("password"));
        return errors;
    }

    private void keepAuthFormValues(HttpServletRequest request) {
        request.setAttribute("fullName", request.getParameter("fullName"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("otp", request.getParameter("otp"));
    }

    private void forwardBack(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException {
        if (url.contains("/register")) {
            request.getRequestDispatcher("/views/auth/register.jsp").forward(request, response);
        } else if (url.contains("/verify-otp")) {
            request.getRequestDispatcher("/views/auth/verify-otp.jsp").forward(request, response);
        } else if (url.contains("/forgot-password")) {
            request.getRequestDispatcher("/views/auth/forgot-password.jsp").forward(request, response);
        } else if (url.contains("/reset-password")) {
            request.getRequestDispatcher("/views/auth/reset-password.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/views/auth/login.jsp").forward(request, response);
        }
    }

    private void configureEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");
    }

    private String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
