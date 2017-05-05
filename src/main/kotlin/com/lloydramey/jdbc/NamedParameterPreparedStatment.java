package com.lloydramey.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class NamedParameterPreparedStatment {
    public static InternalNamedParameterPreparedStatement createNamedPreparedStatement(Connection conn, String sql) throws SQLException {
        final ParseResult res = InternalNamedParameterPreparedStatementKt.parse(sql);
        return new InternalNamedParameterPreparedStatement(conn.prepareStatement(res.getSql()), res);
    }
}
