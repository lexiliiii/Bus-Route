package edu.virginia.sde.hw5;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.net.HttpURLConnection;

public class StopReader {

    private final URL busStopsApiUrl;

    public StopReader(Configuration configuration) {
        this.busStopsApiUrl = configuration.getBusStopsURL();
    }

    /**
     * Read all the stops from the "stops" json URL from Configuration Reader
     * @return List of stops
     */
    public List<Stop> getStops() {
        List<Stop> collection = new ArrayList<>();
        //TODO: implement
        try(var inputStream = busStopsApiUrl.openStream();
            var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            var bufferedReader = new BufferedReader(inputStreamReader)) {
            var fileContents = bufferedReader.lines().collect(Collectors.joining("\n"));

            JSONObject json = new JSONObject(new JSONTokener(fileContents));
            JSONArray stops = json.getJSONArray("stops");
            for(int i = 0; i < stops.length(); i ++){
                JSONObject eachStop = stops.getJSONObject(i);
                int id = eachStop.getInt("id");
                String name = eachStop.getString("name");
                JSONArray position = eachStop.getJSONArray("position");
                double latitude = position.getDouble(0);
                double longitude = position.getDouble(1);
                collection.add(new Stop(id, name, latitude, longitude));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return collection;
    }


//        public static void main(String[] args){
////        Configuration con = new Configuration();
////        con.parseJsonConfigFile();
//        try {
//            Configuration con = new Configuration();
//            StopReader stop = new StopReader(con);
//            List<Stop> temp = stop.getStops();
//            System.out.println(temp);
//        } catch (Exception e) {
//            e.printStackTrace(); // This will print the stack trace if any exceptions are caught.
//        }
//    }
}
