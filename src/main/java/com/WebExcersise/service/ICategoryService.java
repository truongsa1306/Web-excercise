package com.WebExcersise.service;

import com.WebExcersise.entity.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    void insert(Category category);

    void update(Category category);

    void delete(int categoryId);

    Optional<Category> findById(int categoryId);

    Optional<Category> findByCategoryName(String categoryName);

    List<Category> findAll();

    List<Category> findAll(int page, int pageSize);

    List<Category> searchByName(String keyword);

    int count();
}
