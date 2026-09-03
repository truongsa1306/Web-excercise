package com.WebExcersise.dao;

import com.WebExcersise.config.DatabaseType;
import com.WebExcersise.config.JpaConfig;
import com.WebExcersise.entity.Category;
import com.WebExcersise.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;

public class ProductDao implements IProductDao {
    private final DatabaseType databaseType;

    public ProductDao() {
        this(JpaConfig.getDefaultDatabaseType());
    }

    public ProductDao(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    @Override
    public void insert(Product product) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            attachCategory(entityManager, product);
            entityManager.persist(product);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Product product) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            attachCategory(entityManager, product);
            entityManager.merge(product);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(int productId) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Product product = entityManager.find(Product.class, productId);
            if (product == null) {
                throw new IllegalArgumentException("Khong tim thay product id: " + productId);
            }
            entityManager.remove(product);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Product> findById(int productId) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return Optional.ofNullable(entityManager.find(Product.class, productId));
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findAll() {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return entityManager.createNamedQuery("Product.findAll", Product.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findAll(int page, int pageSize) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            int firstResult = Math.max(page, 0) * pageSize;
            return entityManager.createNamedQuery("Product.findAll", Product.class)
                    .setFirstResult(firstResult)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Product> findLatest(int limit) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return entityManager.createNamedQuery("Product.findLatest", Product.class)
                    .setMaxResults(limit)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int count() {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            Long total = entityManager.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
            return total.intValue();
        } finally {
            entityManager.close();
        }
    }

    private void attachCategory(EntityManager entityManager, Product product) {
        if (product.getCategory() != null) {
            Category category = entityManager.getReference(Category.class, product.getCategory().getCategoryid());
            product.setCategory(category);
        }
    }

    private void rollback(EntityTransaction transaction) {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }
}
