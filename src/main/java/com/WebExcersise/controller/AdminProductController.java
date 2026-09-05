package com.WebExcersise.controller;

import com.WebExcersise.config.UploadConfig;
import com.WebExcersise.entity.Category;
import com.WebExcersise.entity.Product;
import com.WebExcersise.service.CategoryServiceImpl;
import com.WebExcersise.service.ICategoryService;
import com.WebExcersise.service.IProductService;
import com.WebExcersise.service.ProductServiceImpl;
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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
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
                Product product = bindProductFields(request, new Product());
                if (hasProductValidationErrors(request, response, product, false)) {
                    return;
                }
                applyProductCategory(request, product);
                applyProductImage(request, product);
                product.setCreatedAt(LocalDateTime.now());
                productService.insert(product);
                response.sendRedirect(request.getContextPath() + "/admin/products");
            } else if (url.contains("/admin/product/update")) {
                int productId = parseInt(request.getParameter("productId"), 0);
                Product product = productService.findById(productId)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay product id: " + productId));
                String oldImage = product.getImages();
                bindProductFields(request, product);
                if (hasProductValidationErrors(request, response, product, true)) {
                    return;
                }
                applyProductCategory(request, product);
                applyProductImage(request, product);
                productService.update(product);
                if (!Objects.equals(oldImage, product.getImages())) {
                    FileUploadUtil.deleteLocalImage(oldImage, UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/products");
            }
        } catch (RuntimeException | IOException exception) {
            request.setAttribute("error", exception.getMessage());
            forwardProductForm(request, response, url.contains("/admin/product/update"));
        }
    }

    private Product bindProductFields(HttpServletRequest request, Product product) {
        String productName = request.getParameter("productName");
        String description = request.getParameter("description");
        product.setProductName(productName == null ? null : productName.trim());
        product.setDescription(description == null ? null : description.trim());
        product.setPrice(parseBigDecimal(request.getParameter("price")));
        product.setStatus(parseInt(request.getParameter("status"), 0));

        String imageUrl = request.getParameter("images");
        if (imageUrl != null && !imageUrl.isBlank()) {
            product.setImages(imageUrl.trim());
        }

        int categoryId = parseInt(request.getParameter("categoryId"), 0);
        if (categoryId > 0) {
            Category category = new Category();
            category.setCategoryid(categoryId);
            product.setCategory(category);
        }

        return product;
    }

    private void applyProductCategory(HttpServletRequest request, Product product) {
        int categoryId = parseInt(request.getParameter("categoryId"), 0);
        if (categoryId > 0) {
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Khong tim thay category id: " + categoryId));
            product.setCategory(category);
        }
    }

    private void applyProductImage(HttpServletRequest request, Product product) throws IOException, ServletException {
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
    }

    private boolean hasProductValidationErrors(HttpServletRequest request, HttpServletResponse response, Product product, boolean edit)
            throws ServletException, IOException {
        Map<String, String> errors = FormValidator.errors();
        FormValidator.required(errors, "productName", request.getParameter("productName"), "Ten san pham khong duoc rong");
        FormValidator.maxLength(errors, "productName", request.getParameter("productName"), 150, "Ten san pham khong duoc vuot qua 150 ky tu");
        FormValidator.maxLength(errors, "description", request.getParameter("description"), 1000, "Mo ta khong duoc vuot qua 1000 ky tu");
        FormValidator.maxLength(errors, "images", request.getParameter("images"), 500, "Link anh khong duoc vuot qua 500 ky tu");
        BigDecimal price = FormValidator.decimal(errors, "price", request.getParameter("price"), BigDecimal.ZERO, "Gia san pham khong hop le");
        FormValidator.min(errors, "price", price, BigDecimal.ZERO, "Gia san pham khong duoc am");
        int categoryId = FormValidator.integer(errors, "categoryId", request.getParameter("categoryId"), 0, "Danh muc khong hop le");
        FormValidator.min(errors, "categoryId", categoryId, 1, "Vui long chon danh muc");
        int status = FormValidator.integer(errors, "status", request.getParameter("status"), product.getStatus(), "Trang thai khong hop le");
        if (status != 0 && status != 1) {
            errors.putIfAbsent("status", "Trang thai khong hop le");
        }
        FormValidator.apply(request, errors);
        if (errors.isEmpty()) {
            return false;
        }
        prepareProductForm(request, product);
        request.getRequestDispatcher(edit ? "/views/admin/product-edit.jsp" : "/views/admin/product-add.jsp").forward(request, response);
        return true;
    }

    private void forwardProductForm(HttpServletRequest request, HttpServletResponse response, boolean edit) throws ServletException, IOException {
        Product product = new Product();
        String productId = request.getParameter("productId");
        if (edit && productId != null && !productId.isBlank()) {
            product.setProductId(parseInt(productId, 0));
        }
        bindProductFields(request, product);
        prepareProductForm(request, product);
        request.getRequestDispatcher(edit ? "/views/admin/product-edit.jsp" : "/views/admin/product-add.jsp").forward(request, response);
    }

    private void prepareProductForm(HttpServletRequest request, Product product) {
        request.setAttribute("product", product);
        request.setAttribute("priceValue", request.getParameter("price"));
        request.setAttribute("categories", categoryService.findAll());
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException exception) {
            return BigDecimal.ZERO;
        }
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
