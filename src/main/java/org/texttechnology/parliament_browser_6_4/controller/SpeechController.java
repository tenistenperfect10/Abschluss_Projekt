package org.texttechnology.parliament_browser_6_4.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mongodb.client.AggregateIterable;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.Impl.LatexBuilder;
import org.texttechnology.parliament_browser_6_4.data.Impl.LatexSpeech_Impl;
import org.texttechnology.parliament_browser_6_4.data.Impl.Speech_mongoDB_Impl;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.Speech_mongoDB;
import org.texttechnology.parliament_browser_6_4.data.configuration.FreemarkerBasedRoute;
import org.texttechnology.parliament_browser_6_4.helper.Result;
import org.texttechnology.parliament_browser_6_4.helper.SessionsUtils;
import spark.Request;
import spark.Response;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * The SpeechController class is responsible for setting up and managing routes for handling requests related to speeches
 * in the Parliament Browser application. This includes displaying speech lists, speech details, performing speech searches,
 * updating speech records, and generating downloads for speech documents. It utilizes the Freemarker template engine
 * for rendering views and integrates various data handling and transformation operations.
 *
 * @author He Liu
 * @author Yu Ming
 */
public class SpeechController {



    private final Configuration cfg;

    private final InsightFactory insightFactory;
    /**
     * Constructs a SpeechController with specified {@link Configuration} and {@link InsightFactory}.
     * Initializes routes for handling HTTP requests related to speeches.
     *
     * @param cfg the Configuration instance for template engine settings
     * @param insightFactory the InsightFactory instance for data manipulation and access
     * @throws IOException if an input or output exception occurs
     */
    public SpeechController(Configuration cfg, InsightFactory insightFactory)
            throws IOException {

        this.cfg = cfg;
        this.insightFactory = insightFactory;
        initializeRoutes();
    }

    /**
     * Defines various routes for a web application focused on managing speeches, using the Freemarker template engine
     * for dynamic content rendering. This includes routes for displaying speech lists, details, search functionality,
     * speech updates, and downloads.
     *
     * @throws IOException if an input or output exception occurs
     */
    private void initializeRoutes() throws IOException {
        get("/speech", new FreemarkerBasedRoute("/speech", "speech.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                SessionsUtils.redirectIfNotLogin(request, response);
                // Building Aggregate Queries
                AggregateIterable<Document> speechList = insightFactory.aggregate();

                SimpleHash root = new SimpleHash();

                // Converting an AggregateIterable to a List
                List<Document> resultList = new ArrayList<>();
                speechList.into(resultList);
                System.out.println("total speech num is " + resultList.size());
                // Conversion to cascading multi-level menu forms
                Map<String, Map<String, List<Document>>> resultMap = convertCascadeMap(resultList);

                root.put("speechMap", resultMap);
                this.getTemplate().process(root, writer);
            }
        });

