package org.texttechnology.parliament_browser_6_4.helper;

import spark.Request;
import spark.Response;
import spark.Session;

public class SessionsUtils {

    /**
     * 如果用户未登录，则重定向到登录页面
     * @param request 请求对象
     * @param response 响应对象
     */
    public static void redirectIfNotLogin(Request request, Response response) {
        Session session = request.session();
        String username = session.attribute("username");
        if (username == null) {
            response.redirect("/login");
        }
    }

    public static <T> T getSessionByKey(Request request, String key) {
        Session session = request.session();
        return session.attribute(key);
    }
}
