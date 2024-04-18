package edu.virginia.sde.hw5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.*;
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


class BusLineReaderTest {

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

//    @Test
//    void getBusLines() {
//        List<BusLine> buslineList = busLineReader.getBusLines();
//        int actualSize = buslineList.size();
//        assertEquals(actualSize,16);
//    }
}