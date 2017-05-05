package com.lloydramey.jdbc

data class ParseResult(val sql: String, val parameters: Map<String, List<Int>>)