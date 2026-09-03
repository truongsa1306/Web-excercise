package com.WebExcersise.service;

import com.WebExcersise.dao.IUserDao;
import com.WebExcersise.dao.UserDao;
import com.WebExcersise.entity.User;

import java.util.Optional;

public class UserServiceImpl implements IUserService {
    private final IUserDao userDao;

    public UserServiceImpl() {
        this(new UserDao());
    }

    public UserServiceImpl(IUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getProfile() {
        return userDao.findProfile().orElseGet(() -> {
            User user = new User("Nguyen Van A", "", "avatar.png");
            userDao.insert(user);
            return user;
        });
    }

    @Override
    public void update(User user) {
        validate(user);
        userDao.update(user);
    }

    @Override
    public Optional<User> findById(int userId) {
        return userDao.findById(userId);
    }

    private void validate(User user) {
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new IllegalArgumentException("Ho ten khong duoc rong");
        }
        if (user.getPhone() != null && user.getPhone().length() > 20) {
            throw new IllegalArgumentException("So dien thoai khong duoc vuot qua 20 ky tu");
        }
    }
}
