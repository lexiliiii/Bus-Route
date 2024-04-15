package edu.virginia.sde.hw5;

import javax.print.DocFlavor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.*;

public class DatabaseDriver {
    private final String sqliteFilename;
    private Connection connection;
//TODO: DO NOT CALL COMMIT IN ANY METHODS
    public DatabaseDriver(Configuration configuration) {
        this.sqliteFilename = configuration.getDatabaseFilename();
    }

    public DatabaseDriver (String sqlListDatabaseFilename) {
        this.sqliteFilename = sqlListDatabaseFilename;
    }

    /**
     * Connect to a SQLite Database. This turns out Foreign Key enforcement, and disables auto-commits
     * @throws SQLException
     */
    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            throw new IllegalStateException("The connection is already opened");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
        //the next line enables foreign key enforcement - do not delete/comment out
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        //the next line disables auto-commit - do not delete/comment out
        connection.setAutoCommit(false);
    }

    /**
     * Commit all changes since the connection was opened OR since the last commit/rollback
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * Rollback to the last commit, or when the connection was opened
     */
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * Ends the connection to the database
     */
    public void disconnect() throws SQLException {
        connection.close();
    }

    /**
     * Creates the three database tables Stops, BusLines, and Routes, with the appropriate constraints including
     * foreign keys, if they do not exist already. If they already exist, this method does nothing.
     * As a hint, you'll need to create Routes last, and Routes must include Foreign Keys to Stops and
     * BusLines.
     * @throws SQLException
     */
    public void createTables() throws SQLException {
        //TODO: implement
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        String Stops =
                "CREATE TABLE Stops (" +
                        "ID INTEGER NOT NULL PRIMARY KEY, " +
                        "StopName TEXT NOT NULL, " +
                        "Latitude REAL NOT NULL, " +
                        "Longitude REAL NOT NULL);";

        String BusLines =
                "CREATE TABLE BusLines (" +
                        "ID INTEGER NOT NULL PRIMARY KEY, " +
                        "IsActive BOOLEAN NOT NULL, " +
                        "LongName TEXT NOT NULL, " +
                        "ShortName TEXT NOT NULL);";

        String Routes =
                "CREATE TABLE Routes (" +
                        "ID INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT, " +
                        "BusLineID INTEGER NOT NULL, " +
                        "StopID INTEGER NOT NULL, " +
                        "RouteOrder INTEGER NOT NULL, " +
                        "FOREIGN KEY (BusLineID) REFERENCES BusLines(ID) ON DELETE CASCADE, " +
                        "FOREIGN KEY (StopID) REFERENCES Stops(ID) ON DELETE CASCADE);";

//        Statement statement = connection.createStatement();
        try (Statement statement = connection.createStatement()) {
            statement.execute(Stops);
            statement.execute(BusLines);
            statement.execute(Routes);
        } catch (SQLException e) {
            throw new SQLException("Error creating tables: " + e.getMessage());
        }
    }


    /**
     * Add a list of Stops to the Database. After adding all the stops, the changes will be committed. However,
     * if any SQLExceptions occur, this method will rollback and throw the exception.
     * @param stops - the stops to be added to the database
     */
    public void addStops(List<Stop> stops) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        String insertStops = "INSERT INTO Stops (ID, StopName, Latitude, Longitude VALUES (?, ?, ?, ?);";

        try (PreparedStatement pstmt = connection.prepareStatement(insertStops)) {
            for (Stop stop : stops) {
                pstmt.setInt(1, stop.getId());
                pstmt.setString(2, stop.getName());
                pstmt.setDouble(3, stop.getLatitude());
                pstmt.setDouble(4, stop.getLongitude());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error adding stops: " + e.getMessage());
        }

        //TODO: implement
//        try {
//            //Your JDBC code codes here
//        } catch (SQLException e) {
//            rollback(); //rolls back any changes before the Exception was thrown
//            throw e; //still throws the SQLException
//        }

    }

    /**
     * Gets a list of all Stops in the database
     */
    public List<Stop> getAllStops() throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }
        //TODO: implement

        List<Stop> collection = new ArrayList<>();
        String sql = "SELECT * FROM Stops;";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                String stopName = rs.getString("StopName");
                double latitude = rs.getDouble("Latitude");
                double longitude = rs.getDouble("Longitude");

                collection.add(new Stop(id, stopName, latitude, longitude));
            }

        } catch (SQLException e) {
            throw new SQLException("Error reading from Stops table: " + e.getMessage());
        }
        return collection;
    }

    /**
     * Get a Stop by its ID number. Returns Optional.isEmpty() if no Stop matches the ID.
     */
    public Optional<Stop> getStopById(int stopId) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        String sql = "SELECT * FROM Stops WHERE ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, stopId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs != null) {
                    String stopName = rs.getString("StopName");
                    double latitude = rs.getDouble("Latitude");
                    double longitude = rs.getDouble("Longitude");

                    Stop stop = new Stop(stopId, stopName, latitude, longitude);
                    return Optional.of(stop);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading from Stops table: " + e.getMessage());
        }

        //TODO: implement
        //use select Query with a Where clause
