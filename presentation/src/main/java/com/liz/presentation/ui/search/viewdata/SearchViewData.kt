package com.liz.presentation.ui.search.viewdata

import com.liz.presentation.ui.search.adapter.SearchRecyclerData

data class SearchViewData(
    val list: List<SearchRecyclerData> = emptyList(),
    val page: Int = 0
)