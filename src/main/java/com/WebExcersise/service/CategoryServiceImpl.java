package com.WebExcersise.service;

import com.WebExcersise.dao.CategoryDao;
import com.WebExcersise.dao.ICategoryDao;
import com.WebExcersise.entity.Category;

import java.util.List;
import java.util.Optional;

public class CategoryServiceImpl implements ICategoryService {
    private final ICategoryDao categoryDao;

    public CategoryServiceImpl() {
        this(new CategoryDao());
    }

    public CategoryServiceImpl(ICategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public void insert(Category category) {
        validate(category);
        if (categoryDao.findByCategoryName(category.getCategoryname()).isPresent()) {
            throw new IllegalArgumentException("Ten danh muc da ton tai");
        }
        categoryDao.insert(category);
    }

    @Override
    public void update(Category category) {
        validate(category);
        Category current = categoryDao.findById(category.getCategoryid())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay category id: " + category.getCategoryid()));

        categoryDao.findByCategoryName(category.getCategoryname())
                .filter(existing -> existing.getCategoryid() != current.getCategoryid())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Ten danh muc da ton tai");
                });

        categoryDao.update(category);
    }

    @Override
    public void delete(int categoryId) {
        categoryDao.delete(categoryId);
    }

    @Override
    public Optional<Category> findById(int categoryId) {
        return categoryDao.findById(categoryId);
    }

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        return categoryDao.findByCategoryName(categoryName);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public List<Category> findAll(int page, int pageSize) {
        return categoryDao.findAll(page, pageSize);
    }

    @Override
    public List<Category> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return findAll();
        }
        return categoryDao.searchByName(keyword.trim());
    }

    @Override
    public int count() {
        return categoryDao.count();
    }

    private void validate(Category category) {
        if (category.getCategoryname() == null || category.getCategoryname().isBlank()) {
            throw new IllegalArgumentException("Ten danh muc khong duoc rong");
        }
    }
}
