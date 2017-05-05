package com.lloydramey.jdbc

private enum class ParseState {SINGLE_QUOTE, DOUBLE_QUOTE, SINGLE_LINE_COMMENT, MULTI_LINE_COMMENT, PLAIN; }

tailrec private fun parse(query: String, pos: Int, state: ParseState, convertedSql: String, params: List<String>): ParseResult {
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

tailrec private fun identifierLength(str: String, pos: Int, size: Int = 0): Int {
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