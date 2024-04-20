package edu.virginia.sde.hw5;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
            var webServiceReader = new WebServiceReader(busStopsApiUrl);
            var json = webServiceReader.getJSONObject();

            JSONArray stops = json.getJSONArray("stops");

            for (int i = 0; i < stops.length(); i++) {
                JSONObject eachStop = stops.getJSONObject(i);

                int id = eachStop.getInt("id");
                String name = eachStop.getString("name");
                JSONArray position = eachStop.getJSONArray("position");
                double latitude = position.getDouble(0);
                double longitude = position.getDouble(1);

                collection.add(new Stop(id, name, latitude, longitude));
            }

        return collection;
    }


//    public static void main(String[] args){
//        try {
//            Configuration con = new Configuration();
//            StopReader stop = new StopReader(con);
//            List<Stop> temp = stop.getStops();
//            System.out.println(temp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
