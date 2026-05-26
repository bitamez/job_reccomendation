package com.mesi.jobai.service;

import com.mesi.jobai.dao.UserDAO;
import com.mesi.jobai.model.User;

/**
 * AuthService handles business logic for authentication.
 * It sits between AuthController and UserDAO, adding rules
 * like input sanitization and role validation.
 */
public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Validates and registers a new user.
     * Rules:
     *  - Email must contain "@" and "."
     *  - Password must be at least 6 characters long
     *  - Role must be either APPLICANT or EMPLOYER
     */
    public boolean register(String name, String email, String password, String role) {
        // Basic field checks
        if (name == null || name.trim().isEmpty()) return false;
        if (email == null || !email.contains("@") || !email.contains(".")) return false;
        if (password == null || password.length() < 6) return false;
        if (!role.equalsIgnoreCase("APPLICANT") && !role.equalsIgnoreCase("EMPLOYER")) return false;

        User newUser = new User(0, name.trim(), email.trim().toLowerCase(), password, role.toUpperCase());
        return userDAO.registerUser(newUser);
    }

    /**
     * Authenticates a user's login credentials.
     * Returns the User object on success, or null on failure.
     */
    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty()) return null;
        if (password == null || password.trim().isEmpty()) return null;
        return userDAO.loginUser(email.trim().toLowerCase(), password);
    }

    /**
     * Updates a user's profile name and email.
     * Validates that the new email is valid before updating.
     */
    public boolean updateProfile(User user) {
        if (user == null) return false;
        if (user.getName() == null || user.getName().trim().isEmpty()) return false;
        if (user.getEmail() == null || !user.getEmail().contains("@")) return false;
        return userDAO.updateUser(user);
    }
}
