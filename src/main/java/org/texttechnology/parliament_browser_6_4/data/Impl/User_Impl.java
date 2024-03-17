package org.texttechnology.parliament_browser_6_4.data.Impl;

import org.texttechnology.parliament_browser_6_4.data.User;

/**
 * Represents a user within the system, encapsulating user-related information
 * such as username, password, user type, and edit permissions.
 */
public class User_Impl implements User {

    private String username;

    private String password;

    private Integer userType;

    private Integer canEdit;

    private String verifyCode;

    /**
     * Constructs a new User_Impl with specified username, password, user type, and edit permissions.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @param userType The type of the user (e.g., admin, regular user).
     * @param canEdit  The permission level of the user regarding edit capabilities.
     */
    public User_Impl(String username, String password, Integer userType, Integer canEdit) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.canEdit = canEdit;
    }
    /**
     * Default constructor for creating an instance of User_Impl without setting properties upfront.
     */
    public User_Impl() {
    }
    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the username of the user.
     *
     * @param username The new username for the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets the password of the user.
     *
     * @param password The new password for the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Gets the user type of the user.
     *
     * @return The user type of the user.
     */
    public Integer getUserType() {
        return userType;
    }
    /**
     * Sets the user type of the user.
     *
     * @param userType The new user type for the user.
     */
    public void setUserType(Integer userType) {
        this.userType = userType;
    }
    /**
     * Gets the edit permissions of the user.
     *
     * @return The edit permissions of the user.
     */
    public Integer getCanEdit() {
        return canEdit;
    }
    /**
     * Sets the edit permissions of the user.
     *
     * @param canEdit The new edit permissions for the user.
     */
    public void setCanEdit(Integer canEdit) {
        this.canEdit = canEdit;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
    /**
     * Returns a string representation of the user, including username, password,
     * user type, and edit permissions.
     *
     * @return A string representation of the user.
     */
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                ", verifyCode=" + verifyCode +
                ", canEdit=" + canEdit +
                '}';
    }
}
