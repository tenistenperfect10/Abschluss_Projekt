package org.texttechnology.parliament_browser_6_4.controller;


import cn.hutool.json.JSONUtil;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateException;
import org.bson.Document;
import org.texttechnology.parliament_browser_6_4.dao.UserDAO;
import org.texttechnology.parliament_browser_6_4.data.Impl.User_Impl;
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

public class UserController {

    private final UserDAO userDAO;
    private final Configuration cfg;

    public UserController(UserDAO userDAO, Configuration cfg)
            throws IOException {
        this.userDAO = userDAO;
        this.cfg = cfg;
        initializeRoutes();
    }

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
                User_Impl user = JSONUtil.toBean(request.body(), User_Impl.class);
                if (user.getUserType() == 1) {
                    if (user.getVerifyCode() == null || !user.getVerifyCode().equals("150624")) {
                        return Result.buildError("The secret key is wrong");
                    }
                }
                if (userDAO.queryExistUser(user.getUsername()) != null) {
                    return Result.buildError("The user is already exist");
                }
                // 管理员都具备编辑权限
                user.setCanEdit(user.getUserType());
                userDAO.saveUser(user);
                return Result.buildSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                return Result.buildError(e.getMessage());
            }
        });

        post("/api/login", (request, response) -> {
            response.type("application/json");
            try {
                Session session = request.session();
                User_Impl user = JSONUtil.toBean(request.body(), User_Impl.class);
                Document queryExistUser = userDAO.queryExistUser(user.getUsername());
                if (queryExistUser == null) {
                    return Result.buildError("The user is not exist");
                }
                if (!userDAO.matchUser(user)) {
                    return Result.buildError("Password is error");
                }
                session.attribute("username", user.getUsername());
                return Result.buildSuccess();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });

        post("/api/updatePwd", (request, response) -> {
            response.type("application/json");
            try {
                String username = request.queryParams("username");
                if (userDAO.queryExistUser(username) == null) {
                    return Result.buildError("The user is not exist");
                }
                String newPassword = request.queryParams("newPassword");
                String confirmNewPassword = request.queryParams("confirmNewPassword");
                if (!newPassword.equals(confirmNewPassword)) {
                    return Result.buildError("Password inconsistency");
                }
                User_Impl user = new User_Impl(username, newPassword, null, null);
                userDAO.updateUserPwd(user);
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
                List<Document> users = userDAO.findAllRegularUsers();

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
                Document queryUser = userDAO.queryExistUser(username);
                if (queryUser == null) {
                    return Result.buildError("The user is not exist");
                }
                User_Impl user = new User_Impl(username, null, null, null);
                if (queryUser.get("canEdit", Integer.class) == null || queryUser.get("canEdit", Integer.class) == 0) {
                    user.setCanEdit(1);
                } else {
                    user.setCanEdit(0);
                }
                userDAO.updateUserPermission(user);
                return Result.buildSuccess();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });

        post("/api/deleteUser", (request, response) -> {
            response.type("application/json");
            try {
                String username = request.queryParams("username");
                userDAO.deleteUserByUsername(username);
                return Result.buildSuccess();
            } catch (Exception e) {
                return Result.buildError(e.getMessage());
            }
        });
    }
}
