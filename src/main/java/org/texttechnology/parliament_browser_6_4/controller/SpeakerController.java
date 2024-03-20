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
 *  set up and manage routes for handling requests related to speakers
 * @author Yu Ming
 * @author He Liu
 */
public class SpeakerController {

    private final InsightFactory insightFactory;
    private final Configuration cfg;

    public SpeakerController(InsightFactory insightFactory, Configuration cfg)
            throws IOException {
        this.insightFactory = insightFactory;
        this.cfg = cfg;
        initializeRoutes();
    }

    /**
     *  defines route handlers for a web application managing speakers,
     *  using the Freemarker template engine for rendering views.
     * @throws IOException
     */
    private void initializeRoutes() throws IOException {

        before("/speaker/*", (request, response) -> {
            Session session = request.session();
            if (session.attribute("username") == null) {
                response.redirect("/login");
            }
        });

        get("/speaker", new FreemarkerBasedRoute("/speaker", "speaker.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                SessionsUtils.redirectIfNotLogin(request, response);
                Integer canEdit = SessionsUtils.getSessionByKey(request, "canEdit");
                System.out.println(canEdit);

                List<Document> speakerList = insightFactory.findAllSpeaker();

                SimpleHash root = new SimpleHash();

                root.put("speakerList", speakerList);
                root.put("canEdit", canEdit);

                this.getTemplate().process(root, writer);
            }
        });

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
                    speakerList.add(insightFactory.findSpeakerById(id));
                }else{
                    speakerList.addAll(insightFactory.searchSpeaker(name, firstName, fraction, party));
                }

                SimpleHash root = new SimpleHash();

                root.put("speakerList", speakerList);
                root.put("canEdit", canEdit);

                this.getTemplate().process(root, writer);
            }
        });

        post("/api/speaker/save", (request, response) -> {
            response.type("application/json");
            try {
                JSONObject obj = JSONUtil.parseObj(request.body());
                System.out.println(obj);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                // Parses a string and converts it to a Date object
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
