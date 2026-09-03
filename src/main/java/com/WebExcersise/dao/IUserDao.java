package com.WebExcersise.dao;

import com.WebExcersise.entity.User;

import java.util.Optional;

public interface IUserDao {
    void insert(User user);

    void update(User user);

    Optional<User> findById(int userId);

    Optional<User> findProfile();
}
