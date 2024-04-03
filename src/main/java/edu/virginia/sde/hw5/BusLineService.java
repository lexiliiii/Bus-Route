package edu.virginia.sde.hw5;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
        //TODO: implement
        return null;
    }

    /**
     * Given two stop, a source and a destination, find the shortest (by distance) BusLine that starts
     * from source and ends at Destination.
     * @return Optional.empty() if no bus route visits both points
     * @throws IllegalArgumentException if either stop doesn't exist in the database
     */
    public Optional<BusLine> getRecommendedBusLine(Stop source, Stop destination) {
        //TODO: implement
        return Optional.empty();
    }
}
