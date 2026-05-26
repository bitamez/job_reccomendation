package com.mesi.jobai.controller;

import com.mesi.jobai.dao.UserDAO;
import com.mesi.jobai.model.User;

public class AuthController {
    private final UserDAO userDAO;

    public AuthController() {
        this.userDAO = new UserDAO();
    }

    /**
     * Registers a new user.
     * @param name the user's name
     * @param email the user's email
     * @param password the user's password
     * @param role the user's role (e.g. APPLICANT or EMPLOYER)
     * @return true if registration succeeded, false otherwise
     */
    public boolean register(String name, String email, String password, String role) {
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            return false;
        }
        User newUser = new User(0, name, email, password, role);
        return userDAO.registerUser(newUser);
    }

    /**
     * Logs in a user with email and password.
     * @param email the user's email
     * @param password the user's password
     * @return the logged-in User object, or null if login failed
     */
    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return null;
        }
        return userDAO.loginUser(email, password);
    }

    /**
     * Updates the user's profile information.
     * @param user the user details to update
     * @return true if updated successfully, false otherwise
     */
    public boolean updateProfile(User user) {
        if (user == null || user.getName() == null || user.getName().trim().isEmpty() ||
            user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return false;
        }
        return userDAO.updateUser(user);
    }
}
