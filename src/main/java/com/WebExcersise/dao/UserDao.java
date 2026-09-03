package com.WebExcersise.dao;

import com.WebExcersise.config.DatabaseType;
import com.WebExcersise.config.JpaConfig;
import com.WebExcersise.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.Optional;

public class UserDao implements IUserDao {
    private final DatabaseType databaseType;

    public UserDao() {
        this(JpaConfig.getDefaultDatabaseType());
    }

    public UserDao(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    @Override
    public void insert(User user) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(user);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void update(User user) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(user);
            transaction.commit();
        } catch (RuntimeException exception) {
            rollback(transaction);
            throw exception;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<User> findById(int userId) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return Optional.ofNullable(entityManager.find(User.class, userId));
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<User> findProfile() {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return entityManager.createNamedQuery("User.findProfile", User.class)
                    .setMaxResults(1)
                    .getResultStream()
                    .findFirst();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        EntityManager entityManager = JpaConfig.getEntityManager(databaseType);
        try {
            return Optional.of(entityManager.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException exception) {
            return Optional.empty();
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
