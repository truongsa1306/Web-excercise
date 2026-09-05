package com.WebExcersise.controller;

import com.WebExcersise.config.UploadConfig;
import com.WebExcersise.entity.Category;
import com.WebExcersise.entity.Video;
import com.WebExcersise.service.CategoryServiceImpl;
import com.WebExcersise.service.ICategoryService;
import com.WebExcersise.service.IVideoService;
import com.WebExcersise.service.VideoServiceImpl;
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
                Video video = bindVideoFields(request, new Video());
                if (hasVideoValidationErrors(request, response, video, false)) {
                    return;
                }
                applyVideoCategory(request, video);
                applyPoster(request, video);
                videoService.insert(video);
                response.sendRedirect(request.getContextPath() + "/admin/videos");
            } else if (url.contains("/admin/video/update")) {
                String videoId = request.getParameter("videoId");
                Video video = videoService.findById(videoId)
                        .orElseThrow(() -> new IllegalArgumentException("Khong tim thay video id: " + videoId));
                String oldPoster = video.getPoster();
                bindVideoFields(request, video);
                if (hasVideoValidationErrors(request, response, video, true)) {
                    return;
                }
                applyVideoCategory(request, video);
                applyPoster(request, video);
                videoService.update(video);
                if (!Objects.equals(oldPoster, video.getPoster())) {
                    FileUploadUtil.deleteLocalImage(oldPoster, UploadConfig.UPLOAD_DIR);
                }
                response.sendRedirect(request.getContextPath() + "/admin/videos");
            }
        } catch (RuntimeException | IOException exception) {
            request.setAttribute("error", exception.getMessage());
            forwardVideoForm(request, response, url.contains("/admin/video/update"));
        }
    }

    private Video bindVideoFields(HttpServletRequest request, Video video) {
        String videoId = request.getParameter("videoId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        video.setVideoId(videoId == null ? null : videoId.trim());
        video.setTitle(title == null ? null : title.trim());
        video.setDescription(description == null ? null : description.trim());
        video.setViews(parseInt(request.getParameter("views"), 0));
        video.setActive(parseInt(request.getParameter("active"), 0));

        String posterUrl = request.getParameter("poster");
        if (posterUrl != null && !posterUrl.isBlank()) {
            video.setPoster(posterUrl.trim());
        }

        int categoryId = parseInt(request.getParameter("categoryId"), 0);
        if (categoryId > 0) {
            Category category = new Category();
            category.setCategoryid(categoryId);
            video.setCategory(category);
        }

        return video;
    }

    private void applyVideoCategory(HttpServletRequest request, Video video) {
        int categoryId = parseInt(request.getParameter("categoryId"), 0);
        if (categoryId > 0) {
            Category category = categoryService.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Khong tim thay category id: " + categoryId));
            video.setCategory(category);
        }
    }

    private void applyPoster(HttpServletRequest request, Video video) throws IOException, ServletException {
        String posterUrl = request.getParameter("poster");
        Part posterPart = request.getPart("posterFile");
        String uploadedFileName = FileUploadUtil.saveImage(posterPart, UploadConfig.UPLOAD_DIR);
        if (uploadedFileName != null) {
            video.setPoster(uploadedFileName);
        } else if (posterUrl != null && !posterUrl.isBlank()) {
            video.setPoster(posterUrl.trim());
        }
    }

    private boolean hasVideoValidationErrors(HttpServletRequest request, HttpServletResponse response, Video video, boolean edit)
            throws ServletException, IOException {
        Map<String, String> errors = FormValidator.errors();
        FormValidator.required(errors, "videoId", request.getParameter("videoId"), "Video id khong duoc rong");
        FormValidator.maxLength(errors, "videoId", request.getParameter("videoId"), 50, "Video id khong duoc vuot qua 50 ky tu");
        FormValidator.required(errors, "title", request.getParameter("title"), "Tieu de video khong duoc rong");
        FormValidator.maxLength(errors, "title", request.getParameter("title"), 500, "Tieu de video khong duoc vuot qua 500 ky tu");
        FormValidator.maxLength(errors, "description", request.getParameter("description"), 500, "Mo ta khong duoc vuot qua 500 ky tu");
        FormValidator.maxLength(errors, "poster", request.getParameter("poster"), 500, "Link poster khong duoc vuot qua 500 ky tu");
        int views = FormValidator.integer(errors, "views", request.getParameter("views"), video.getViews(), "Luot xem khong hop le");
        FormValidator.min(errors, "views", views, 0, "Luot xem khong duoc am");
        int categoryId = FormValidator.integer(errors, "categoryId", request.getParameter("categoryId"), 0, "Danh muc khong hop le");
        FormValidator.min(errors, "categoryId", categoryId, 1, "Vui long chon danh muc");
        int active = FormValidator.integer(errors, "active", request.getParameter("active"), video.getActive(), "Trang thai khong hop le");
        if (active != 0 && active != 1) {
            errors.putIfAbsent("active", "Trang thai khong hop le");
        }
        FormValidator.apply(request, errors);
        if (errors.isEmpty()) {
            return false;
        }
        prepareVideoForm(request, video);
        request.getRequestDispatcher(edit ? "/views/admin/video-edit.jsp" : "/views/admin/video-add.jsp").forward(request, response);
        return true;
    }

    private void forwardVideoForm(HttpServletRequest request, HttpServletResponse response, boolean edit) throws ServletException, IOException {
        Video video = bindVideoFields(request, new Video());
        prepareVideoForm(request, video);
        request.getRequestDispatcher(edit ? "/views/admin/video-edit.jsp" : "/views/admin/video-add.jsp").forward(request, response);
    }

    private void prepareVideoForm(HttpServletRequest request, Video video) {
        request.setAttribute("video", video);
        request.setAttribute("viewsValue", request.getParameter("views"));
        request.setAttribute("categories", categoryService.findAll());
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
