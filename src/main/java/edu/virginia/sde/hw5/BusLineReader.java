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
    public List<BusLine> getBusLines() {
        List<BusLine> collection = new ArrayList<>();
        List<Stop> allStops = stopReader.getStops();

        var webServiceReader = new WebServiceReader(busLinesApiUrl);
        var json = webServiceReader.getJSONObject();
        JSONArray buslines = json.getJSONArray("lines");


        var webServiceReader1 = new WebServiceReader(busStopsApiUrl);
        var json_route = webServiceReader1.getJSONObject();
        JSONArray routes  = json_route.getJSONArray("routes");
        for (int i = 0; i < buslines.length(); i++) {
            JSONObject eachStop = buslines.getJSONObject(i);
            int id_buslines = eachStop.getInt("id");
            boolean isActive = eachStop.getBoolean("is_active");
            String longName = eachStop.getString("long_name");
            String shortName = eachStop.getString("short_name");

            for(int a = 0; a < routes.length(); a ++){
                JSONObject eachRoute = routes.getJSONObject(a);
                JSONArray stopList = eachRoute.getJSONArray("stops");
                int id_route = eachRoute.getInt("id");

                if(id_route == id_buslines){
                    Route tempRoute = new Route();
                    for(int j = 0; j < stopList.length(); j ++){
                        int temp_id = stopList.getInt(j);
                        for(Stop s: allStops){
                            if(s.getId() == temp_id){
                                tempRoute.add(s);
                            }
                        }
//                       allStops.contains(temp_id)
//                      System.out.println(temp_id);
                    }
//                        System.out.println(tempRoute);

                    collection.add(new BusLine(id_buslines, isActive, longName, shortName, tempRoute));
                }

            }
        }
        return collection;
    }
   /** public static void main(String[] args) {
        try {
            Configuration con = new Configuration();
            StopReader stop = new StopReader(con);
            BusLineReader bus = new BusLineReader(con);

            List<BusLine> xx = bus.getBusLines();
            System.out.println(xx);
            // System.out.println(stop);
            // List<Stop> temp = stop.getStops();
            // System.out.println(temp.size());
        } catch (Exception e) {
            e.printStackTrace(); // This will print the stack trace if any exceptions are caught.
        }*/
    }


}
