package com.lloydramey.jdbc

import java.io.InputStream
import java.math.BigDecimal
import java.sql.Connection
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Time
import java.sql.Timestamp

class NamedParameterPreparedStatement internal constructor(val delegate: PreparedStatement, val parseResult: ParseResult) : PreparedStatement by delegate {

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

fun createNamedPreparedStatement(conn: Connection, sql: String): NamedParameterPreparedStatement {
    val res = parse(sql)
    return NamedParameterPreparedStatement(conn.prepareStatement(res.sql), res)
}

data class ParseResult(val sql: String, val parameters: Map<String, List<Int>>)

enum class ParseState {SINGLE_QUOTE, DOUBLE_QUOTE, SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, PLAIN; }

tailrec internal fun parse(query: String, pos: Int, state: ParseState, convertedSql: String, params: List<String>): ParseResult {
    if (pos >= query.length) {
        val map = params.mapIndexed { index, param -> param to index + 1 }.groupBy({ it.first }, { it.second })
        return ParseResult(convertedSql, map)
    }

    val current = query[pos]
    val next = if (query.length > pos + 1) query[pos + 1] else null

    when (state) {
        ParseState.SINGLE_QUOTE ->
            if (current == '\'') return parse(query, pos + 1, ParseState.PLAIN, convertedSql + current, params)
            else return parse(query, pos + 1, ParseState.SINGLE_QUOTE, convertedSql + current, params)
        ParseState.DOUBLE_QUOTE ->
            if (current == '"') return parse(query, pos + 1, ParseState.PLAIN, convertedSql + current, params)
            else return parse(query, pos + 1, ParseState.DOUBLE_QUOTE, convertedSql + current, params)
        ParseState.SINGLE_LINE_COMMENT ->
            if (current == '\n') return parse(query, pos + 1, ParseState.PLAIN, convertedSql + current, params)
            else return parse(query, pos + 1, ParseState.SINGLE_LINE_COMMENT, convertedSql + current, params)
        ParseState.MULTI_LINE_COMMENT ->
            if (current == '*' && next == '/') return parse(query, pos + 2, ParseState.PLAIN, convertedSql + current + next, params)
            else return parse(query, pos + 1, ParseState.MULTI_LINE_COMMENT, convertedSql + current, params)
        ParseState.PLAIN ->
            if (current == '\'') return parse(query, pos + 1, ParseState.SINGLE_QUOTE, convertedSql + current, params)
            else if (current == '"') return parse(query, pos + 1, ParseState.DOUBLE_QUOTE, convertedSql + current, params)
            else if (current == '/' && next == '*') return parse(query, pos + 2, ParseState.MULTI_LINE_COMMENT, convertedSql + current + next, params)
            else if (current == '-' && next == '-') return parse(query, pos + 2, ParseState.SINGLE_LINE_COMMENT, convertedSql + current + next, params)
            else if (current == ':' && next != null && Character.isJavaIdentifierStart(next)) {
                val nameLength = identifierLength(query, pos + 1)
                val name = query.substring(pos + 1, pos + 1 + nameLength)
                return parse(query, pos + 1 + nameLength, ParseState.PLAIN, convertedSql + '?', params + name)
            } else return parse(query, pos + 1, ParseState.PLAIN, convertedSql + current, params)
    }
}

tailrec internal fun identifierLength(str: String, pos: Int, size: Int = 0): Int {
    if (size == 0) {
        if (Character.isJavaIdentifierStart(str[pos])) return identifierLength(str, pos + 1, size + 1)
        else return 0
    } else {
        if (Character.isJavaIdentifierPart(str[pos])) return identifierLength(str, pos + 1, size + 1)
        else return size
    }
}

/**
 * Parse the query string containing named parameters and result a parse result, which holds
 * the parsed sql (named parameters replaced by standard '?' parameters and an ordered list of the
 * named parameters.
 *
 * Original SQL parsing code borrowed from Adam Crume. Thanks Adam.
 * See <a href="http://www.javaworld.com/article/2077706/core-java/named-parameters-for-preparedstatement.html?page=2">http://www.javaworld.com/article/2077706/core-java/named-parameters-for-preparedstatement.html?page=2</a>
 *
 * @param query Query containing named parameters
 * @return ParseResult
 */
internal fun parse(query: String): ParseResult {
    return parse(query, 0, ParseState.PLAIN, "", listOf())
}