package com.WebExcersise.controller;

import com.WebExcersise.entity.User;
import com.WebExcersise.service.IUserService;
import com.WebExcersise.service.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
                User user = userService.register(
                        request.getParameter("fullName"),
                        request.getParameter("email"),
                        request.getParameter("password")
                );
                response.sendRedirect(request.getContextPath() + "/verify-otp?email=" + encode(user.getEmail()));
            } else if (url.contains("/verify-otp")) {
                User user = userService.activate(request.getParameter("email"), request.getParameter("otp"));
                request.getSession().setAttribute("currentUserId", user.getUserId());
                request.getSession().setAttribute("currentUserName", user.getFullName());
                response.sendRedirect(request.getContextPath() + "/profile");
            } else if (url.contains("/login")) {
                User user = userService.login(request.getParameter("email"), request.getParameter("password"));
                request.getSession().setAttribute("currentUserId", user.getUserId());
                request.getSession().setAttribute("currentUserName", user.getFullName());
                response.sendRedirect(request.getContextPath() + "/profile");
            } else if (url.contains("/forgot-password")) {
                String email = request.getParameter("email");
                userService.sendForgotPasswordOtp(email);
                response.sendRedirect(request.getContextPath() + "/reset-password?email=" + encode(email));
            } else if (url.contains("/reset-password")) {
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
            request.setAttribute("email", request.getParameter("email"));
            forwardBack(request, response, url);
        }
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
