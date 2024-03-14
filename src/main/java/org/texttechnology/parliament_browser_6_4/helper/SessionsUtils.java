package org.texttechnology.parliament_browser_6_4.helper;

import spark.Request;
import spark.Response;
import spark.Session;
/**
 * Utilities class for handling session-related operations within the application.
 * This class provides static methods to assist with session management, such as
 * redirecting users who are not logged in to the login page and retrieving session
 * attributes by key.
 */

public class SessionsUtils {


    /**
     * Redirects the user to the login page if they are not currently logged in.
     * This method checks the user's session for a "username" attribute and redirects
     * to the login page if it is not found, indicating the user is not logged in.
     *
     * @param request The request object, representing the HTTP request.
     * @param response The response object, used to redirect the user to the login page.
     */
    public static void redirectIfNotLogin(Request request, Response response) {
        Session session = request.session();
        String username = session.attribute("username");
        if (username == null) {
            response.redirect("/login");
        }
    }


    /**
     * Retrieves an attribute from the user's session using a specified key.
     * This generic method returns the value of the session attribute corresponding
     * to the given key, or null if the attribute does not exist.
     *
     * @param <T> The expected type of the session attribute.
     * @param request The request object, containing the user's session.
     * @param key The key of the session attribute to retrieve.
     * @return The value of the session attribute, or null if not present.
     */
    public static <T> T getSessionByKey(Request request, String key) {
        Session session = request.session();
        return session.attribute(key);
    }
}
