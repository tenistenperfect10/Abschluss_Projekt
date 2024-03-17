package org.texttechnology.parliament_browser_6_4.controller;


import freemarker.template.Configuration;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;

import java.io.IOException;

public class CommentController {

    private final InsightFactory insightFactory;
    private final Configuration cfg;

    public CommentController(InsightFactory insightFactory, Configuration cfg)
            throws IOException {
        this.insightFactory = insightFactory;
        this.cfg = cfg;
        initializeRoutes();
    }

    private void initializeRoutes() throws IOException {
    }
}
