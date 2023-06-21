package com.liz.presentation.ui.search.viewdata

import com.liz.presentation.ui.search.adapter.SearchRecyclerData

const val START_POSITION = 1
const val END_POSITION = -1

data class SearchViewData(
    val list: List<SearchRecyclerData>? = null,
    val page: Int = START_POSITION,
    val query: String = ""
)