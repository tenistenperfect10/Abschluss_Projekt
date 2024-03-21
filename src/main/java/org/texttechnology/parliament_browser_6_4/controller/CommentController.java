package org.texttechnology.parliament_browser_6_4.controller;


import freemarker.template.Configuration;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;

import java.io.IOException;

/**
 *  set up and manage routes for handling requests related to comments
 * @author Yu Ming
 * @author He Liu
 */
public class CommentController {

    private final InsightFactory insightFactory;
    private final Configuration cfg;
    /**
     * Constructs a CommentController with specified {@link InsightFactory} and {@link Configuration}.
     * This constructor also calls {@link #initializeRoutes()} to set up the necessary routes for comment management.
     *
     * @param insightFactory the InsightFactory instance used for data manipulation and access
     * @param cfg the Configuration instance used for setting up and configuring templates
     * @throws IOException if an input or output exception occurred
     */

    public CommentController(InsightFactory insightFactory, Configuration cfg)
            throws IOException {
        this.insightFactory = insightFactory;
        this.cfg = cfg;
        initializeRoutes();
    }
    /**
     * Initializes the routes required for handling comment-related requests. This method sets up the endpoints
     * that respond to various actions (e.g., creating, reading, updating, and deleting comments) within the application.
     * Specific routes and their implementations should be defined within this method.
     *
     * @throws IOException if an input or output exception occurred
     */
    private void initializeRoutes() throws IOException {
    }
}
