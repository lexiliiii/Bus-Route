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
            driver.createTables();
            driver.clearTables();




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

        /*testGetRecommendedBusLine_Stopexits_hasBusline1*/
        Stop startStop = new Stop(4235108, "Alderman Rd @ O-Hill Dining Hall", 38.033937, -78.51424);
        Stop endStop = new Stop(4235106, "Alderman Rd @ Gooch/Dillard (Southbound)", 38.029305, -78.516414);
        BusLineService busLineService = new BusLineService(driver);
        Optional<BusLine> actualRecommendedBusLine = busLineService.getRecommendedBusLine(startStop, endStop);
        BusLine recommendedBusLine1 = new BusLine(4013976, true, "Orange Line", "Orange");
        BusLine recommendedBusLine12 = new BusLine(4013970, true, "Green Line", "GRN");
        boolean result1=actualRecommendedBusLine.equals(recommendedBusLine12);
        System.out.println(actualRecommendedBusLine);
        System.out.println(result1);
        System.out.println("______________________________");
        /*testGetRecommendedBusLine_StopDoesNotExist*/
        Stop startStop2 = new Stop(00000, "Texas", 38.033937, -78.51424);
        Stop endStop2 = new Stop(11111, "Maryland", 38.029305, -78.516414);
        try {
            busLineService.getRecommendedBusLine(startStop2, endStop2);
            System.out.println("No IllegalArgumentException was thrown. Test failed.");
        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException was thrown. Test passed.");
        }
        System.out.println("______________________________");
        /*testGetRecommendedBusLine_Stopexits_hasBusline2*/
        Stop startStop3 = new Stop(4260150, "McCormick Rd @ Garrett Hall", 38.0342971845614, -78.5060574809856);
        Stop endStop3 = new Stop(4258644, "McCormick Rd @ Alderman Library", 38.035546, -78.505295);
        BusLine recommendedBusLine3 = new BusLine(4013970, true, "Green Line", "GRN");
        //there are two buslines 4013972, 4013970)4013970 is shorter
        Optional<BusLine> actualRecommendedBusLine3 = busLineService.getRecommendedBusLine(startStop3, endStop3);
        boolean result3=actualRecommendedBusLine3.equals(recommendedBusLine3);
        System.out.println(actualRecommendedBusLine3);
        System.out.println(recommendedBusLine3);
        System.out.println(result3);
        System.out.println("______________________________");

        /*testGetClosestStop*/
        double latitude = 38.049;
        double longitude = -78.5053;
        Stop result = busLineService.getClosestStop(latitude, longitude);
        Stop true_result = new Stop(4235116, "Arlington Blvd @ Barracks Rd Shopping Ctr", 38.048796, -78.505219);
        boolean result4=true_result.equals(result);
        System.out.println(result4);






    }


}