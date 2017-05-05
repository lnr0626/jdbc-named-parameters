package com.lloydramey.jdbc

import java.io.InputStream
import java.math.BigDecimal
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Time
import java.sql.Timestamp

class NamedParameterPreparedStatement private constructor(val delegate: PreparedStatement, val parseResult: ParseResult) : PreparedStatement by delegate {
    
    companion object {
        @Throws(SQLException::class)
        @JvmStatic
        fun createNamedPreparedStatement(conn: Connection, sql: String): NamedParameterPreparedStatement {
            val res = parse(sql)
            return NamedParameterPreparedStatement(conn.prepareStatement(res.sql), res)
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun createNamedParameterPreparedStatement(conn: Connection, sql: String): NamedParameterPreparedStatement {
            val parseResult = parse(sql)
            return NamedParameterPreparedStatement(conn.prepareStatement(parseResult.sql), parseResult)
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun createNamedParameterPreparedStatement(conn: Connection, sql: String,
            autoGeneratedKeys: Int): NamedParameterPreparedStatement {
            val parseResult = parse(sql)
            return NamedParameterPreparedStatement(conn.prepareStatement(parseResult.sql, autoGeneratedKeys),
                parseResult)
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun createNamedParameterPreparedStatement(conn: Connection, sql: String,
            columnIndexes: IntArray): NamedParameterPreparedStatement {
            val parseResult = parse(sql)
            return NamedParameterPreparedStatement(conn.prepareStatement(parseResult.sql, columnIndexes),
                parseResult)
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun createNamedParameterPreparedStatement(conn: Connection, sql: String,
            columnNames: Array<String>): NamedParameterPreparedStatement {
            val parseResult = parse(sql)
            return NamedParameterPreparedStatement(conn.prepareStatement(parseResult.sql, columnNames),
                parseResult)
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun createNamedParameterPreparedStatement(conn: Connection, sql: String,
            resultSetType: Int, resultSetConcurrency: Int): NamedParameterPreparedStatement {
            val parseResult = parse(sql)
            return NamedParameterPreparedStatement(conn.prepareStatement(parseResult.sql, resultSetType, resultSetConcurrency),
                parseResult)
        }

        @Throws(SQLException::class)
        @JvmStatic
        fun createNamedParameterPreparedStatement(conn: Connection, sql: String,
            resultSetType: Int, resultSetConcurrency: Int, resultSetHoldability: Int): NamedParameterPreparedStatement {
            val parseResult = parse(sql)
            return NamedParameterPreparedStatement(conn.prepareStatement(parseResult.sql, resultSetType, resultSetConcurrency, resultSetHoldability),
                parseResult)
        }
    }

    private fun getParameterIndexes(parameter: String): Collection<Int> =
        parseResult.parameters[parameter] ?: throw IllegalArgumentException("SQL statement doesn't contain the parameter '$parameter")

    @Throws(SQLException::class)
    fun setNull(parameter: String, sqlType: Int) {
        getParameterIndexes(parameter).forEach { i -> setNull(i, sqlType) }
    }

    @Throws(SQLException::class)
    fun setBoolean(parameter: String, x: Boolean) {
        getParameterIndexes(parameter).forEach { i -> setBoolean(i, x) }
    }

    @Throws(SQLException::class)
    fun setByte(parameter: String, x: Byte) {
        getParameterIndexes(parameter).forEach { i -> setByte(i, x) }
    }

    @Throws(SQLException::class)
    fun setShort(parameter: String, x: Short) {
        getParameterIndexes(parameter).forEach { i -> setShort(i, x) }
    }

    @Throws(SQLException::class)
    fun setInt(parameter: String, x: Int) {
        getParameterIndexes(parameter).forEach { i -> setInt(i, x) }
    }

    @Throws(SQLException::class)
    fun setLong(parameter: String, x: Long) {
        getParameterIndexes(parameter).forEach { i -> setLong(i, x) }
    }

    @Throws(SQLException::class)
    fun setFloat(parameter: String, x: Float) {
        getParameterIndexes(parameter).forEach { i -> setFloat(i, x) }
    }

    @Throws(SQLException::class)
    fun setDouble(parameter: String, x: Float) {
        getParameterIndexes(parameter).forEach { i -> setDouble(i, x.toDouble()) }
    }

    @Throws(SQLException::class)
    fun setBigDecimal(parameter: String, x: BigDecimal) {
        getParameterIndexes(parameter).forEach { i -> setBigDecimal(i, x) }
    }

    @Throws(SQLException::class)
    fun setString(parameter: String, x: String) {
        getParameterIndexes(parameter).forEach { i -> setString(i, x) }
    }

    @Throws(SQLException::class)
    fun setBytes(parameter: String, x: ByteArray) {
        getParameterIndexes(parameter).forEach { i -> setBytes(i, x) }
    }

    @Throws(SQLException::class)
    fun setDate(parameter: String, x: Date) {
        getParameterIndexes(parameter).forEach { i -> setDate(i, x) }
    }

    @Throws(SQLException::class)
    fun setTime(parameter: String, x: Time) {
        getParameterIndexes(parameter).forEach { i -> setTime(i, x) }
    }

    @Throws(SQLException::class)
    fun setTimestamp(parameter: String, x: Timestamp) {
        getParameterIndexes(parameter).forEach { i -> setTimestamp(i, x) }
    }

    @Throws(SQLException::class)
    fun setAsciiStream(parameter: String, x: InputStream) {
        getParameterIndexes(parameter).forEach { i -> setAsciiStream(i, x) }
    }

    @Suppress("DEPRECATION")
    @Deprecated("")
    @Throws(SQLException::class)
    fun setUnicodeStream(parameter: String, x: InputStream, length: Int) {
        getParameterIndexes(parameter).forEach { i -> setUnicodeStream(i, x, length) }
    }

    @Throws(SQLException::class)
    fun setBinaryStream(parameter: String, x: InputStream, length: Int) {
        getParameterIndexes(parameter).forEach { i -> setBinaryStream(i, x, length) }
    }

    @Throws(SQLException::class)
    fun setObject(parameter: String, x: Any, targetSqlType: Int, scale: Int) {
        getParameterIndexes(parameter).forEach { i -> setObject(i, x, targetSqlType, scale) }
    }

    @Throws(SQLException::class)
    fun setObject(parameter: String, x: Any, targetSqlType: Int) {
        getParameterIndexes(parameter).forEach { i -> setObject(i, x, targetSqlType) }
    }

    @Throws(SQLException::class)
    fun setObject(parameter: String, x: Any) {
        getParameterIndexes(parameter).forEach { i -> setObject(i, x) }
    }

}
