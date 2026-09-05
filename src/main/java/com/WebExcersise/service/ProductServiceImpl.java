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
        if (product.getProductName().trim().length() > 150) {
            throw new IllegalArgumentException("Ten san pham khong duoc vuot qua 150 ky tu");
        }
        if (product.getDescription() != null && product.getDescription().trim().length() > 1000) {
            throw new IllegalArgumentException("Mo ta khong duoc vuot qua 1000 ky tu");
        }
        if (product.getImages() != null && product.getImages().trim().length() > 500) {
            throw new IllegalArgumentException("Link anh khong duoc vuot qua 500 ky tu");
        }
        if (product.getCategory() == null) {
            throw new IllegalArgumentException("Vui long chon danh muc");
        }
        if (product.getPrice() == null) {
            product.setPrice(BigDecimal.ZERO);
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Gia san pham khong duoc am");
        }
        if (product.getStatus() != 0 && product.getStatus() != 1) {
            throw new IllegalArgumentException("Trang thai khong hop le");
        }
    }
}
