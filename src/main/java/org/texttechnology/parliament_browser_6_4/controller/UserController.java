package org.texttechnology.parliament_browser_6_4.controller;

import cn.hutool.json.JSONUtil;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.data.Impl.User_Impl;
import org.texttechnology.parliament_browser_6_4.data.InsightFactory;
import org.texttechnology.parliament_browser_6_4.data.configuration.FreemarkerBasedRoute;
import org.texttechnology.parliament_browser_6_4.helper.Result;
import org.texttechnology.parliament_browser_6_4.helper.SessionsUtils;
import spark.Request;
import spark.Response;
import spark.Session;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;
/**
 * Controller class for handling user-related operations like login, registration, password update, and user management.
 * It interacts with the InsightFactory to perform operations on user data and uses Freemarker templates for rendering views.
 */
public class UserController {

    private final InsightFactory insightFactory;
    private final Configuration cfg;

    /**
     * Constructs a UserController with the specified InsightFactory and Configuration for template rendering.
     * Initializes routes for user-related endpoints.
     *
     * @param insightFactory The InsightFactory for user data operations.
     * @param cfg The Configuration object for Freemarker template engine setup.
     * @throws IOException If an input or output exception occurred.
     */
    public UserController(InsightFactory insightFactory, Configuration cfg)
            throws IOException {
        this.insightFactory = insightFactory;
        this.cfg = cfg;
        initializeRoutes();
    }
    /**
     * Initializes routes for handling user operations such as login, registration, forgetting password, updating password, and user management.
     * This method sets up endpoints for both GET and POST requests to manage user-related functionalities.
     *
     * @throws IOException If an input or output exception occurred.
     */
    private void initializeRoutes() throws IOException {
        get("/login", new FreemarkerBasedRoute("/login", "login.ftl", cfg) {
            @Override
            public void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                this.getTemplate().process(null, writer);
            }
        });

        get("/register", new FreemarkerBasedRoute("/register", "register.ftl", cfg) {
            @Override
            public void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                this.getTemplate().process(null, writer);
            }
        });

        get("/forget", new FreemarkerBasedRoute("/forget", "forget.ftl", cfg) {
            @Override
            public void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {

                this.getTemplate().process(null, writer);
            }
        });

        post("/api/register", (request, response) -> {
            response.type("application/json");
            try {
                User_Impl userImpl = JSONUtil.toBean(request.body(), User_Impl.class);
                if (insightFactory.queryExistUser(userImpl.getUsername()) != null) {
                    return Result.buildError("The user is already exist");
                }
                // 管理员都具备编辑权限
                userImpl.setCanEdit(userImpl.getUserType());
                insightFactory.saveUser(userImpl);
                return Result.buildSuccess();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });

        post("/api/login", (request, response) -> {
            response.type("application/json");
            try {
                Session session = request.session();
                User_Impl userImpl = JSONUtil.toBean(request.body(), User_Impl.class);
                Document queryExistUser = insightFactory.queryExistUser(userImpl.getUsername());
                if (queryExistUser == null) {
                    return Result.buildError("The user is not exist");
                }
                if (!insightFactory.matchUser(userImpl)) {
                    return Result.buildError("Password is error");
                }
                session.attribute("username", userImpl.getUsername());
                return Result.buildSuccess();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });

        post("/api/updatePwd", (request, response) -> {
            response.type("application/json");
            try {
                String username = request.queryParams("username");
                if (insightFactory.queryExistUser(username) == null) {
                    return Result.buildError("The user is not exist");
                }
                String newPassword = request.queryParams("newPassword");
                String confirmNewPassword = request.queryParams("confirmNewPassword");
                if (!newPassword.equals(confirmNewPassword)) {
                    return Result.buildError("Password inconsistency");
                }
                User_Impl user = new User_Impl(username, newPassword, null, null);
                insightFactory.updateUserPwd(user);
                return Result.buildSuccess();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });

        get("/logout", new FreemarkerBasedRoute("/login", "login.ftl", cfg) {
            @Override
            public void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                Session session = request.session();
                session.invalidate();
                this.getTemplate().process(null, writer);
            }
        });

        get("/userCenter", new FreemarkerBasedRoute("/userCenter", "userCenter.ftl", cfg) {
            @Override
            protected void doHandle(Request request, Response response, Writer writer)
                    throws IOException, TemplateException {
                SessionsUtils.redirectIfNotLogin(request, response);
                String username = SessionsUtils.getSessionByKey(request, "username");
                List<Document> users = insightFactory.findAllRegularUsers();

                SimpleHash root = new SimpleHash();
                root.put("username", username);
                root.put("users", users);

                this.getTemplate().process(root, writer);
            }
        });

        post("/api/assign", (request, response) -> {
            response.type("application/json");
            try {
                String username = request.queryParams("username");
                Document queryUser = insightFactory.queryExistUser(username);
                if (queryUser == null) {
                    return Result.buildError("The user is not exist");
                }
                User_Impl userImpl = new User_Impl(username, null, null, null);
                if (queryUser.get("canEdit", Integer.class) == null || queryUser.get("canEdit", Integer.class) == 0) {
                    userImpl.setCanEdit(1);
                } else {
                    userImpl.setCanEdit(0);
                }
                insightFactory.updateUserPermission(userImpl);
                return Result.buildSuccess();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });

        post("/api/deleteUser", (request, response) -> {
            response.type("application/json");
            try {
                String username = request.queryParams("username");
                insightFactory.deleteUserByUsername(username);
                return Result.buildSuccess();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });
    }
}

