package org.texttechnology.parliament_browser_6_4.controller;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.configuration.FreemarkerBasedRoute;
import spark.Request;
import spark.Response;
import spark.Session;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static spark.Spark.get;
/**
 * The {@code MeetingController} class is responsible for managing the routes associated with meeting-related views and actions.
 * It utilizes the {@link InsightFactory} for accessing data related to meetings and the {@link Configuration} object from FreeMarker
 * to render HTML templates. This controller sets up the endpoints for actions such as viewing meeting details, listing meetings,
 * and other functionalities related to meetings within the application.
 */
public class MeetingController {
    /**
     * An instance of {@link InsightFactory} for accessing meeting-related data.
     */
    private final InsightFactory insightFactory;
    /**
     * The FreeMarker {@link Configuration} instance used for rendering views.
     */
    private final Configuration cfg;
    /**
     * Constructs a {@code MeetingController} with a specified {@link InsightFactory} and {@link Configuration}.
     * It initializes routes relevant to meeting management.
     *
     * @param insightFactory The factory for accessing meeting data.
     * @param cfg The FreeMarker configuration for template rendering.
     * @throws IOException If an I/O error occurs during route initialization.
     */
    public MeetingController(InsightFactory insightFactory, Configuration cfg)
            throws IOException {
        this.insightFactory = insightFactory;
        this.cfg = cfg;
        initializeRoutes();
    }


    /**
     * Initializes the web routes for handling meeting-related requests. This includes:
     * - Redirecting unauthenticated users to the login page.
     * - Redirecting users with specific roles to appropriate pages.
     * - Handling requests to the root ("/") and index ("/index") routes by rendering the meetings list.
     * - Providing a custom "not found" page for dead links through the "/data_not_found" route.
     *
     * @throws IOException If an error occurs in setting up the routes or during template processing.
     */
    private void initializeRoutes() throws IOException {
        // Route for the application's root that displays lectures or redirects based on user status.
        get("/", new FreemarkerBasedRoute("/", "index.ftl", cfg) {
            @Override
            public void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                // Authentication and redirection logic.
                // Display lectures if user is authenticated and has the correct permissions.

                Session session = request.session();
                String username = session.attribute("username");
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                Document document = insightFactory.queryExistUser(username);
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
            /**
             * Handles requests to the home page ("/"). It checks if the user is logged in and redirects
             * accordingly. If the user is logged in and has the correct permissions, it retrieves and displays
             * a list of lectures.
             *
             * @param request  The HTTP request.
             * @param response The HTTP response.
             * @param writer   The writer to output the rendered template.
             * @throws IOException If an I/O error occurs.
             * @throws TemplateException If an error occurs in template processing.
             */
            @Override
            public void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                Session session = request.session();
                String username = session.attribute("username");
                if (username == null) {
                    response.redirect("/login");
                    return;
                }
                Document document = insightFactory.queryExistUser(username);
                session.attribute("canEdit", document.get("canEdit", Integer.class));

                this.getTemplate().process(null, writer);
            }
        });


        // tells the user that the URL is dead
        get("/data_not_found", new FreemarkerBasedRoute("/data_not_found", "data_not_found.ftl", cfg) {
            /**
             * Provides a custom "not found" page for dead links.
             *
             * @param request  The HTTP request.
             * @param response The HTTP response.
             * @param writer   The writer to output the rendered template.
             * @throws IOException If an I/O error occurs.
             * @throws TemplateException If an error occurs in template processing.
             */
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                SimpleHash root = new SimpleHash();
                this.getTemplate().process(root, writer);
            }
        });
    }

}
