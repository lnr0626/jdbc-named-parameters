package com.lloydramey.jdbc;

import com.axiomalaska.jdbc.FileUtil;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.lloydramey.jdbc.NamedParameterPreparedStatementKt.parse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestNamedParameterPreparedStatement {
    @Test
    public void testParsing(){
        String testQuery = "SELECT * FROM table WHERE afield = ':not me' AND bfield = :param1 AND cfield = :param2 and dfield = :param2;";
        String expectedParsedQuery = "SELECT * FROM table WHERE afield = ':not me' AND bfield = ? AND cfield = ? and dfield = ?;";
        Map<String, List<Integer>> expectedParameterList = new HashMap<>();
        expectedParameterList.put("param1", Collections.singletonList(1));
        expectedParameterList.put("param2", Arrays.asList(2, 3));
        ParseResult parseResult = parse(testQuery);
        assertEquals(expectedParsedQuery, parseResult.getSql());
        assertThat(expectedParameterList, is(parseResult.getParameters()));
    }

    @Test
    public void testParsing2() throws IOException{
        String testQuery = FileUtil.readFile(new File("src/test/resources/test.sql"));
        String expectedParsedQuery = FileUtil.readFile(new File("src/test/resources/expected.sql"));
        Map<String, List<Integer>> expectedParameterList = new HashMap<>();
        expectedParameterList.put("named_parameter1", Collections.singletonList(1));
        expectedParameterList.put("named_parameter2", Collections.singletonList(2));
        ParseResult parseResult = parse(testQuery);
        assertEquals(expectedParsedQuery, parseResult.getSql());
        assertThat(expectedParameterList, is(parseResult.getParameters()));
    }
}
