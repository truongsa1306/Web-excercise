package com.WebExcersise.dao;

import com.WebExcersise.config.DatabaseType;
import com.WebExcersise.config.JpaConfig;
import com.WebExcersise.entity.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class CategoryDao implements ICategoryDao {
    private final DatabaseType databaseType;

    public CategoryDao() {
        this(JpaConfig.getDefaultDatabaseType());
    }

    public CategoryDao(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    @Override
    public void insert(Category category) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(category);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Category category) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(category);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(int categoryId) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Category category = entityManager.find(Category.class, categoryId);
            if (category == null) {
                throw new IllegalArgumentException("Khong tim thay category id: " + categoryId);
            }
            entityManager.remove(category);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Category> findById(int categoryId) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return Optional.ofNullable(entityManager.find(Category.class, categoryId));
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Category> findByCategoryName(String categoryName) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            String jpql = "SELECT c FROM Category c WHERE LOWER(c.categoryname) = LOWER(:categoryName)";
            TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
            query.setParameter("categoryName", categoryName);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Category> findAll() {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return entityManager.createNamedQuery("Category.findAll", Category.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Category> findAll(int page, int pageSize) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            int firstResult = Math.max(page, 0) * pageSize;
            return entityManager.createNamedQuery("Category.findAll", Category.class)
                    .setFirstResult(firstResult)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Category> searchByName(String keyword) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            String jpql = "SELECT c FROM Category c WHERE LOWER(c.categoryname) LIKE LOWER(:keyword) ORDER BY c.categoryid DESC";
            TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int count() {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            Long total = entityManager.createQuery("SELECT COUNT(c) FROM Category c", Long.class).getSingleResult();
            return total.intValue();
        } finally {
            entityManager.close();
        }
    }

    private void rollback(EntityTransaction transaction) {
        if (transaction.isActive()) {
            transaction.rollback();
        }
    }
}
