package com.WebExcersise.service;

import com.WebExcersise.entity.Video;

import java.util.List;
import java.util.Optional;

public interface IVideoService {
    void insert(Video video);

    void update(Video video);

    void delete(String videoId);

    Optional<Video> findById(String videoId);

    List<Video> findAll();

    List<Video> findAll(int page, int pageSize);

    List<Video> searchByTitle(String keyword);

    List<Video> findByCategoryId(int categoryId);

    int count();
}
