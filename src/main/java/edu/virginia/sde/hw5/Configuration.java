package edu.virginia.sde.hw5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.stream.Collectors;

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
            String jsonString = bufferedReader.lines().collect(Collectors.joining());

            JSONObject json = new JSONObject(new JSONTokener(jsonString));

            JSONObject endpoints = json.getJSONObject("endpoints");
            busStopsURL = new URL(endpoints.getString("stops"));
            busLinesURL = new URL(endpoints.getString("lines"));
            databaseFilename = json.getString("database");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void main(String[] args){
////        Configuration con = new Configuration();
////        con.parseJsonConfigFile();
//        try {
//            Configuration con = new Configuration();
//            System.out.println("Bus Stops URL: " + con.getBusStopsURL());
//            System.out.println("Bus Lines URL: " + con.getBusLinesURL());
//            System.out.println("Database Filename: " + con.getDatabaseFilename());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
