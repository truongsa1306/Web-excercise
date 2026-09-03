package com.WebExcersise.service;

import com.WebExcersise.entity.User;

import java.util.Optional;

public interface IUserService {
    User getProfile();

    void update(User user);

    Optional<User> findById(int userId);
}
