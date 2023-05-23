package com.liz.presentation.ui.search.viewdata

data class SearchUi(
    val state: SearchState = SearchState.INITIAL,
    val viewData: SearchViewData = SearchViewData()
)
