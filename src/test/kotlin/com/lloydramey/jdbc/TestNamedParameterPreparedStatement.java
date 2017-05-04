package com.lloydramey.jdbc;

import kotlin.text.Charsets;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static com.lloydramey.jdbc.NamedParameterPreparedStatementKt.parse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestNamedParameterPreparedStatement {
    @Test
    public void testParsing() {
        String testQuery = "SELECT * FROM table WHERE afield = ':not me' AND bfield = :param1 AND cfield = :param2 and dfield = :param2;";
        String expectedParsedQuery = "SELECT * FROM table WHERE afield = ':not me' AND bfield = ? AND cfield = ? and dfield = ?;";
        Map<String, List<Integer>> expectedParameterList = new HashMap<>();
        expectedParameterList.put("param1", Collections.singletonList(1));
        expectedParameterList.put("param2", Arrays.asList(2, 3));
        ParseResult parseResult = parse(testQuery);
        assertEquals(expectedParsedQuery, parseResult.getSql());
        assertThat(parseResult.getParameters(), is(expectedParameterList));
    }

    @Test
    public void testParsing2() throws IOException {
        String testQuery = getResourceAsString("test.sql");
        String expectedParsedQuery = getResourceAsString("expected.sql");
        Map<String, List<Integer>> expectedParameterList = new HashMap<>();
        expectedParameterList.put("named_parameter1", Collections.singletonList(1));
        expectedParameterList.put("named_parameter2", Collections.singletonList(2));
        ParseResult parseResult = parse(testQuery);
        assertEquals(expectedParsedQuery, parseResult.getSql());
        assertThat(parseResult.getParameters(), is(expectedParameterList));
    }

    private String getResourceAsString(String name) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        return IOUtils.toString(classLoader.getResourceAsStream(name), Charsets.UTF_8);
    }
}
