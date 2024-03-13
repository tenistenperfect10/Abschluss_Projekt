package org.texttechnology.parliament_browser_6_4.data.configuration;

import cn.hutool.json.JSONUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import spark.Request;
import spark.Response;
import spark.RouteImpl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public abstract class FreemarkerBasedRoute extends RouteImpl {

    private final Template template;
    private String path;

    protected FreemarkerBasedRoute(final String path, final String templateName, Configuration cfg)
            throws IOException {
        super(path);
        this.path = path;
        template = cfg.getTemplate(templateName);
    }

    public static Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(FreemarkerBasedRoute.class, "/freemarker");
        return retVal;
    }

    @Override
    public Object handle(Request request, Response response) {
        StringWriter writer = new StringWriter();
        try {
            //初始化queryMap
            request.queryMap();
            //LogDAO.getInstance().addLog("request", path, request.url(), request.requestMethod(), JSONUtil.toJsonStr(request.queryMap().toMap()), request.ip());
            doHandle(request, response, writer);
            //LogDAO.getInstance().addLog("response", path, request.url(), request.requestMethod(), writer.toString(), request.ip());
        } catch (Exception e) {
            e.printStackTrace();
            response.redirect("/internal_error");
        }
        return writer;
    }

    protected abstract void doHandle(final Request request, final Response response,
                                     final Writer writer) throws IOException, TemplateException;

    public Template getTemplate() {
        return template;
    }
}