//        return Optional.empty();
    }












    /**
     * Get all Stops whose name contains the substring (case-insensitive). For example, the parameter "Rice"
     * would return a List of Stops containing "Whitehead Rd @ Rice Hall"
     */
    public List<Stop> getStopsByName(String subString) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        List<Stop> collection = new ArrayList<>();
        String sql = "SELECT * FROM Stops;";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                String stopName = rs.getString("StopName");
                if(stopName.toLowerCase().contains(subString.toLowerCase())){
                    int id = rs.getInt("ID");
                    double latitude = rs.getDouble("Latitude");
                    double longitude = rs.getDouble("Longitude");
                    collection.add(new Stop(id, stopName, latitude, longitude));
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error reading from Stops table: " + e.getMessage());
        }

        //TODO: implement

//SELECT * FROM COURSES WHERE Subject = ‘CS’;

        return collection;
    }

    /**
     * Add BusLines and their Routes to the database, including their routes. This method should only be called after
     * Stops are added to the database via addStops, since Routes depends on the StopIds already being
     * in the database. If any SQLExceptions occur, this method will rollback all changes since
     * the method was called. This could happen if, for example, a BusLine contains a Stop that is not in the database.
     */
    public void addBusLines(List<BusLine> busLines) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }
        //TODO: implement

        String insertBusLine = "INSERT INTO BusLines (ID, IsActive, LongName, ShortName) VALUES (?, ?, ?, ?);";
        String insertRoute = "INSERT INTO Routes (BusLineID, StopID, RouteOrder) VALUES (?, ?, ?);";

        try (PreparedStatement pstmtBusLine = connection.prepareStatement(insertBusLine);
             PreparedStatement pstmtRoute = connection.prepareStatement(insertRoute)) {

            for (BusLine busLine : busLines) {
                pstmtBusLine.setInt(1, busLine.getId());
                pstmtBusLine.setBoolean(2, busLine.isActive());
                pstmtBusLine.setString(3, busLine.getLongName());
                pstmtBusLine.setString(4, busLine.getShortName());
                pstmtBusLine.executeUpdate();

                Route routes = busLine.getRoute();
                for (Stop route : routes) {
                    for (int i = 0; i < routes.size(); i++) {
//                        pstmtRoute.setInt(1, routeId);
                        pstmtRoute.setInt(2, busLine.getId());
                        pstmtRoute.setInt(3, route.getId());
                        pstmtRoute.setInt(4, i);
                        pstmtRoute.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        }
//        try {
            //Your JDBC code codes here
//        } catch (SQLException e) {
//            rollback(); //rolls back any changes before the Exception was thrown
//            throw e; //still throws the SQLException
//        }

    }

    /**
     * Return a list of all BusLines
     */
    public List<BusLine> getBusLines() throws SQLException{
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        List<BusLine> collection = new ArrayList<>();
        String sql = "SELECT * FROM Buslines;";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("ID");
                boolean isActive = rs.getBoolean("IsActive");
                String longName = rs.getString("LongName");
                String shortName = rs.getString("ShortName");

                collection.add(new BusLine(id, isActive, longName, shortName));
            }

        } catch (SQLException e) {
            throw new SQLException("Error reading from Stops table: " + e.getMessage());
        }
        return collection;

        //TODO: implement
    }

    /**
     * Get a BusLine by its id number. Return Optional.empty() if no busLine is found
     */
    public Optional<BusLine> getBusLinesById(int busLineId) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        String sql = "SELECT * FROM BusLine WHERE ID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, busLineId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs != null) {
                    boolean isActive = rs.getBoolean("IsActive");
                    String longName = rs.getString("LongName");
                    String shortName = rs.getString("ShortName");

                    BusLine busLine = new BusLine(busLineId, isActive, longName, shortName);
                    return Optional.of(busLine);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading from Stops table: " + e.getMessage());
        }
        //TODO: implement
