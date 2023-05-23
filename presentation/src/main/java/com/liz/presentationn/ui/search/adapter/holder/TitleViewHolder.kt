package com.liz.presentationn.ui.search.adapter.holder

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.liz.presentationn.databinding.ItemSearchTitleBinding

class TitleViewHolder(
    private val binding: ItemSearchTitleBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.title.movementMethod = LinkMovementMethod.getInstance()
    }

    fun bind(viewData: TitleViewData) {
        binding.title.text = viewData.title
    }

    companion object {
        fun create(parent: ViewGroup): TitleViewHolder {
            return TitleViewHolder(
                ItemSearchTitleBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }
}