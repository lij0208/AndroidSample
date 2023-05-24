package com.liz.presentation.ui.search

import android.text.Html
import androidx.core.text.HtmlCompat
import androidx.paging.PagingData
import androidx.paging.map
import com.liz.domain.entity.SearchItemEntity
import com.liz.presentation.ui.search.adapter.SearchRecyclerData
import com.liz.presentation.ui.search.adapter.holder.TitleViewData
import com.liz.presentation.ui.search.viewdata.SearchState
import com.liz.presentation.ui.search.viewdata.SearchUi
import javax.inject.Inject

class SearchConverter @Inject constructor() {

    fun convert(ui: SearchUi, entity: PagingData<SearchItemEntity>): SearchUi {
        val list = entity.map {
            createTitleItem(it)
        }
        return ui.copy(
            state = SearchState.SUCCESS,
            viewData = ui.viewData.copy(
                list = list,
                page = ui.viewData.page + 1
            )
        )
    }

    private fun createTitleItem(entity: SearchItemEntity) : SearchRecyclerData {
        return SearchRecyclerData.TitleItemData(
            viewData = TitleViewData(
                title = Html.fromHtml(entity.title, HtmlCompat.FROM_HTML_MODE_LEGACY),
                blogLink = entity.bloggerlink
            )
        )
    }
}