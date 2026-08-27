package com.WebExcersise.controller;

import com.WebExcersise.config.UploadConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@WebServlet(urlPatterns = "/image")
public class ImageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("fname");
        if (fileName == null || fileName.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Path uploadDir = UploadConfig.UPLOAD_DIR.normalize();
        Path imagePath = uploadDir.resolve(Path.of(fileName).getFileName()).normalize();
        if (!imagePath.startsWith(uploadDir) || !Files.exists(imagePath)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = Files.probeContentType(imagePath);
        response.setContentType(contentType == null ? "application/octet-stream" : contentType);
        response.setContentLengthLong(Files.size(imagePath));

        try (OutputStream outputStream = response.getOutputStream()) {
            Files.copy(imagePath, outputStream);
        }
    }
}
