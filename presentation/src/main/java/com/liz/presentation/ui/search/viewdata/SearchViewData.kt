package com.liz.presentation.ui.search.viewdata

import androidx.paging.PagingData
import com.liz.presentation.ui.search.adapter.SearchRecyclerData

data class SearchViewData(
    val list: PagingData<SearchRecyclerData>? = null,
    val page: Int = 0,
    val query: String = ""
)