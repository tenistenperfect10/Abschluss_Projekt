package org.texttechnology.parliament_browser_6_4.data.Impl;

public class User_Impl {

    private String username;

    private String password;

    private Integer userType;

    private Integer canEdit;

    public User_Impl(String username, String password, Integer userType, Integer canEdit) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.canEdit = canEdit;
    }

    public User_Impl() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Integer canEdit) {
        this.canEdit = canEdit;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userType=" + userType +
                ", canEdit=" + canEdit +
                '}';
    }
}
