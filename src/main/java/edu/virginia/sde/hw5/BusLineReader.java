package edu.virginia.sde.hw5;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BusLineReader {
    private final URL busLinesApiUrl;
    private final URL busStopsApiUrl;

    /* You'll need this to get the Stop objects when building the Routes object */
    private final StopReader stopReader;
    /**
     * Returns a list of BusLine objects. This is a "deep" list, meaning all the BusLine objects
     * already have their Route objects fully populated with that line's Stops.
     */

    public BusLineReader(Configuration configuration) {
        this.busStopsApiUrl = configuration.getBusStopsURL();
        this.busLinesApiUrl = configuration.getBusLinesURL();
        stopReader = new StopReader(configuration);
    }

    /**
     * This method returns the BusLines from the API service, including their
     * complete Routes.
     */
    public List<BusLine> getBusLines() {//TODO: implement
        List<BusLine> collection = new ArrayList<>();
        List<Stop> allStops = stopReader.getStops();

        try(var inputStream = busLinesApiUrl.openStream();
            var inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            var bufferedReader = new BufferedReader(inputStreamReader)) {
            try(var inputStream1 = busStopsApiUrl.openStream();
                var inputStreamReader1 = new InputStreamReader(inputStream1, StandardCharsets.UTF_8);
                var bufferedReader1 = new BufferedReader(inputStreamReader1)) {

                    var fileContents = bufferedReader.lines().collect(Collectors.joining("\n"));
                    var fileContents1 = bufferedReader1.lines().collect(Collectors.joining("\n"));

                    JSONObject json = new JSONObject(new JSONTokener(fileContents));
                    JSONArray buslines = json.getJSONArray("lines");
                    JSONObject json_route = new JSONObject(new JSONTokener(fileContents1));
                    JSONArray routes = json_route.getJSONArray("routes");

                    for (int i = 0; i < buslines.length(); i++) {
                        JSONObject eachStop = buslines.getJSONObject(i);
                        int id_buslines = eachStop.getInt("id");
                        boolean isActive = eachStop.getBoolean("is_active");
                        String longName = eachStop.getString("long_name");
                        String shortName = eachStop.getString("short_name");

                        JSONObject eachRoute = routes.getJSONObject(i);
                        JSONArray stopList = eachRoute.getJSONArray("stops");
                        int id_route = eachRoute.getInt("id");
//                        System.out.println(stopList);
//                        System.out.println(allStops);

                        if(id_route == id_buslines){
                            Route tempRoute = new Route();

                            for(int j = 0; j < stopList.length(); j ++){
                                int temp_id = stopList.getInt(j);
                                for(Stop s: allStops){
                                    if(s.getId() == temp_id){
                                        tempRoute.add(s);
                                    }
                                }
//                            allStops.contains(temp_id)
//                            System.out.println(temp_id);
                            }
//                        System.out.println(tempRoute);

                            collection.add(new BusLine(id_buslines, isActive, longName, shortName, tempRoute));
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
        }
        return collection;
    }

//    public static void main(String[] args){
////        Configuration con = new Configuration();
////        con.parseJsonConfigFile();
//        try {
//            Configuration con = new Configuration();
//            StopReader stop = new StopReader(con);
//            BusLineReader bus = new BusLineReader(con);
//
//            List<BusLine> xx = bus.getBusLines();
//            System.out.println(xx);
////            System.out.println(stop);
////            List<Stop> temp = stop.getStops();
////            System.out.println(temp.size());
//        } catch (Exception e) {
//            e.printStackTrace(); // This will print the stack trace if any exceptions are caught.
//        }
//    }
}
