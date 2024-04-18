package edu.virginia.sde.hw5;
import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.*;
import java.util.Arrays;
public class Main {
    public static void main(String[] args){
        Configuration con = new Configuration();
        String databaseName = "bus_stops.sqlite";
        DatabaseDriver driver = new DatabaseDriver(databaseName);

//        System.out.println(con.getBusLinesURL());

        try {
            driver.connect();
            driver.clearTables();


            driver.createTables();

            BusLineReader busReader = new BusLineReader(con);
            List<BusLine> busLines = busReader.getBusLines();

            StopReader stopReader = new StopReader(con);
            List<Stop> stops = stopReader.getStops();

            driver.addStops(stops);
            driver.addBusLines(busLines);

//            List<Route> routes = new ArrayList<>();
//            for(int i = 0; i < busLines.size(); i ++){
//                routes.add(busLines.get(i).getRoute());
//            }

//            System.out.println("Number of stops: " + stops.size());
//            for (Stop stop : stops) {
//                System.out.println("Processing stop ID: " + stop.getId());
//            }
//
//            System.out.println("Number of bus lines: " + busLines.size());
//            for (BusLine busLine : busLines) {
//                System.out.println("Processing bus line ID: " + busLine.getId());
//            }

            driver.commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during database operations: " + e.getMessage());
            try {
                driver.rollback();
            } catch (SQLException re) {
                System.out.println("Error during rollback: " + re.getMessage());
            }
        } finally {
            try {
                driver.disconnect();
            } catch (SQLException e) {
                System.out.println("Error disconnecting from the database: " + e.getMessage());
            }
        }

            //testGetRecommendedBusLine_Stopexits_hasBusline
            Stop startStop = new Stop(4235108, "Alderman Rd @ O-Hill Dining Hall", 38.033937, -78.51424);
            Stop endStop = new Stop(4235106, "Alderman Rd @ Gooch/Dillard (Southbound)", 38.029305, -78.516414);
            BusLineService busLineService = new BusLineService(driver);
            Optional<BusLine> actualRecommendedBusLine = busLineService.getRecommendedBusLine(startStop, endStop);
            BusLine recommendedBusLine = new BusLine(4013976, true, "Orange Line", "Orange");
            // assertTrue(actualRecommendedBusLine.isPresent());
            // assertEquals(recommendedBusLine, actualRecommendedBusLine.get());**/
            boolean result=actualRecommendedBusLine.equals(recommendedBusLine);
            System.out.println(actualRecommendedBusLine);
            System.out.println(result);

        }
}