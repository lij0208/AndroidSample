package com.liz.presentation.ui.search.adapter

import com.liz.presentation.ui.search.adapter.holder.TitleViewData

sealed class SearchRecyclerData {
    data class TitleItemData(val viewData: TitleViewData) : SearchRecyclerData()
}