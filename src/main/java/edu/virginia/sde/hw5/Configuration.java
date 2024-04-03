package edu.virginia.sde.hw5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class Configuration {
    public static final String configurationFilename = "config.json";

    private URL busStopsURL;

    private URL busLinesURL;

    private String databaseFilename;

    public Configuration() { }

    public URL getBusStopsURL() {
        if (busStopsURL == null) {
            parseJsonConfigFile();
        }
        return busStopsURL;
    }

    public URL getBusLinesURL() {
        if (busLinesURL == null) {
            parseJsonConfigFile();
        }
        return busLinesURL;
    }

    public String getDatabaseFilename() {
        if (databaseFilename == null) {
            parseJsonConfigFile();
        }
        return databaseFilename;
    }

    /**
     * Parse the JSON file config.json to set all three of the fields:
     *  busStopsURL, busLinesURL, databaseFilename
     */
    private void parseJsonConfigFile() {
        try (InputStream inputStream = Objects.requireNonNull(Configuration.class.getResourceAsStream(configurationFilename));
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            //TODO: Parse config.json to set the three fields
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
