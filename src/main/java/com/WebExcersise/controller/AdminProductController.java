package com.WebExcersise.controller;

import com.WebExcersise.config.UploadConfig;
import com.WebExcersise.entity.Category;
import com.WebExcersise.entity.Product;
import com.WebExcersise.service.CategoryServiceImpl;
import com.WebExcersise.service.ICategoryService;
import com.WebExcersise.service.IProductService;
import com.WebExcersise.service.ProductServiceImpl;
import com.WebExcersise.util.FileUploadUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

@MultipartConfig
@WebServlet(urlPatterns = {
        "/admin/products",
        "/admin/product/add",
        "/admin/product/insert",
        "/admin/product/edit",
        "/admin/product/update",
        "/admin/product/delete"
})
public class AdminProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);
        String url = request.getRequestURI();

        try {
            if (url.contains("/admin/products")) {
                request.setAttribute("products", productService.findAll());
                request.getRequestDispatcher("/views/admin/product-list.jsp").forward(request, response);
            } else if (url.contains("/admin/product/add")) {
                request.setAttribute("categories", categoryService.findAll());
                request.getRequestDispatcher("/views/admin/product-add.jsp").forward(request, response);
            } else if (url.contains("/admin/product/edit")) {
                int id = parseInt(request.getParameter("id"), 0);
                Product product = productService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay product id: " + id));
                request.setAttribute("product", product);
                request.setAttribute("categories", categoryService.findAll());
                request.getRequestDispatcher("/views/admin/product-edit.jsp").forward(request, response);
            } else if (url.contains("/admin/product/delete")) {
                int id = parseInt(request.getParameter("id"), 0);
                Product product = productService.findById(id).orElse(null);
                productService.delete(id);
                if (product != null) {
                    FileUploadUtil.deleteLocalImage(product.getImages(), UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/products");
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
            if (url.contains("/admin/product/insert")) {
                Product product = readProductFromRequest(request, new Product());
                product.setCreatedAt(LocalDateTime.now());
                productService.insert(product);
                response.sendRedirect(request.getContextPath() + "/admin/products");
            } else if (url.contains("/admin/product/update")) {
                int productId = parseInt(request.getParameter("productId"), 0);
                Product product = productService.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay product id: " + productId));
                String oldImage = product.getImages();
                readProductFromRequest(request, product);
                productService.update(product);
                if (!Objects.equals(oldImage, product.getImages())) {
                    FileUploadUtil.deleteLocalImage(oldImage, UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/products");
            }
        } catch (RuntimeException | IOException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("categories", categoryService.findAll());
            request.getRequestDispatcher("/views/admin/error.jsp").forward(request, response);
        }
    }

    private Product readProductFromRequest(HttpServletRequest request, Product product) throws IOException, ServletException {
        product.setProductName(request.getParameter("productName"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(parseBigDecimal(request.getParameter("price")));
        product.setStatus(parseInt(request.getParameter("status"), 0));

        int categoryId = parseInt(request.getParameter("categoryId"), 0);
        if (categoryId > 0) {
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Khong tim thay category id: " + categoryId));
            product.setCategory(category);
        }

        String imageUrl = request.getParameter("images");
        Part imagePart = request.getPart("imageFile");
        String uploadedFileName = FileUploadUtil.saveImage(imagePart, UploadConfig.UPLOAD_DIR);
        if (uploadedFileName != null) {
            product.setImages(uploadedFileName);
        } else if (imageUrl != null && !imageUrl.isBlank()) {
            product.setImages(imageUrl.trim());
        } else if (product.getImages() == null || product.getImages().isBlank()) {
            product.setImages("avatar.png");
        }

        return product;
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value.trim());
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
