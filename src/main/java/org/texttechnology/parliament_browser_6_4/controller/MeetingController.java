package org.texttechnology.parliament_browser_6_4.controller;


import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.dao.UserDAO;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.configuration.FreemarkerBasedRoute;
import spark.Request;
import spark.Response;
import spark.Session;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static spark.Spark.get;

public class MeetingController {

    private final InsightFactory insightFactory;

    private final UserDAO userDAO;
    private final Configuration cfg;

    public MeetingController(InsightFactory insightFactory, UserDAO userDAO, Configuration cfg)
            throws IOException {
        this.insightFactory = insightFactory;

        this.userDAO = userDAO;
        this.cfg = cfg;
        initializeRoutes();
    }



    private void initializeRoutes() throws IOException {

        get("/", new FreemarkerBasedRoute("/", "index.ftl", cfg) {
            @Override
            public void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                Session session = request.session();
                String username = session.attribute("username");
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                Document document = userDAO.queryExistUser(username);
                if (document.get("userType", Integer.class).equals(1)) {
                    response.redirect("/userCenter");
                    return;
                }

                List<Document> lectureList = insightFactory.findByDateDescending(10);
                SimpleHash root = new SimpleHash();
                session.attribute("canEdit", document.get("canEdit", Integer.class));

                root.put("myLectures", lectureList);
                this.getTemplate().process(root, writer);
            }
        });

        get("/index", new FreemarkerBasedRoute("/index", "index.ftl", cfg) {
            @Override
            public void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                Session session = request.session();
                String username = session.attribute("username");
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                Document document = userDAO.queryExistUser(username);
                session.attribute("canEdit", document.get("canEdit", Integer.class));

                this.getTemplate().process(null, writer);
            }
        });


        // tells the user that the URL is dead
        get("/data_not_found", new FreemarkerBasedRoute("/data_not_found", "data_not_found.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                this.getTemplate().process(root, writer);
            }
        });

        get("/swagger", (req, res) -> {
            res.redirect("/swagger/index.html");
            return null;
        });
    }

}
