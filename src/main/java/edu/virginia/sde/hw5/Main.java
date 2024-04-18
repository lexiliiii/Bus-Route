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

        try {
            driver.connect();

            if(driver != null){
                driver.clearTables();
            }

            driver.createTables();

            BusLineReader busReader = new BusLineReader(con);
            List<BusLine> busLines = busReader.getBusLines();

            StopReader stopReader = new StopReader(con);
            List<Stop> stops = stopReader.getStops();

            List<BusLine> bus = driver.getBusLines();
            System.out.println(bus);
//            List<Stop> stop = driver.getAllStops();
//            System.out.println(stop);

            driver.addStops(stops);
            driver.addBusLines(busLines);

//            List<Route> routes = new ArrayList<>();
//            for(int i = 0; i < busLines.size(); i ++){
//                routes.add(busLines.get(i).getRoute());
//            }
//
//            System.out.println("Number of stops: " + stops.size());
//            for (Stop stop : stops) {
//                System.out.println("Processing stop ID: " + stop);
//            }

            System.out.println("Number of bus lines: " + busLines.size());
            for (BusLine busLine : busLines) {
//                System.out.println(busLine);
//                System.out.println(busLine.getShortName());
            }

//            System.out.println("-----------------------");
//            System.out.println("-----------------------");
//            System.out.println("-----------------------");
//            System.out.println("-----------------------");
//            System.out.println(driver.getBusLineByLongName("Night Pilot"));

            driver.commit();

        } catch (Exception e) {
//            e.printStackTrace();
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
    }
}