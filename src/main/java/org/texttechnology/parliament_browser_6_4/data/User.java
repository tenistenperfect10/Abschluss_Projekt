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

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    Integer getUserType();

    void setUserType(Integer userType);

    Integer getCanEdit();

    void setCanEdit(Integer canEdit);




}