        get("/speechDetail/:id", new FreemarkerBasedRoute("/speechDetail/:id", "speechDetail.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                SessionsUtils.redirectIfNotLogin(request, response);
                Integer canEdit = SessionsUtils.getSessionByKey(request, "canEdit");

                String id = StringEscapeUtils.escapeHtml4(request.params(":id"));
                Document speech = insightFactory.findSpeechById(id);

                Document speaker = new Document();
                if(StrUtil.isNotEmpty(speech.getString("speaker"))){
                    speaker = insightFactory.findSpeakerById(speech.getString("speaker"));
                }
                List<String>  commentIds = speech.getList("comments", String.class);

                List<Document> commentList = new ArrayList<>();
                if(CollUtil.isNotEmpty(commentIds)){
                    AggregateIterable<Document> aggregateIterable = insightFactory.findByIdsWithSpeaker(commentIds);
                    aggregateIterable.into(commentList);
                }
                Date startDate = new Date(speech.get("starttime", Long.class));
                Date endDate = new Date(speech.get("endtime", Long.class));

                // Create a SimpleDateFormat object that specifies the desired formatting
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

                // Converting a Date object to a string using the format method
                String formattedStartDate = sdf.format(startDate);
                String formattedEndDate = sdf.format(endDate);

                SimpleHash root = new SimpleHash();

                root.put("speech", speech);
                root.put("speaker", speaker);
                root.put("commentList", commentList);

                root.put("startDate", formattedStartDate);
                root.put("endDate", formattedEndDate);
                root.put("canEdit", canEdit);

                this.getTemplate().process(root, writer);
            }
        });

        post("/speech/search", new FreemarkerBasedRoute("/speech", "speech.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                SessionsUtils.redirectIfNotLogin(request, response);

                String startTime = StringEscapeUtils.escapeHtml4(request.queryParams("startTime"));
                String endTime = StringEscapeUtils.escapeHtml4(request.queryParams("endTime"));

                // Create a SimpleDateFormat object that specifies the desired formatting
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Date startDate = null;
                Date endDate = null;
                String errorMsg = "";

                try {
                    startDate = sdf.parse(startTime);
                    endDate = sdf.parse(endTime);
                    if (startDate.after(endDate)) {
                        errorMsg += "The start time cannot be greater than the end time!";
                    }
                } catch (Exception e) {
                    errorMsg += "Time format error!";
                }

                SimpleHash root = new SimpleHash();

                if(StrUtil.isNotBlank(errorMsg)){
                    root.put("errorMsg", errorMsg);
                    this.getTemplate().process(root, writer);
                    return;
                }

                AggregateIterable<Document> speechList = insightFactory.searchSpeech(startDate, endDate);

                // Converting an AggregateIterable to a List
                List<Document> resultList = new ArrayList<>();
                speechList.into(resultList);
                System.out.println("search speech num is " + resultList.size());
                // Conversion to cascading multi-level menu forms
                Map<String, Map<String, List<Document>>> resultMap = convertCascadeMap(resultList);


                root.put("speechMap", resultMap);
                root.put("errorMsg", errorMsg);
                root.put("startDate", startTime);
                root.put("endDate", endTime);

                this.getTemplate().process(root, writer);
            }
        });

        get("/speech/query", new FreemarkerBasedRoute("/speech", "speech.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                SessionsUtils.redirectIfNotLogin(request, response);
                String keyword = request.queryParams("keyword");
                List<Document> speeches = insightFactory.globalQueryByKeyword(keyword).into(new ArrayList<>());

                Map<String, Map<String, List<Document>>> resultMap = convertCascadeMap(speeches);

                SimpleHash root = new SimpleHash();
                root.put("speechMap", resultMap);
                this.getTemplate().process(root, writer);
            }
        });

        post("/api/speech/update", (request, response) -> {
            response.type("application/json");
            try {
                JSONObject obj = JSONUtil.parseObj(request.body());
                String id = (String) obj.remove("_id");
                boolean success = insightFactory.updateSpeechById(id, JSONUtil.toJsonStr(obj));
                return success ? Result.buildSuccess() : Result.buildError();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });

        get("/speech/download", (request, response) -> {
            String pdfDirectory = "src/main/resources/pdf/";
            String downloadFile = null;
            try {
                /** Query results and transform into LatexSpeech objects */
                String protocol = request.queryParams("protocol");
                List<Document> speeches = insightFactory.queryDownloadSpeeches(protocol).into(new ArrayList<>());
                Map<String, List<Document>> resultMap = convertCascadeMap(speeches).get(protocol);
                Map<String, List<Speech_mongoDB>> speechMap = new HashMap<>();
                resultMap.forEach((key, list) -> {
                    List<Speech_mongoDB> speechList = list.stream().map(document -> {
                        Speech_mongoDB_Impl speech = new Speech_mongoDB_Impl();
                        speech.setSpeaker(document.getString("speakerName"));
                        speech.setText(document.getString("text"));
                        List<Document> commentsDocs = insightFactory.findByIds(document.getList("comments", String.class));
                        List<String> comments = commentsDocs.stream().map(doc -> doc.getString("text")).collect(Collectors.toList());
                        speech.setComments(comments);
                        return speech;
                    }).collect(Collectors.toList());
                    speechMap.put(key, speechList);
                });
                LatexSpeech_Impl latexSpeech = new LatexSpeech_Impl();
                latexSpeech.setTitle(protocol);
                latexSpeech.setSpeechMap(speechMap);
                /** Generate a pdf and pass it back to the front end */
                LatexBuilder latexBuilder = new LatexBuilder();
                downloadFile = latexBuilder.build(latexSpeech);
                System.out.println("after build: " + downloadFile);
                response.header("Content-Disposition", "attachment; " + "filename=" +  downloadFile);
                InputStream inputStream = new FileInputStream(pdfDirectory + downloadFile);
                // Writes the contents of the file to the response output stream
                OutputStream outputStream = response.raw().getOutputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // Closing the input and output streams
                inputStream.close();
                outputStream.close();

                return response.raw();
            } catch (Exception e) {
                System.out.println("error downloadFile: " + downloadFile);
                e.printStackTrace();
                return Result.buildError(e.getMessage());
            }
        });

    }



    /**
     * Transforms a list of Document objects into a multi-level cascading map suitable for collapsing and expanding the display
     * in the UI. This structure organizes speeches by protocol and index, facilitating the grouping of speeches under specific
     * headings and subheadings for easier navigation and interaction.
     *
     * @param resultList the list of Document objects representing speech records
     * @return a cascading map organized by protocol and index, grouping speeches accordingly
     */
    private Map<String, Map<String, List<Document>>> convertCascadeMap(List<Document> resultList) {
        Map<String, Map<String, List<Document>>> resultMap = new HashMap<>();
        resultList.stream().forEach(result -> {
//            String key = result.getString("speakerId") + "&" + result.getString("speakerName");
            String key = result.getString("title");
            if (!resultMap.containsKey(key)) {
                Map<String, List<Document>> titleMap = new HashMap<>();
                List<Document> documents = new ArrayList<>();
                documents.add(result);
                titleMap.put(result.getString("index"), documents);
                resultMap.put(key, titleMap);
            } else {
                Map<String, List<Document>> titleMap = resultMap.get(key);
                if (!titleMap.containsKey(result.getString("index"))) {
                    List<Document> documents = new ArrayList<>();
                    documents.add(result);
                    titleMap.put(result.getString("index"), documents);
                } else {
                    titleMap.get(result.getString("index")).add(result);
                }
            }
        });
        return resultMap;
    }
}
