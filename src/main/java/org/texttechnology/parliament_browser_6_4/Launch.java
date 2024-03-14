package org.texttechnology.parliament_browser_6_4;

import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import org.texttechnology.parliament_browser_6_4.controller.*;
import org.texttechnology.parliament_browser_6_4.dao.LogDAO;
import org.texttechnology.parliament_browser_6_4.data.Impl.InsightFactory_Impl;
import org.texttechnology.parliament_browser_6_4.data.configuration.FreemarkerBasedRoute;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConfig;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConnectionHandler;
import spark.Spark;

import java.io.IOException;

import static spark.Spark.setPort;

public class Launch {
    private final Configuration cfg;

    public Launch() throws IOException {
        String pTarget = Launch.class.getClassLoader().getResource("Project_06_04.txt").getPath();
        MongoDBConfig dbConfigTarget = new MongoDBConfig(pTarget);
        MongoDBConnectionHandler mongoDBConnectionHandler = new MongoDBConnectionHandler(dbConfigTarget);
        MongoDatabase mongoDatabase = mongoDBConnectionHandler.getDatabase();
        cfg = FreemarkerBasedRoute.createFreemarkerConfiguration();

        Spark.staticFiles.location("/public");
        setPort(8080);
        InsightFactory_Impl factory = new InsightFactory_Impl();
        factory.createDatabaseConnection(dbConfigTarget);
        LogDAO.init(mongoDatabase);

        //System.out.println(factory.findBySpeechId("ID209613000"));

        new MeetingController(factory, cfg);
        new CommentController(factory, cfg);
        new SpeakerController(factory, cfg);
        new SpeechController(factory, cfg);
        new UserController(factory, cfg);
    }

    public static void main(String[] args) throws IOException {
        new Launch();
    }

}
