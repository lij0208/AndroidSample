package com.liz.presentationn.ui.search

import android.text.Html
import androidx.core.text.HtmlCompat
import com.liz.domain.entity.SearchEntity
import com.liz.presentationn.ui.search.adapter.SearchRecyclerData
import com.liz.presentationn.ui.search.adapter.holder.TitleViewData
import javax.inject.Inject

class SearchConverter @Inject constructor() {

    fun convert(entity: SearchEntity): List<SearchRecyclerData> {
        return entity.itemsEntity.map {
            SearchRecyclerData.TitleItemData(
                viewData = TitleViewData(
                    title = Html.fromHtml(it.title, HtmlCompat.FROM_HTML_MODE_LEGACY),
                    blogLink = it.bloggerlink
                )
            )
        }
    }
}