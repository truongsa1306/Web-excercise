package com.WebExcersise.service;

import com.WebExcersise.dao.IUserDao;
import com.WebExcersise.dao.UserDao;
import com.WebExcersise.entity.User;
import com.WebExcersise.util.OtpUtil;
import com.WebExcersise.util.PasswordUtil;

import java.util.Optional;

public class UserServiceImpl implements IUserService {
    private final IUserDao userDao;
    private final EmailService emailService;

    public UserServiceImpl() {
        this(new UserDao(), new EmailService());
    }

    public UserServiceImpl(IUserDao userDao) {
        this(userDao, new EmailService());
    }

    public UserServiceImpl(IUserDao userDao, EmailService emailService) {
        this.userDao = userDao;
        this.emailService = emailService;
    }

    @Override
    public User getProfile() {
        return userDao.findProfile().orElseGet(() -> {
            User user = new User("Nguyen Van A", "", "avatar.png");
            user.setEmail("user@example.com");
            user.setPasswordHash(PasswordUtil.hash("123"));
            user.setActive(1);
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

    @Override
    public User register(String fullName, String email, String password) {
        validateRegister(fullName, email, password);
        userDao.findByEmail(email).ifPresent(existing -> {
            throw new IllegalArgumentException("Email da duoc dang ky");
        });

        String otp = OtpUtil.generate();
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone("");
        user.setImages("avatar.png");
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setActive(0);
        user.setOtpCode(otp);
        user.setOtpPurpose("REGISTER");
        user.setOtpExpiresAt(OtpUtil.expiresAt());
        userDao.insert(user);
        emailService.sendOtp(user.getEmail(), "Kich hoat tai khoan", otp);
        return user;
    }

    @Override
    public User activate(String email, String otp) {
        User user = findByEmailRequired(email);
        verifyOtp(user, otp, "REGISTER");
        user.setActive(1);
        clearOtp(user);
        userDao.update(user);
        return user;
    }

    @Override
    public User login(String email, String password) {
        User user = findByEmailRequired(email);
        if (user.getActive() != 1) {
            throw new IllegalArgumentException("Tai khoan chua kich hoat");
        }
        if (!PasswordUtil.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Email hoac mat khau khong dung");
        }
        return user;
    }

    @Override
    public void sendForgotPasswordOtp(String email) {
        User user = findByEmailRequired(email);
        String otp = OtpUtil.generate();
        user.setOtpCode(otp);
        user.setOtpPurpose("FORGOT_PASSWORD");
        user.setOtpExpiresAt(OtpUtil.expiresAt());
        userDao.update(user);
        emailService.sendOtp(user.getEmail(), "Xac nhan quen mat khau", otp);
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        if (newPassword == null || newPassword.length() < 3) {
            throw new IllegalArgumentException("Mat khau moi phai co it nhat 3 ky tu");
        }
        User user = findByEmailRequired(email);
        verifyOtp(user, otp, "FORGOT_PASSWORD");
        user.setPasswordHash(PasswordUtil.hash(newPassword));
        clearOtp(user);
        userDao.update(user);
    }

    private void validate(User user) {
        if (user.getFullName() == null || user.getFullName().isBlank()) {
            throw new IllegalArgumentException("Ho ten khong duoc rong");
        }
        if (user.getPhone() != null && user.getPhone().length() > 20) {
            throw new IllegalArgumentException("So dien thoai khong duoc vuot qua 20 ky tu");
        }
    }

    private void validateRegister(String fullName, String email, String password) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Ho ten khong duoc rong");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new IllegalArgumentException("Email khong hop le");
        }
        if (password == null || password.length() < 3) {
            throw new IllegalArgumentException("Mat khau phai co it nhat 3 ky tu");
        }
    }

    private User findByEmailRequired(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email khong duoc rong");
        }
        return userDao.findByEmail(email.trim())
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay tai khoan voi email nay"));
    }

    private void verifyOtp(User user, String otp, String purpose) {
        if (otp == null || otp.isBlank()) {
            throw new IllegalArgumentException("OTP khong duoc rong");
        }
        if (!purpose.equals(user.getOtpPurpose()) || !otp.trim().equals(user.getOtpCode()) || OtpUtil.isExpired(user.getOtpExpiresAt())) {
            throw new IllegalArgumentException("OTP khong dung hoac da het han");
        }
    }

    private void clearOtp(User user) {
        user.setOtpCode(null);
        user.setOtpPurpose(null);
        user.setOtpExpiresAt(null);
    }
}
