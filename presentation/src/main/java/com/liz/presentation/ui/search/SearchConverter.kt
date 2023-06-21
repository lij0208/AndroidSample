package com.liz.presentation.ui.search

import android.text.Html
import androidx.core.text.HtmlCompat
import com.liz.domain.entity.SearchEntity
import com.liz.domain.entity.SearchItemEntity
import com.liz.presentation.ui.search.adapter.SearchRecyclerData
import com.liz.presentation.ui.search.adapter.holder.TitleViewData
import com.liz.presentation.ui.search.viewdata.END_POSITION
import com.liz.presentation.ui.search.viewdata.START_POSITION
import com.liz.presentation.ui.search.viewdata.SearchState
import com.liz.presentation.ui.search.viewdata.SearchUi
import javax.inject.Inject

class SearchConverter @Inject constructor() {

    fun convert(ui: SearchUi, query: String, entity: SearchEntity): SearchUi {
        return ui.copy(
            state = SearchState.SUCCESS,
            viewData = ui.viewData.copy(
                list = getNextList(ui, entity),
                page = getNextPage(ui, entity),
                query = query
            )
        )
    }

    private fun getNextList(ui: SearchUi, entity: SearchEntity): List<SearchRecyclerData> {
        val mappedList = entity.itemsEntity.map {
            createTitleItem(it)
        }
        return if (ui.viewData.page == START_POSITION)
            mappedList
        else {
            val mutableList = ui.viewData.list?.toMutableList() ?: mutableListOf()
            mutableList.addAll(mappedList)
            mutableList
        }
    }

    private fun getNextPage(ui: SearchUi, entity: SearchEntity): Int {
        return if (entity.display < DISPLAY_PER_COUNT) {
            END_POSITION
        } else {
            ui.viewData.page + 1
        }
    }

    private fun createTitleItem(entity: SearchItemEntity): SearchRecyclerData {
        return SearchRecyclerData.TitleItemData(
            viewData = TitleViewData(
                title = Html.fromHtml(entity.title, HtmlCompat.FROM_HTML_MODE_LEGACY),
                blogLink = entity.bloggerlink
            )
        )
    }

    companion object {
        const val DISPLAY_PER_COUNT = 20
        const val INIT_SORT = "sim"
    }
}