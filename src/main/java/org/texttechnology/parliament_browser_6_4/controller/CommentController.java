package org.texttechnology.parliament_browser_6_4.controller;


import freemarker.template.Configuration;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;

import java.io.IOException;
/**
 * The {@code CommentController} class is responsible for managing the routes related to comments
 * in the application. It initializes the routes that handle the CRUD operations for comments
 * and configures them to use specific templates and data.
 * <p>
 * This controller utilizes an {@link InsightFactory} for accessing the data layer and a
 * {@link Configuration} instance from the FreeMarker template engine to render the UI.
 */
public class CommentController {
    /**
     * Factory for accessing comment data and insights.
     */

    private final InsightFactory insightFactory;
    /**
     * Configuration for the FreeMarker template engine.
     */
    private final Configuration cfg;
    /**
     * Constructs a new {@code CommentController} with the specified {@link InsightFactory} and
     * {@link Configuration}. It also initializes the routes for handling comment-related requests.
     *
     * @param insightFactory The factory for accessing data and insights.
     * @param cfg The FreeMarker configuration for rendering templates.
     * @throws IOException If there is an issue initializing the routes.
     */

    public CommentController( InsightFactory insightFactory, Configuration cfg)
            throws IOException {
        this.insightFactory = insightFactory;
        this.cfg = cfg;
        initializeRoutes();
    }
    /**
     * Initializes the web routes for comment-related operations. This method sets up the paths
     * and associated handlers that manage the viewing, creation, editing, and deletion of comments.
     *
     * @throws IOException If there is an error setting up the routes.
     */
    private void initializeRoutes() throws IOException {
    }
}
