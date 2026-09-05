package com.WebExcersise.controller;

import com.WebExcersise.config.UploadConfig;
import com.WebExcersise.entity.Category;
import com.WebExcersise.service.CategoryServiceImpl;
import com.WebExcersise.service.ICategoryService;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

@MultipartConfig
@WebServlet(urlPatterns = {
        "/admin/categories",
        "/admin/category/add",
        "/admin/category/insert",
        "/admin/category/edit",
        "/admin/category/update",
        "/admin/category/delete"
})
public class CategoryController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);
        String url = request.getRequestURI();

        try {
            if (url.contains("/admin/categories")) {
                String keyword = request.getParameter("keyword");
                List<Category> categories = categoryService.searchByName(keyword);
                request.setAttribute("listcate", categories);
                request.setAttribute("keyword", keyword);
                request.getRequestDispatcher("/views/admin/category-list.jsp").forward(request, response);
            } else if (url.contains("/admin/category/add")) {
                request.getRequestDispatcher("/views/admin/category-add.jsp").forward(request, response);
            } else if (url.contains("/admin/category/edit")) {
                int id = Integer.parseInt(request.getParameter("id"));
                Category category = categoryService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay category id: " + id));
                request.setAttribute("cate", category);
                request.getRequestDispatcher("/views/admin/category-edit.jsp").forward(request, response);
            } else if (url.contains("/admin/category/delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
                Category category = categoryService.findById(id).orElse(null);
                categoryService.delete(id);
                if (category != null) {
                    FileUploadUtil.deleteLocalImage(category.getImages(), UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/categories");
            }
        } catch (RuntimeException exception) {
            request.setAttribute("error", exception.getMessage());
            request.getRequestDispatcher("/views/admin/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);
        String url = request.getRequestURI();

        try {
            if (url.contains("/admin/category/insert")) {
                Category category = bindCategoryFields(request, new Category());
                if (hasCategoryValidationErrors(request, response, category, false)) {
                    return;
                }
                applyCategoryImage(request, category);
                categoryService.insert(category);
                response.sendRedirect(request.getContextPath() + "/admin/categories");
            } else if (url.contains("/admin/category/update")) {
                int categoryId = Integer.parseInt(request.getParameter("categoryid"));
                Category category = categoryService.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay category id: " + categoryId));
                String oldImage = category.getImages();
                bindCategoryFields(request, category);
                if (hasCategoryValidationErrors(request, response, category, true)) {
                    return;
                }
                applyCategoryImage(request, category);
                categoryService.update(category);
                if (!Objects.equals(oldImage, category.getImages())) {
                    FileUploadUtil.deleteLocalImage(oldImage, UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/categories");
            }
        } catch (RuntimeException | IOException exception) {
            request.setAttribute("error", exception.getMessage());
            forwardCategoryForm(request, response, url.contains("/admin/category/update"));
        }
    }

    private Category bindCategoryFields(HttpServletRequest request, Category category) {
        String categoryName = request.getParameter("categoryname");
        category.setCategoryname(categoryName == null ? null : categoryName.trim());
        category.setStatus(parseInt(request.getParameter("status"), 0));
        String imageUrl = request.getParameter("images");
        if (imageUrl != null && !imageUrl.isBlank()) {
            category.setImages(imageUrl.trim());
        }
        return category;
    }

    private void applyCategoryImage(HttpServletRequest request, Category category) throws IOException, ServletException {
        String imageUrl = request.getParameter("images");
        Part imagePart = request.getPart("images1");
        String uploadedFileName = FileUploadUtil.saveImage(imagePart, UploadConfig.UPLOAD_DIR);
        if (uploadedFileName != null) {
            category.setImages(uploadedFileName);
        } else if (imageUrl != null && !imageUrl.isBlank()) {
            category.setImages(imageUrl.trim());
        } else if (category.getImages() == null || category.getImages().isBlank()) {
            category.setImages("avatar.png");
        }
    }

    private boolean hasCategoryValidationErrors(HttpServletRequest request, HttpServletResponse response, Category category, boolean edit)
            throws ServletException, IOException {
        Map<String, String> errors = FormValidator.errors();
        FormValidator.required(errors, "categoryname", request.getParameter("categoryname"), "Ten danh muc khong duoc rong");
        FormValidator.maxLength(errors, "categoryname", request.getParameter("categoryname"), 50, "Ten danh muc khong duoc vuot qua 50 ky tu");
        FormValidator.maxLength(errors, "images", request.getParameter("images"), 500, "Link anh khong duoc vuot qua 500 ky tu");
        int status = FormValidator.integer(errors, "status", request.getParameter("status"), category.getStatus(), "Trang thai khong hop le");
        if (status != 0 && status != 1) {
            errors.putIfAbsent("status", "Trang thai khong hop le");
        }
        FormValidator.apply(request, errors);
        if (errors.isEmpty()) {
            return false;
        }
        request.setAttribute("cate", category);
        request.getRequestDispatcher(edit ? "/views/admin/category-edit.jsp" : "/views/admin/category-add.jsp").forward(request, response);
        return true;
    }

    private void forwardCategoryForm(HttpServletRequest request, HttpServletResponse response, boolean edit) throws ServletException, IOException {
        Category category = new Category();
        String categoryId = request.getParameter("categoryid");
        if (edit && categoryId != null && !categoryId.isBlank()) {
            category.setCategoryid(parseInt(categoryId, 0));
        }
        bindCategoryFields(request, category);
        request.setAttribute("cate", category);
        request.getRequestDispatcher(edit ? "/views/admin/category-edit.jsp" : "/views/admin/category-add.jsp").forward(request, response);
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException exception) {
            return defaultValue;
        }
    }

    private void configureEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");
    }
}
