package com.WebExcersise.service;

import com.WebExcersise.dao.IProductDao;
import com.WebExcersise.dao.ProductDao;
import com.WebExcersise.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements IProductService {
    private final IProductDao productDao;

    public ProductServiceImpl() {
        this(new ProductDao());
    }

    public ProductServiceImpl(IProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public void insert(Product product) {
        validate(product);
        productDao.insert(product);
    }

    @Override
    public void update(Product product) {
        validate(product);
        productDao.findById(product.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay product id: " + product.getProductId()));
        productDao.update(product);
    }

    @Override
    public void delete(int productId) {
        productDao.delete(productId);
    }

    @Override
    public Optional<Product> findById(int productId) {
        return productDao.findById(productId);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Product> findAll(int page, int pageSize) {
        return productDao.findAll(page, pageSize);
    }

    @Override
    public List<Product> findLatest(int limit) {
        return productDao.findLatest(limit);
    }

    @Override
    public int count() {
        return productDao.count();
    }

    private void validate(Product product) {
        if (product.getProductName() == null || product.getProductName().isBlank()) {
            throw new IllegalArgumentException("Ten san pham khong duoc rong");
        }
        if (product.getPrice() == null) {
            product.setPrice(BigDecimal.ZERO);
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Gia san pham khong duoc am");
        }
    }
}
