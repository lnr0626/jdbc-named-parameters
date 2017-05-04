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
        for (i in getParameterIndexes(parameter)) {
            setNull(i, sqlType)
        }
    }

    @Throws(SQLException::class)
    fun setBoolean(parameter: String, x: Boolean) {
        for (i in getParameterIndexes(parameter)) {
            setBoolean(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setByte(parameter: String, x: Byte) {
        for (i in getParameterIndexes(parameter)) {
            setByte(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setShort(parameter: String, x: Short) {
        for (i in getParameterIndexes(parameter)) {
            setShort(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setInt(parameter: String, x: Int) {
        for (i in getParameterIndexes(parameter)) {
            setInt(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setLong(parameter: String, x: Long) {
        for (i in getParameterIndexes(parameter)) {
            setLong(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setFloat(parameter: String, x: Float) {
        for (i in getParameterIndexes(parameter)) {
            setFloat(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setDouble(parameter: String, x: Float) {
        for (i in getParameterIndexes(parameter)) {
            setDouble(i, x.toDouble())
        }
    }

    @Throws(SQLException::class)
    fun setBigDecimal(parameter: String, x: BigDecimal) {
        for (i in getParameterIndexes(parameter)) {
            setBigDecimal(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setString(parameter: String, x: String) {
        for (i in getParameterIndexes(parameter)) {
            setString(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setBytes(parameter: String, x: ByteArray) {
        for (i in getParameterIndexes(parameter)) {
            setBytes(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setDate(parameter: String, x: Date) {
        for (i in getParameterIndexes(parameter)) {
            setDate(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setTime(parameter: String, x: Time) {
        for (i in getParameterIndexes(parameter)) {
            setTime(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setTimestamp(parameter: String, x: Timestamp) {
        for (i in getParameterIndexes(parameter)) {
            setTimestamp(i, x)
        }
    }

    @Throws(SQLException::class)
    fun setAsciiStream(parameter: String, x: InputStream) {
        for (i in getParameterIndexes(parameter)) {
            setAsciiStream(i, x)
        }
    }

    @Deprecated("")
    @Throws(SQLException::class)
    fun setUnicodeStream(parameter: String, x: InputStream, length: Int) {
        for (i in getParameterIndexes(parameter)) {
            setUnicodeStream(i, x, length)
        }
    }

    @Throws(SQLException::class)
    fun setBinaryStream(parameter: String, x: InputStream, length: Int) {
        for (i in getParameterIndexes(parameter)) {
            setBinaryStream(i, x, length)
        }
    }

    @Throws(SQLException::class)
    fun setObject(parameter: String, x: Any, targetSqlType: Int, scale: Int) {
        for (i in getParameterIndexes(parameter)) {
            setObject(i, x, targetSqlType, scale)
        }
    }

    @Throws(SQLException::class)
    fun setObject(parameter: String, x: Any, targetSqlType: Int) {
        for (i in getParameterIndexes(parameter)) {
            setObject(i, x, targetSqlType)
        }
    }

    @Throws(SQLException::class)
    fun setObject(parameter: String, x: Any) {
        for (i in getParameterIndexes(parameter)) {
            setObject(i, x)
        }
    }

}

fun createNamedPreparedStatement(conn: Connection, sql: String): NamedParameterPreparedStatement {
    val res = parse(sql)
    return NamedParameterPreparedStatement(conn.prepareStatement(res.sql), res)
}

data class ParseResult(val sql: String, val parameters: Map<String, List<Int>>)

/**
 * Parse the query string containing named parameters and result a parse result, which holds
 * the parsed sql (named parameters replaced by standard '?' parameters and an ordered list of the
 * named parameters.
 *
 * SQL parsing code borrowed from Adam Crume. Thanks Adam.
 * See <a href="http://www.javaworld.com/article/2077706/core-java/named-parameters-for-preparedstatement.html?page=2">http://www.javaworld.com/article/2077706/core-java/named-parameters-for-preparedstatement.html?page=2</a>
 *
 * @param query Query containing named parameters
 * @return ParseResult
 */
internal fun parse(query: String): ParseResult {
    val length = query.length
    val parsedQuery = StringBuffer(length)
    var inSingleQuote = false
    var inDoubleQuote = false
    var inSingleLineComment = false
    var inMultiLineComment = false
    var index = 1
    val params = mutableMapOf<String, List<Int>>()

    var i = 0
    while (i < length) {
        var c = query[i]
        if (inSingleQuote) {
            if (c == '\'') {
                inSingleQuote = false
            }
        } else if (inDoubleQuote) {
            if (c == '"') {
                inDoubleQuote = false
            }
        } else if (inMultiLineComment) {
            if (c == '*' && query[i + 1] == '/') {
                inMultiLineComment = false
            }
        } else if (inSingleLineComment) {
            if (c == '\n') {
                inSingleLineComment = false
            }
        } else {
            if (c == '\'') {
                inSingleQuote = true
            } else if (c == '"') {
                inDoubleQuote = true
            } else if (c == '/' && query[i + 1] == '*') {
                inMultiLineComment = true
            } else if (c == '-' && query[i + 1] == '-') {
                inSingleLineComment = true
            } else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query[i + 1])) {
                var j = i + 2
                while (j < length && Character.isJavaIdentifierPart(query[j])) {
                    j++
                }
                val name = query.substring(i + 1, j)
                params[name] = (params[name] ?: listOf()) + index
                c = '?' // replace the parameter with a question mark
                i += name.length // skip past the end if the parameter
            }
        }
        parsedQuery.append(c)
        i++
    }
    return ParseResult(parsedQuery.toString(), params)
}