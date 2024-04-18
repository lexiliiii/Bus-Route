package edu.virginia.sde.hw5;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.*;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;

class DatabaseDriverTest {

    @Mock
    private URL stops;

    @Mock
    private URL lines;

    @Mock
    private Configuration configuration;

    private BusLineReader busLineReader;

    @BeforeEach
    void setUp() throws MalformedURLException {
        stops = new URL("https://www.cs.virginia.edu/~pm8fc/busses/stops.json");
        lines = new URL("https://www.cs.virginia.edu/~pm8fc/busses/lines.json");
        when(configuration.getBusLinesURL()).thenReturn(lines);
        when(configuration.getBusStopsURL()).thenReturn(stops);
        busLineReader = new BusLineReader(configuration);
    }

    @Test
    void createTables() {

    }

    @Test
    void addStops() {
    }

    @Test
    void getAllStops() {
    }

    @Test
    void getStopById() {
    }

    @Test
    void getStopsByName() {
    }

    @Test
    void addBusLines() {
    }

    @Test
    void getBusLines() {
    }

    @Test
    void getBusLinesById() {
    }

    @Test
    void getBusLineByLongName() {
    }

    @Test
    void getBusLineByShortName() {
    }

    @Test
    void getBusLinesByStop() {
    }

    @Test
    void getRouteForBusLine() {
    }

    @Test
    void clearTables() {
    }
}