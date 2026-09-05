package com.WebExcersise.service;

import com.WebExcersise.dao.IVideoDao;
import com.WebExcersise.dao.VideoDao;
import com.WebExcersise.entity.Video;

import java.util.List;
import java.util.Optional;

public class VideoServiceImpl implements IVideoService {
    private final IVideoDao videoDao;

    public VideoServiceImpl() {
        this(new VideoDao());
    }

    public VideoServiceImpl(IVideoDao videoDao) {
        this.videoDao = videoDao;
    }

    @Override
    public void insert(Video video) {
        validate(video);
        if (videoDao.findById(video.getVideoId()).isPresent()) {
            throw new IllegalArgumentException("Video id da ton tai");
        }
        videoDao.insert(video);
    }

    @Override
    public void update(Video video) {
        validate(video);
        videoDao.findById(video.getVideoId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay video id: " + video.getVideoId()));
        videoDao.update(video);
    }

    @Override
    public void delete(String videoId) {
        videoDao.delete(videoId);
    }

    @Override
    public Optional<Video> findById(String videoId) {
        return videoDao.findById(videoId);
    }

    @Override
    public List<Video> findAll() {
        return videoDao.findAll();
    }

    @Override
    public List<Video> findAll(int page, int pageSize) {
        return videoDao.findAll(page, pageSize);
    }

    @Override
    public List<Video> searchByTitle(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return videoDao.searchByTitle(keyword.trim());
    }

    @Override
    public List<Video> findByCategoryId(int categoryId) {
        return videoDao.findByCategoryId(categoryId);
    }

    @Override
    public int count() {
        return videoDao.count();
    }

    private void validate(Video video) {
        if (video.getVideoId() == null || video.getVideoId().isBlank()) {
            throw new IllegalArgumentException("Video id khong duoc rong");
        }
        if (video.getVideoId().trim().length() > 50) {
            throw new IllegalArgumentException("Video id khong duoc vuot qua 50 ky tu");
        }
        if (video.getTitle() == null || video.getTitle().isBlank()) {
            throw new IllegalArgumentException("Tieu de video khong duoc rong");
        }
        if (video.getTitle().trim().length() > 500) {
            throw new IllegalArgumentException("Tieu de video khong duoc vuot qua 500 ky tu");
        }
        if (video.getDescription() != null && video.getDescription().trim().length() > 500) {
            throw new IllegalArgumentException("Mo ta khong duoc vuot qua 500 ky tu");
        }
        if (video.getPoster() != null && video.getPoster().trim().length() > 500) {
            throw new IllegalArgumentException("Link poster khong duoc vuot qua 500 ky tu");
        }
        if (video.getCategory() == null) {
            throw new IllegalArgumentException("Vui long chon danh muc");
        }
        if (video.getViews() < 0) {
            throw new IllegalArgumentException("Luot xem khong duoc am");
        }
        if (video.getActive() != 0 && video.getActive() != 1) {
            throw new IllegalArgumentException("Trang thai khong hop le");
        }
    }
}
