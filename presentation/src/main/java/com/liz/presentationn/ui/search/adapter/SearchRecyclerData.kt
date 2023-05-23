package com.liz.presentationn.ui.search.adapter

import com.liz.presentationn.ui.search.adapter.holder.TitleViewData

sealed class SearchRecyclerData {
    data class TitleItemData(val viewData: TitleViewData) : SearchRecyclerData()
}