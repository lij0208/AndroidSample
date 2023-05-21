package com.example.androidsample.ui.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.androidsample.ui.search.adapter.holder.TextResultViewHolder

class SearchAdapter : ListAdapter<String, TextResultViewHolder>(SearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextResultViewHolder {
        return TextResultViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: TextResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SearchDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}