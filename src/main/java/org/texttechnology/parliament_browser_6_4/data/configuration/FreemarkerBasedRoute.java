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
/**
 * An abstract class that extends {@link RouteImpl} to provide a route implementation
 * based on Freemarker templates for rendering HTML views.
 * @author He Liu
 * @author Yu Ming
 */
public abstract class FreemarkerBasedRoute extends RouteImpl {

    private final Template template;
    private String path;
    /**
     * Constructs a new FreemarkerBasedRoute with the specified path, template name, and configuration.
     *
     * @param path         The path for which this route is registered.
     * @param templateName The name of the Freemarker template to be used for rendering the view.
     * @param cfg          The Freemarker configuration object used to get the template.
     * @throws IOException If an error occurs during template retrieval.
     */
    protected FreemarkerBasedRoute(final String path, final String templateName, Configuration cfg)
            throws IOException {
        super(path);
        this.path = path;
        template = cfg.getTemplate(templateName);
    }


    /**
     * Creates a Freemarker {@link Configuration} instance configured for loading templates
     * from the "/freemarker" directory.
     *
     * @return A {@link Configuration} instance for loading Freemarker templates.
     */
    public static Configuration createFreemarkerConfiguration() {
        Configuration retVal = new Configuration();
        retVal.setClassForTemplateLoading(FreemarkerBasedRoute.class, "/freemarker");
        return retVal;
    }


    /**
     * Handles the incoming request and processes the response using a Freemarker template.
     * This method initializes the query map, logs the request, calls the abstract {@code doHandle} method
     * for further processing, and logs the response.
     *
     * @param request  The {@link Request} object providing information about the HTTP request.
     * @param response The {@link Response} object providing functionality for modifying the HTTP response.
     * @return A {@link StringWriter} containing the processed template output.
     */
    @Override
    public Object handle(Request request, Response response) {
        StringWriter writer = new StringWriter();
        try {
            //Initialize queryMap
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



    /**
     * An abstract method to be implemented by subclasses for handling the request and rendering
     * the response using the provided {@link Writer}.
     *
     * @param request  The {@link Request} object providing information about the HTTP request.
     * @param response The {@link Response} object providing functionality for modifying the HTTP response.
     * @param writer   The {@link Writer} used to write the output of the Freemarker template.
     * @throws IOException        If an input or output exception occurred.
     * @throws TemplateException  If an exception occurred while processing the Freemarker template.
     */
    protected abstract void doHandle(final Request request, final Response response,
                                     final Writer writer) throws IOException, TemplateException;




    /**
     * Gets the Freemarker template associated with this route.
     *
     * @return The {@link Template} used for rendering the view.
     */
    public Template getTemplate() {
        return template;
    }
}
