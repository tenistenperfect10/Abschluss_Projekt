package org.texttechnology.parliament_browser_6_4.dao;


import cn.hutool.json.JSONUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.Impl.User_Impl;

import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private final MongoCollection<Document> userCollection;

    public UserDAO(final MongoDatabase database) {
        userCollection = database.getCollection("user");
    }

    public Document queryExistUser(String username) {
        return userCollection.find(new Document("username", username)).first();
    }

    public List<Document> findAllRegularUsers() {
        return userCollection.find(new Document("userType", 0)).into(new ArrayList<>());
    }

    public void saveUser(User_Impl user) {
        Document document = Document.parse(JSONUtil.toJsonStr(user));
        userCollection.insertOne(document);
    }

    public boolean matchUser(User_Impl user) {
        Document queryUser = userCollection.find(new Document("username", user.getUsername())).first();
        if (queryUser.getString("password").equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    public boolean updateUserPwd(User_Impl user) {
        UpdateResult updateResult = userCollection.updateOne(Filters.eq("username", user.getUsername()), Updates.set("password", user.getPassword()));
        return updateResult.getMatchedCount() > 0;
    }

    public boolean updateUserPermission(User_Impl user) {
        UpdateResult updateResult = userCollection.updateOne(Filters.eq("username", user.getUsername()), Updates.set("canEdit", user.getCanEdit()));
        return updateResult.getMatchedCount() > 0;
    }

    public boolean deleteUserByUsername(String username) {
        DeleteResult result = userCollection.deleteOne(new Document("username", username));
        return result.getDeletedCount() > 0;
    }

}
