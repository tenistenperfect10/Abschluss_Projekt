package org.texttechnology.parliament_browser_6_4.data.Impl;

import cn.hutool.json.JSONUtil;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConfig;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConnectionHandler;

import java.util.ArrayList;
import java.util.List;

public class InsightFactory_Impl implements InsightFactory {

    private MongoDBConnectionHandler dbConnectionHandler = null;

    private MongoCollection<Document> userCollection ;



    public MongoDBConnectionHandler getMongoConnection() {
        return this.dbConnectionHandler;
    }


    public void createDatabaseConnection(MongoDBConfig pConfig) {
        this.dbConnectionHandler = new MongoDBConnectionHandler(pConfig);

    }

    @Override
    public Document queryExistUser(String username) {
        this.userCollection = dbConnectionHandler.getCollection("user");
        return userCollection.find(new Document("username", username)).first();
    }

    @Override
    public List<Document> findAllRegularUsers() {
        this.userCollection = dbConnectionHandler.getCollection("user");

        return userCollection.find(new Document("userType", 0)).into(new ArrayList<>());
    }

    @Override
    public void saveUser(User_Impl user) {
        this.userCollection = dbConnectionHandler.getCollection("user");
        Document document = Document.parse(JSONUtil.toJsonStr(user));
        userCollection.insertOne(document);
    }

    @Override
    public boolean matchUser(User_Impl user) {

        this.userCollection = dbConnectionHandler.getCollection("user");
        Document queryUser = userCollection.find(new Document("username", user.getUsername())).first();
        if (queryUser.getString("password").equals(user.getPassword())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateUserPwd(User_Impl user) {
        this.userCollection = dbConnectionHandler.getCollection("user");

        UpdateResult updateResult = userCollection.updateOne(Filters.eq("username", user.getUsername()), Updates.set("password", user.getPassword()));
        return updateResult.getMatchedCount() > 0;
    }

    @Override
    public boolean updateUserPermission(User_Impl user) {
        this.userCollection = dbConnectionHandler.getCollection("user");
        UpdateResult updateResult = userCollection.updateOne(Filters.eq("username", user.getUsername()), Updates.set("canEdit", user.getCanEdit()));
        return updateResult.getMatchedCount() > 0;
    }

    @Override
    public boolean deleteUserByUsername(String username) {
        this.userCollection = dbConnectionHandler.getCollection("user");
        DeleteResult result = userCollection.deleteOne(new Document("username", username));
        return result.getDeletedCount() > 0;
    }
}
