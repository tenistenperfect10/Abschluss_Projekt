package org.texttechnology.parliament_browser_6_4.helper;

import java.io.*;
import java.util.Properties;

/**
 * A class that extends {@link java.util.Properties} to manage MongoDB configuration.
 * This class allows loading MongoDB configuration parameters such as hostname, username,
 * password, port, database name, and collection name from a properties file.
 * @author Giuseppe Abrami
 */
public class MongoDBConfig extends Properties {
    /**
     * Constructs a new {@code MongoDBConfig} by reading the specified properties file.
     *
     * @param sPath The path to the properties file containing MongoDB configuration.
     * @throws IOException If an I/O error occurs while reading the properties file.
     */
    public MongoDBConfig(String sPath) throws IOException {
        String current = new File( "." ).getCanonicalPath();
        BufferedReader lReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(sPath)), "UTF-8"));
        this.load(lReader);
        lReader.close();
    }



    /**
     * Retrieves the MongoDB hostname from the configuration.
     *
     * @return The MongoDB hostname, defaults to "127.0.0.1" if not specified.
     */
    public String getMongoHostname(){
        return getProperty("remote_host", "127.0.0.1");

    }

    /**
     * Retrieves the MongoDB username from the configuration.
     *
     * @return The MongoDB username, defaults to "user" if not specified.
     */
    public String getMongoUsername(){
        return getProperty("remote_user", "user");

    }

    /**
     * Retrieves the MongoDB password from the configuration.
     *
     * @return The MongoDB password, defaults to "password" if not specified.
     */
    public String getMongoPassword(){
        return getProperty("remote_password", "password");
    }

    /**
     * Retrieves the MongoDB port from the configuration.
     *
     * @return The MongoDB port as an integer, defaults to 27017 if not specified.
     */
    public int getMongoPort(){
        return Integer.valueOf(getProperty("remote_port", "27017"));
    }


    /**
     * Retrieves the MongoDB database name from the configuration.
     *
     * @return The MongoDB database name, defaults to "database" if not specified.
     */
    public String getMongoDatabase(){
        return getProperty("remote_database", "database");
    }

    /**
     * Retrieves the name of the MongoDB collection to connect to from the configuration.
     *
     * @return The MongoDB collection name, defaults to "collection" if not specified.
     */
    public String getMongoCollection(){
        return getProperty("remote_collection", "collection");
    }

}
