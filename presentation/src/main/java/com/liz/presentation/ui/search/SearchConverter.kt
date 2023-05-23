package com.liz.presentation.ui.search

import android.text.Html
import androidx.core.text.HtmlCompat
import com.liz.domain.entity.SearchEntity
import com.liz.presentation.ui.search.adapter.SearchRecyclerData
import com.liz.presentation.ui.search.adapter.holder.TitleViewData
import com.liz.presentation.ui.search.viewdata.SearchState
import com.liz.presentation.ui.search.viewdata.SearchUi
import javax.inject.Inject

class SearchConverter @Inject constructor() {

    fun convert(ui: SearchUi, entity: SearchEntity): SearchUi {
        val list = entity.itemsEntity.map {
            SearchRecyclerData.TitleItemData(
                viewData = TitleViewData(
                    title = Html.fromHtml(it.title, HtmlCompat.FROM_HTML_MODE_LEGACY),
                    blogLink = it.bloggerlink
                )
            )
        }
        return ui.copy(
            state = SearchState.SUCCESS,
            viewData = ui.viewData.copy(
                list = list,
                page = ui.viewData.page + 1
            )
        )
    }
}