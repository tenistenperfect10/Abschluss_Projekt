package org.texttechnology.parliament_browser_6_4.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.configuration.FreemarkerBasedRoute;
import org.texttechnology.parliament_browser_6_4.helper.Result;
import org.texttechnology.parliament_browser_6_4.helper.SessionsUtils;
import spark.Request;
import spark.Response;
import spark.Session;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.*;

import static spark.Spark.*;


/**
 * Controller for handling routes related to speaker information within the application.
 * It utilizes InsightFactory for accessing data and Configuration for rendering FreeMarker templates.
 */
public class SpeakerController {

    private final InsightFactory insightFactory;
    private final Configuration cfg;

    /**
     * Constructs a SpeakerController with specified data access and template rendering configurations.
     * Initializes routes for speaker-related actions.
     *
     * @param insightFactory The factory for accessing speaker data.
     * @param cfg The FreeMarker configuration for template rendering.
     * @throws IOException If an I/O error occurs during route initialization.
     */
    public SpeakerController(InsightFactory insightFactory, Configuration cfg) throws IOException {
        this.insightFactory = insightFactory;
        this.cfg = cfg;
        initializeRoutes();
    }

    /**
     * Initializes the web routes for speaker-related functionalities including viewing speaker details,
     * searching for speakers, and saving speaker data.
     *
     * @throws IOException If an error occurs in setting up the routes or during template processing.
     */
    private void initializeRoutes() throws IOException {

        before("/speaker/*", (request, response) -> {
            Session session = request.session();
            if (session.attribute("username") == null) {
                response.redirect("/login");
            }
        });

        // Route for displaying the speaker list page
        get("/speaker", new FreemarkerBasedRoute("/speaker", "speaker.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                SessionsUtils.redirectIfNotLogin(request, response);
                Integer canEdit = SessionsUtils.getSessionByKey(request, "canEdit");
                System.out.println(canEdit);

                List<Document> speakerList = insightFactory.findAll();

                SimpleHash root = new SimpleHash();

                root.put("speakerList", speakerList);
                root.put("canEdit", canEdit);

                this.getTemplate().process(root, writer);
            }
        });

        // Route for displaying detailed information about a specific speaker
        get("/speakerDetail/:id", new FreemarkerBasedRoute("/speakerDetail/:id", "speakerDetail.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                SessionsUtils.redirectIfNotLogin(request, response);

                String id = StringEscapeUtils.escapeHtml4(request.params(":id"));
                Document speaker = insightFactory.findByIdAggregate(id);

                SimpleHash root = new SimpleHash();

                root.put("speaker", speaker);

                this.getTemplate().process(root, writer);
            }
        });

        // Route for handling speaker search functionality
        post("/speaker/search", new FreemarkerBasedRoute("/speaker", "speaker.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                SessionsUtils.redirectIfNotLogin(request, response);
                Integer canEdit = SessionsUtils.getSessionByKey(request, "canEdit");

                String id = StringEscapeUtils.escapeHtml4(request.queryParams("id"));
                String name = StringEscapeUtils.escapeHtml4(request.queryParams("name"));
                String firstName = StringEscapeUtils.escapeHtml4(request.queryParams("firstName"));
                String fraction = StringEscapeUtils.escapeHtml4(request.queryParams("fraction"));
                String party = StringEscapeUtils.escapeHtml4(request.queryParams("party"));

                List<Document> speakerList = new ArrayList<>();
                if(StrUtil.isNotBlank(id)){
                    speakerList.add(insightFactory.findById(id));
                }else{
                    speakerList.addAll(insightFactory.search(name, firstName, fraction, party));
                }

                SimpleHash root = new SimpleHash();

                root.put("speakerList", speakerList);
                root.put("canEdit", canEdit);

                this.getTemplate().process(root, writer);
            }
        });
        // API endpoint for saving or updating speaker information
        post("/api/speaker/save", (request, response) -> {
            response.type("application/json");
            try {
                JSONObject obj = JSONUtil.parseObj(request.body());
                System.out.println(obj);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                // Parsing strings and converting them to Date objects
                Date geburtsdatum = null;
                Date sterbedatum = null;
                if (!JSONNull.NULL.equals(obj.get("geburtsdatum"))) {
                    geburtsdatum = sdf.parse((String) obj.remove("geburtsdatum"));
                }
                if (!JSONNull.NULL.equals(obj.get("sterbedatum"))) {
                    sterbedatum =  sdf.parse((String) obj.remove("sterbedatum"));
                }
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("geburtsdatum", geburtsdatum);
                fieldMap.put("sterbedatum", sterbedatum);

                String speechID = obj.get("id", String.class);
                System.out.println(speechID);
                if (speechID != null) {
                    System.out.println("update: " + speechID);
                    obj.remove("id");
                    insightFactory.updateSpeakerById(new ObjectId(speechID), JSONUtil.toJsonStr(obj));
                    insightFactory.updateByFieldMap(speechID, fieldMap);
                    return Result.buildSuccess();
                }
                System.out.println("end method now insert");
                ObjectId id = insightFactory.save(Document.parse(JSONUtil.toJsonStr(obj)));

                insightFactory.updateByFieldMap(id, fieldMap);
                return Result.buildSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error: " + e.getMessage());
                return Result.buildError(e.getMessage());
            }
        });
    }
}
