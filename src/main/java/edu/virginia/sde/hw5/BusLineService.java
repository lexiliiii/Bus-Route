package edu.virginia.sde.hw5;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.*;

public class BusLineService {
    private final DatabaseDriver databaseDriver;

    public BusLineService(DatabaseDriver databaseDriver) {
        this.databaseDriver = databaseDriver;
    }

    public void addStops(List<Stop> stops) {
        try {
            databaseDriver.connect();
            databaseDriver.addStops(stops);
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBusLines(List<BusLine> busLines) {
        try {
            databaseDriver.connect();
            databaseDriver.addBusLines(busLines);
            databaseDriver.disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BusLine> getBusLines() {
        try {
            databaseDriver.connect();
            var busLines = databaseDriver.getBusLines();
            databaseDriver.disconnect();
            return busLines;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Stop> getStops() {
        try {
            databaseDriver.connect();
            var stops = databaseDriver.getAllStops();
            databaseDriver.disconnect();
            return stops;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Route getRoute(BusLine busLine) {
        try {
            databaseDriver.connect();
            var stops = databaseDriver.getRouteForBusLine(busLine);
            databaseDriver.disconnect();
            return stops;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the closest stop to a given coordinate (using Euclidean distance, not great circle distance)
     * @param latitude - North/South coordinate (positive is North, Negative is South) in degrees
     * @param longitude - East/West coordinate (negative is West, Positive is East) in degrees
     * @return the closest Stop
     */
    public Stop getClosestStop(double latitude, double longitude) {
        List<Stop> stopList = getStops();
        double min = 100000000.0;
        Stop minStop = new Stop();

        for(Stop s : stopList){
            double temp = s.distanceTo(latitude, longitude);
            if(temp < min){
                min = temp;
                minStop = s;
            }
        }
        return minStop;
    }

    /**
     * Given two stop, a source and a destination, find the shortest (by distance) BusLine that starts
     * from source and ends at Destination.
     * @return Optional.empty() if no bus route visits both points
     * @throws IllegalArgumentException if either stop doesn't exist in the database
     */
    public Optional<BusLine> getRecommendedBusLine(Stop source, Stop destination) {
        List<Stop> stopList = getStops();
        if(!stopList.contains(source) || !stopList.contains(destination)){
            throw new IllegalArgumentException("Source or destination stop doesn't exist in the database");
        }

        double max = 0;
        BusLine maxBus = new BusLine();
        List<BusLine> busLineList = getBusLines();

        for(BusLine b : busLineList){
            Route tempRoute = getRoute(b);

            int start = 0;
            int end = 0;
            double distance = 0;

            if(tempRoute.contains(source) && tempRoute.contains(destination)){
                for(int i = 0; i < tempRoute.size(); i ++){
                    if(tempRoute.get(i).equals(source)){
                        start = i;
                    }
                    else if(tempRoute.get(i).equals(destination)){
                        end = i;
                    }
                }

                if(start < end){
                    for(int j = start; j < end -1; j ++){
                        distance += tempRoute.get(j).distanceTo(tempRoute.get(j+1));
                    }
                    if(distance > max){
                        max = distance;
                        maxBus = b;
                    }
                }

                else{
                    for(int a = end; a < start - 1; a ++){
                        distance += tempRoute.get(a).distanceTo(tempRoute.get(a+1));
                    }
                    if(tempRoute.getRouteDistance() - distance > max){
                        max = tempRoute.getRouteDistance() - distance;
                        maxBus = b;
                    }
                }
            }
        }

        if(maxBus == null){
            return Optional.empty();
        }

        return Optional.of(maxBus);
    }
}
