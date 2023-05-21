package com.example.androidsample.ui.search.adapter.holder

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextResultViewHolder(
    private val view: TextView
) : RecyclerView.ViewHolder(view) {

    fun bind(title: String) {
        view.text = title
    }

    companion object {
        fun create(parent: ViewGroup) : TextResultViewHolder {
            return TextResultViewHolder(TextView(parent.context))
        }
    }
}