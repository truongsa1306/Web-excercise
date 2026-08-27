package com.WebExcersise.controller;

import com.WebExcersise.config.UploadConfig;
import com.WebExcersise.entity.Category;
import com.WebExcersise.service.CategoryServiceImpl;
import com.WebExcersise.service.ICategoryService;
import com.WebExcersise.util.FileUploadUtil;
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
                Category category = readCategoryFromRequest(request, new Category());
                categoryService.insert(category);
                response.sendRedirect(request.getContextPath() + "/admin/categories");
            } else if (url.contains("/admin/category/update")) {
                int categoryId = Integer.parseInt(request.getParameter("categoryid"));
                Category category = categoryService.findById(categoryId)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay category id: " + categoryId));
                String oldImage = category.getImages();
                readCategoryFromRequest(request, category);
                categoryService.update(category);
                if (!Objects.equals(oldImage, category.getImages())) {
                    FileUploadUtil.deleteLocalImage(oldImage, UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/categories");
            }
        } catch (RuntimeException exception) {
            request.setAttribute("error", exception.getMessage());
            request.getRequestDispatcher("/views/admin/error.jsp").forward(request, response);
        }
    }

    private Category readCategoryFromRequest(HttpServletRequest request, Category category) throws IOException, ServletException {
        category.setCategoryname(request.getParameter("categoryname"));
        category.setStatus(parseInt(request.getParameter("status"), 0));

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

        return category;
    }

    private int parseInt(String value, int defaultValue) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    private void configureEncoding(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/html; charset=UTF-8");
    }
}
