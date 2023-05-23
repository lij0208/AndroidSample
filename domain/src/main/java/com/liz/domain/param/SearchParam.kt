package com.liz.domain.param

data class SearchParam(
    val query: String,
    val display: Int,
    val start: Int,
    val sort: String
)
