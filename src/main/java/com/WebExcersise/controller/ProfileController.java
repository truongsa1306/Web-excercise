package com.WebExcersise.controller;

import com.WebExcersise.config.UploadConfig;
import com.WebExcersise.entity.User;
import com.WebExcersise.service.IUserService;
import com.WebExcersise.service.UserServiceImpl;
import com.WebExcersise.util.FileUploadUtil;
import com.WebExcersise.util.FormValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@MultipartConfig
@WebServlet(urlPatterns = "/profile")
public class ProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);
        User user = getCurrentUser(request, response);
        if (user == null) {
            return;
        }
        request.setAttribute("user", user);
        request.getRequestDispatcher("/views/web/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);

        try {
            User user = getCurrentUser(request, response);
            if (user == null) {
                return;
            }

            String oldImage = user.getImages();
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            user.setFullName(fullName == null ? null : fullName.trim());
            user.setPhone(phone == null ? null : phone.trim());

            Map<String, String> errors = validateProfile(request);
            FormValidator.apply(request, errors);
            if (!errors.isEmpty()) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("/views/web/profile.jsp").forward(request, response);
                return;
            }

            Part imagePart = request.getPart("images");
            String uploadedFileName = FileUploadUtil.saveImage(imagePart, UploadConfig.UPLOAD_DIR);
            if (uploadedFileName != null) {
                user.setImages(uploadedFileName);
            } else if (user.getImages() == null || user.getImages().isBlank()) {
                user.setImages("avatar.png");
            }

            userService.update(user);
            request.getSession().setAttribute("currentUserName", user.getFullName());
            if (!Objects.equals(oldImage, user.getImages())) {
                FileUploadUtil.deleteLocalImage(oldImage, UploadConfig.UPLOAD_DIR);
            }

            request.setAttribute("success", "Cap nhat profile thanh cong");
            request.setAttribute("user", user);
        } catch (RuntimeException | IOException exception) {
            request.setAttribute("error", exception.getMessage());
            User user = getCurrentUser(request, response);
            if (user == null) {
                return;
            }
            request.setAttribute("user", user);
        }

        request.getRequestDispatcher("/views/web/profile.jsp").forward(request, response);
    }

    private Map<String, String> validateProfile(HttpServletRequest request) {
        Map<String, String> errors = FormValidator.errors();
        FormValidator.required(errors, "fullName", request.getParameter("fullName"), "Ho ten khong duoc rong");
        FormValidator.maxLength(errors, "fullName", request.getParameter("fullName"), 100, "Ho ten khong duoc vuot qua 100 ky tu");
        FormValidator.maxLength(errors, "phone", request.getParameter("phone"), 20, "So dien thoai khong duoc vuot qua 20 ky tu");
        FormValidator.phone(errors, "phone", request.getParameter("phone"));
        return errors;
    }

    private User getCurrentUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object currentUserId = request.getSession().getAttribute("currentUserId");
        if (!(currentUserId instanceof Number number)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }

        User user = userService.findById(number.intValue()).orElse(null);
        if (user == null) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login");
            return null;
        }
        return user;
    }

    private void configureEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");
    }
}
