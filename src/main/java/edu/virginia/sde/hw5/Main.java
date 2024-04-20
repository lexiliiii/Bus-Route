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

            driver.addStops(stops);
            driver.addBusLines(busLines);

//            BusLine temp = busLines.get(0);
//            System.out.println(temp);
//            System.out.println();
//            System.out.println(driver.getBusLineByShortName("29n"));
//            System.out.println();
//            System.out.println(driver.getBusLineByShortName("29"));

//            System.out.println(driver.getBusLinesById(4013468));
//            System.out.println();
//            System.out.println(driver.getBusLinesById(403468));

//            System.out.println(driver.getBusLineByLongName("29 North connect"));
//            System.out.println();
//            System.out.println(driver.getBusLineByLongName("29 Norteh CONNECT"));

//            BusLine temp = busLines.get(0);
//            System.out.println(temp);
//            Route temp1 = driver.getRouteForBusLine(temp);
//            System.out.println(temp1);

//            System.out.println("Number of stops: " + stops.size());
//            for (Stop stop : stops) {
//                System.out.println(stop);
//            }
//            System.out.println(driver.getStopById(4267060));
//            System.out.println(driver.getRouteForBusLine());
//
//            System.out.println("Number of bus lines: " + busLines.size());
//            for (BusLine busLine : busLines) {
//                System.out.println(busLine);
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
    }
}