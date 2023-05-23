package com.liz.presentation.ui.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.liz.presentation.ui.search.adapter.holder.TitleViewHolder

class SearchAdapter :
    ListAdapter<SearchRecyclerData, RecyclerView.ViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            SearchType.TITLE.ordinal -> TitleViewHolder.create(parent)
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TitleViewHolder -> holder.bind((getItem(position) as SearchRecyclerData.TitleItemData).viewData)
        }
    }
}

class SearchDiffCallback : DiffUtil.ItemCallback<SearchRecyclerData>() {
    override fun areItemsTheSame(
        oldItem: SearchRecyclerData,
        newItem: SearchRecyclerData
    ): Boolean {
        return if (oldItem is SearchRecyclerData.TitleItemData && newItem is SearchRecyclerData.TitleItemData) {
            oldItem.viewData.blogLink == newItem.viewData.blogLink
        } else {
            false
        }
    }

    override fun areContentsTheSame(
        oldItem: SearchRecyclerData,
        newItem: SearchRecyclerData
    ): Boolean {
        return oldItem == newItem
    }
}

enum class SearchType {
    TITLE
}