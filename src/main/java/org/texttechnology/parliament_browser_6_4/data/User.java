package org.texttechnology.parliament_browser_6_4.data;
/**
 * Represents the contract for user entities within the application.
 * This interface is intended to define common user-related functionalities
 * that all user types must implement. It serves as a foundation for user entity
 * management, including but not limited to authentication, authorization,
 * and user profile management. As the application evolves, methods for managing
 * user attributes, roles, and permissions should be added here.
 */
public interface User {
    /**
     * Retrieves the username of the user.
     *
     * @return A String representing the username of the user.
     */
    String getUsername();

    /**
     * Sets or updates the username of the user.
     *
     * @param username The new username to be set for the user.
     */
    void setUsername(String username);

    /**
     * Gets the password of the user. Note: For security purposes, passwords
     * should be stored and managed securely, potentially using hashing and salting techniques.
     *
     * @return A String representing the user's password.
     */
    String getPassword();
    /**
     * Sets or updates the password of the user. Implementations should ensure
     * that password handling adheres to security best practices, including encryption
     * and secure storage mechanisms.
     *
     * @param password The new password to be set for the user.
     */

    void setPassword(String password);
    /**
     * Retrieves the type of the user. User types are typically represented as integers,
     * where each integer corresponds to a specific category or role within the application
     * (e.g., regular user, admin, moderator).
     *
     * @return An Integer indicating the user's type.
     */
    Integer getUserType();
    /**
     * Sets or updates the type of the user. This can be used to change the user's
     * role or category within the application.
     *
     * @param userType The new user type to be set, represented as an Integer.
     */
    void setUserType(Integer userType);

    /**
     * Gets the user's permission to edit content within the application. This is usually
     * represented as a binary value (e.g., 0 for no permission, 1 for permission granted).
     *
     * @return An Integer representing whether the user has edit permissions.
     */
    Integer getCanEdit();

    /**
     * Sets or updates the user's permission to edit content. This method allows for
     * enabling or disabling a user's ability to perform edit operations within the application.
     *
     * @param canEdit The new edit permission status to be set for the user, represented as an Integer.
     */
    void setCanEdit(Integer canEdit);




}
