package org.texttechnology.parliament_browser_6_4;

import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import org.texttechnology.parliament_browser_6_4.controller.*;
import org.texttechnology.parliament_browser_6_4.dao.*;
import org.texttechnology.parliament_browser_6_4.data.Impl.InsightFactory_Impl;
import org.texttechnology.parliament_browser_6_4.data.configuration.FreemarkerBasedRoute;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConfig;
import org.texttechnology.parliament_browser_6_4.helper.MongoDBConnectionHandler;
import spark.Spark;

import java.io.IOException;

import static spark.Spark.setPort;
/**
 * The main launching class for the web application. It initializes the application's
 * configuration, database connection, and routes for handling HTTP requests.
 * It uses Freemarker for templating and Spark framework for handling web requests.
 */
public class Launch {
    private final Configuration cfg;

    /**
     * Constructor for the Launch class. Initializes MongoDB connection, sets up
     * Freemarker configuration, and configures Spark routes for different parts of
     * the application such as meetings, comments, speakers, speeches, and user control.
     *
     * @throws IOException If there is an issue reading the database configuration file or initializing Freemarker.
     */
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
        // Initialize controllers for handling different application routes.
        //MeetingDAO meetingDAO = new MeetingDAO(mongoDatabase);
        //CommentDAO commentDAO = new CommentDAO(mongoDatabase);
        //SpeakerDAO speakerDAO = new SpeakerDAO(mongoDatabase);
        //SpeechDAO speechDAO = new SpeechDAO(mongoDatabase);
        UserDAO userDAO = new UserDAO(mongoDatabase);
        LogDAO.init(mongoDatabase);

        new MeetingController(factory, userDAO, cfg);
        new CommentController(factory, cfg);
        new SpeakerController(factory, cfg);
        new SpeechController( cfg,factory);
        new UserController(userDAO, cfg);
    }

    /**
     * The main entry point of the application.
     *
     * @param args Command line arguments passed to the application.
     * @throws IOException If there is an issue launching the application.
     */
    public static void main(String[] args) throws IOException {
        new Launch();
    }

}
