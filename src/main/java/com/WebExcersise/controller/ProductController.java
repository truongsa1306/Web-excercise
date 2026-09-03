package com.WebExcersise.controller;

import com.WebExcersise.entity.Product;
import com.WebExcersise.service.IProductService;
import com.WebExcersise.service.ProductServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = {"/product", "/product/detail"})
public class ProductController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6;

    private final IProductService productService = new ProductServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);
        String url = request.getRequestURI();

        if (url.contains("/product/detail")) {
            int id = parseInt(request.getParameter("id"), 0);
            Product product = productService.findById(id)
                    .orElseThrow(() -> new ServletException("Khong tim thay san pham id: " + id));
            request.setAttribute("product", product);
            request.getRequestDispatcher("/views/web/product-detail.jsp").forward(request, response);
            return;
        }

        int page = Math.max(parseInt(request.getParameter("page"), 1), 1);
        int total = productService.count();
        int totalPages = Math.max((int) Math.ceil(total / (double) PAGE_SIZE), 1);
        if (page > totalPages) {
            page = totalPages;
        }

        request.setAttribute("products", productService.findAll(page - 1, PAGE_SIZE));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/views/web/product-list.jsp").forward(request, response);
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
