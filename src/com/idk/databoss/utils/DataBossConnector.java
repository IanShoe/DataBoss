package com.idk.databoss.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum DataBossConnector {

    INSTANCE;
    
    //This needs to be passed in by user
    //Maybe as 2 seperate things, file name and file path
    private static final String PROPERTIES_FILE_NAME = "connection.properties";

    public String getProperty(String key) {
        return loadPropertiesFile().getProperty(key);
    }

    private static Properties loadPropertiesFile() {
        InputStream is = null;
        try {
            is = DataBossConnector.class.getResourceAsStream(PROPERTIES_FILE_NAME);
            if (is == null) {
                throw new FileNotFoundException("Properties file '" + PROPERTIES_FILE_NAME + "' could not be located by the class loader.  Check class path and re-try.");
            }
            final Properties properties = new Properties();
            properties.load(is);
            return properties;
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    // Do nothing, couldn't close the input stream.
                }
            }
        }
    }
}
