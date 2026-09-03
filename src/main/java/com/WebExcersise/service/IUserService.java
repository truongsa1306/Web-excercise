package com.WebExcersise.service;

import com.WebExcersise.entity.User;

import java.util.Optional;

public interface IUserService {
    User getProfile();

    void update(User user);

    Optional<User> findById(int userId);

    User register(String fullName, String email, String password);

    User activate(String email, String otp);

    User login(String email, String password);

    void sendForgotPasswordOtp(String email);

    void resetPassword(String email, String otp, String newPassword);
}
