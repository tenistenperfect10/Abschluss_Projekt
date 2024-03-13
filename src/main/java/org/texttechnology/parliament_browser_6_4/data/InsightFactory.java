package org.texttechnology.parliament_browser_6_4.data;

import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.Impl.User_Impl;

import java.util.List;

public interface InsightFactory {

    Document queryExistUser(String username);

    List<Document> findAllRegularUsers();

    void saveUser(User_Impl user);

    boolean matchUser(User_Impl user);

    boolean updateUserPwd(User_Impl user);

    boolean updateUserPermission(User_Impl user);

    boolean deleteUserByUsername(String username);


}