//        return Optional.empty();
    }

    /**
     * Get BusLine by its full long name (case-insensitive). Return Optional.empty() if no busLine is found.
     */
    public Optional<BusLine> getBusLineByLongName(String longName) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        String sql = "SELECT * FROM BusLine WHERE LongName = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, longName);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs != null) {
                    int id = rs.getInt("ID");
                    boolean isActive = rs.getBoolean("IsActive");
                    String shortName = rs.getString("ShortName");

                    BusLine busLine = new BusLine(id, isActive, longName, shortName);
                    return Optional.of(busLine);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading from Stops table: " + e.getMessage());
        }
        //TODO: implement
//        return Optional.empty();
    }

    /**
     * Get BusLine by its full short name (case-insensitive). Return Optional.empty() if no busLine is found.
     */
    public Optional<BusLine> getBusLineByShortName(String shortName) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        String sql = "SELECT * FROM BusLine WHERE ShortName = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, shortName);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs != null) {
                    int id = rs.getInt("ID");
                    boolean isActive = rs.getBoolean("IsActive");
                    String longName = rs.getString("LongName");

                    BusLine busLine = new BusLine(id, isActive, longName, shortName);
                    return Optional.of(busLine);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading from Stops table: " + e.getMessage());
        }

        //TODO: implement
//        return Optional.empty();
    }

    /**
     * Get all BusLines that visit a particular stop
     */
    public List<BusLine> getBusLinesByStop(Stop stop) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        List<BusLine> collection = new ArrayList<>();

        String sql = "SELECT DISTINCT bl.ID, bl.IsActive, bl.LongName, bl.ShortName " +
                "FROM BusLines bl " +
                "JOIN Routes r ON bl.ID = r.BusLineID " +
                "WHERE r.StopID = ? " +
                "ORDER BY bl.ID;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, stop.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    boolean isActive = rs.getBoolean("IsActive");
                    String longName = rs.getString("LongName");
                    String shortName = rs.getString("ShortName");
                    collection.add(new BusLine(id, isActive, longName, shortName));
                }
            }
        }

        catch (SQLException e) {
            throw new SQLException("Error get bus lines by stop: " + e.getMessage());
        }
        return collection;
        //TODO: implement
//        return null;
    }

    /**
     * Returns a BusLine's route as a List of stops *in-order*
     * @param busLine
     * @throws SQLException
     * @throws java.util.NoSuchElementException if busLine is not in the database
     */
    public Route getRouteForBusLine(BusLine busLine) throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        List<Stop> stops = new ArrayList<>();
        String sql = "SELECT s.ID, s.StopName, s.Latitude, s.Longitude " +
                "FROM Routes r " +
                "JOIN Stops s ON r.StopID = s.ID " +
                "WHERE r.BusLineID = ? " +
                "ORDER BY r.RouteOrder;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, busLine.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("ID");
                    String name = rs.getString("StopName");
                    double latitude = rs.getDouble("Latitude");
                    double longitude = rs.getDouble("Longitude");
                    stops.add(new Stop(id, name, latitude, longitude));
                }
            }
            return new Route(stops);
        }
        catch (SQLException e) {
            throw new SQLException("Error get route for BusLine: " + e.getMessage());
        }
        //TODO: implement
//        return null;
    }

    /**
     * Removes all data from the tables, leaving the tables empty (but still existing!). As a hint, delete the
     * contents of Routes first in order to avoid violating foreign key constraints.
     */
    public void clearTables() throws SQLException {
        if (connection.isClosed()) {
            throw new IllegalStateException("Connection is closed right now.");
        }

        String deleteRoutes = "DELETE FROM Routes;";
        String deleteStops = "DELETE FROM Stops;";
        String deleteBusLines = "DELETE FROM BusLines;";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(deleteRoutes);
            stmt.execute(deleteStops);
            stmt.execute(deleteBusLines);

        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException("Error clearing tables: " + e.getMessage());
        }

        //        try {
//            //Your JDBC code codes here
//        } catch (SQLException e) {
//            rollback(); //rolls back any changes before the Exception was thrown
//            throw e; //still throws the SQLException
//        }
        //TODO: implement
    }
}
