package com.sheryv.tubegripper.ui

import android.support.v7.widget.RecyclerView
import android.databinding.ViewDataBinding
import com.sheryv.tubegripper.model.Item


class ItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Item) {
//        binding.setVariable(BR.item, item)
//        binding.item = item
        binding.executePendingBindings()
    }

}
