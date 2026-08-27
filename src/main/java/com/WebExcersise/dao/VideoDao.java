package com.WebExcersise.dao;

import com.WebExcersise.config.DatabaseType;
import com.WebExcersise.config.JpaConfig;
import com.WebExcersise.entity.Category;
import com.WebExcersise.entity.Video;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class VideoDao implements IVideoDao {
    private final DatabaseType databaseType;

    public VideoDao() {
        this(JpaConfig.getDefaultDatabaseType());
    }

    public VideoDao(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    @Override
    public void insert(Video video) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            attachCategory(entityManager, video);
            entityManager.persist(video);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(Video video) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            attachCategory(entityManager, video);
            entityManager.merge(video);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(String videoId) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Video video = entityManager.find(Video.class, videoId);
            if (video == null) {
                throw new IllegalArgumentException("Khong tim thay video id: " + videoId);
            }
            entityManager.remove(video);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Video> findById(String videoId) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return Optional.ofNullable(entityManager.find(Video.class, videoId));
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Video> findAll() {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return entityManager.createNamedQuery("Video.findAll", Video.class).getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Video> findAll(int page, int pageSize) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            int firstResult = Math.max(page, 0) * pageSize;
            return entityManager.createNamedQuery("Video.findAll", Video.class)
                    .setFirstResult(firstResult)
                    .setMaxResults(pageSize)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Video> searchByTitle(String keyword) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            String jpql = "SELECT v FROM Video v WHERE LOWER(v.title) LIKE LOWER(:keyword) ORDER BY v.videoId";
            TypedQuery<Video> query = entityManager.createQuery(jpql, Video.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Video> findByCategoryId(int categoryId) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            String jpql = "SELECT v FROM Video v WHERE v.category.categoryid = :categoryId ORDER BY v.videoId";
            TypedQuery<Video> query = entityManager.createQuery(jpql, Video.class);
            query.setParameter("categoryId", categoryId);
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public int count() {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            Long total = entityManager.createQuery("SELECT COUNT(v) FROM Video v", Long.class).getSingleResult();
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

    private void attachCategory(EntityManager entityManager, Video video) {
        if (video.getCategory() != null) {
            Category category = entityManager.getReference(Category.class, video.getCategory().getCategoryid());
            video.setCategory(category);
        }
    }
}
