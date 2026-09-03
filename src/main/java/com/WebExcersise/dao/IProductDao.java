package com.WebExcersise.dao;

import com.WebExcersise.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductDao {
    void insert(Product product);

    void update(Product product);

    void delete(int productId);

    Optional<Product> findById(int productId);

    List<Product> findAll();

    List<Product> findAll(int page, int pageSize);

    List<Product> findLatest(int limit);

    int count();
}
