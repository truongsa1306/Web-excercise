package com.WebExcersise.controller;

import com.WebExcersise.config.UploadConfig;
import com.WebExcersise.entity.Category;
import com.WebExcersise.entity.Video;
import com.WebExcersise.service.CategoryServiceImpl;
import com.WebExcersise.service.ICategoryService;
import com.WebExcersise.service.IVideoService;
import com.WebExcersise.service.VideoServiceImpl;
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
        "/admin/videos",
        "/admin/video/add",
        "/admin/video/insert",
        "/admin/video/edit",
        "/admin/video/update",
        "/admin/video/delete"
})
public class VideoController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final IVideoService videoService = new VideoServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        configureEncoding(request, response);
        String url = request.getRequestURI();

        try {
            if (url.contains("/admin/videos")) {
                String keyword = request.getParameter("keyword");
                List<Video> videos = videoService.searchByTitle(keyword);
                request.setAttribute("listvideo", videos);
                request.setAttribute("keyword", keyword);
                request.getRequestDispatcher("/views/admin/video-list.jsp").forward(request, response);
            } else if (url.contains("/admin/video/add")) {
                request.setAttribute("categories", categoryService.findAll());
                request.getRequestDispatcher("/views/admin/video-add.jsp").forward(request, response);
            } else if (url.contains("/admin/video/edit")) {
                String id = request.getParameter("id");
                Video video = videoService.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay video id: " + id));
                request.setAttribute("video", video);
                request.setAttribute("categories", categoryService.findAll());
                request.getRequestDispatcher("/views/admin/video-edit.jsp").forward(request, response);
            } else if (url.contains("/admin/video/delete")) {
                String id = request.getParameter("id");
                Video video = videoService.findById(id).orElse(null);
                videoService.delete(id);
                if (video != null) {
                    FileUploadUtil.deleteLocalImage(video.getPoster(), UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/videos");
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
            if (url.contains("/admin/video/insert")) {
                Video video = readVideoFromRequest(request, new Video());
                videoService.insert(video);
                response.sendRedirect(request.getContextPath() + "/admin/videos");
            } else if (url.contains("/admin/video/update")) {
                String videoId = request.getParameter("videoId");
                Video video = videoService.findById(videoId)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay video id: " + videoId));
                String oldPoster = video.getPoster();
                readVideoFromRequest(request, video);
                videoService.update(video);
                if (!Objects.equals(oldPoster, video.getPoster())) {
                    FileUploadUtil.deleteLocalImage(oldPoster, UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/videos");
            }
        } catch (RuntimeException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("categories", categoryService.findAll());
            request.getRequestDispatcher("/views/admin/error.jsp").forward(request, response);
        }
    }

    private Video readVideoFromRequest(HttpServletRequest request, Video video) throws IOException, ServletException {
        video.setVideoId(request.getParameter("videoId"));
        video.setTitle(request.getParameter("title"));
        video.setDescription(request.getParameter("description"));
        video.setViews(parseInt(request.getParameter("views"), 0));
        video.setActive(parseInt(request.getParameter("active"), 0));

        int categoryId = parseInt(request.getParameter("categoryId"), 0);
        if (categoryId > 0) {
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Khong tim thay category id: " + categoryId));
            video.setCategory(category);
        }

        String posterUrl = request.getParameter("poster");
        Part posterPart = request.getPart("posterFile");
        String uploadedFileName = FileUploadUtil.saveImage(posterPart, UploadConfig.UPLOAD_DIR);
        if (uploadedFileName != null) {
            video.setPoster(uploadedFileName);
        } else if (posterUrl != null && !posterUrl.isBlank()) {
            video.setPoster(posterUrl.trim());
        }

        return video;
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
