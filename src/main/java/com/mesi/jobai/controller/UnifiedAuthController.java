package com.mesi.jobai.controller;

import com.mesi.jobai.dao.AdminDAO;
import com.mesi.jobai.dao.UserDAO;
import com.mesi.jobai.model.Admin;
import com.mesi.jobai.model.User;

/**
 * Unified authentication controller that handles login for all user types:
 * - Admin (username/password)
 * - Employer (email/password)
 * - Applicant (email/password)
 */
public class UnifiedAuthController {
    private final UserDAO userDAO;
    private final AdminDAO adminDAO;

    public UnifiedAuthController() {
        this.userDAO = new UserDAO();
        this.adminDAO = new AdminDAO();
    }

    /**
     * Unified login method that handles all user types using email
     * @param email email address for all user types (admin, employer, applicant)
     * @param password password
     * @return LoginResult containing user type and user object
     */
    public LoginResult login(String email, String password) {
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            return new LoginResult(false, null, null, "Please fill all fields.");
        }

        // First try admin login (email-based)
        Admin admin = adminDAO.loginAdmin(email, password);
        if (admin != null) {
            return new LoginResult(true, "ADMIN", admin, "Admin login successful");
        }

        // Then try user login (email-based)
        User user = userDAO.loginUser(email, password);
        if (user != null) {
            return new LoginResult(true, user.getRole(), user, "User login successful");
        }

        return new LoginResult(false, null, null, "Invalid email or password.");
    }

    /**
     * Register a new user (only for APPLICANT and EMPLOYER)
     */
    public boolean register(String name, String email, String password, String role) {
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            return false;
        }
        
        // Don't allow admin registration through this method
        if ("ADMIN".equalsIgnoreCase(role)) {
            return false;
        }
        
        User newUser = new User(0, name, email, password, role);
        return userDAO.registerUser(newUser);
    }

    /**
     * Result class for login operations
     */
    public static class LoginResult {
        private final boolean success;
        private final String userType;
        private final Object userObject;
        private final String message;

        public LoginResult(boolean success, String userType, Object userObject, String message) {
            this.success = success;
            this.userType = userType;
            this.userObject = userObject;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getUserType() { return userType; }
        public Object getUserObject() { return userObject; }
        public String getMessage() { return message; }
        
        public User getUser() {
            return userObject instanceof User ? (User) userObject : null;
        }
        
        public Admin getAdmin() {
            return userObject instanceof Admin ? (Admin) userObject : null;
        }
    }
}