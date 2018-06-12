package com.sheryv.tubegripper.ui

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.v7.widget.RecyclerView
import com.sheryv.tubegripper.model.Item


abstract class BaseAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding:ViewDataBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder,
                                  position: Int) {
        val obj = getItemForPosition(position)
        holder.bind(obj)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    protected abstract fun getObjForPosition(position: Int): Any

    protected abstract fun getItemForPosition(position: Int): Item

    protected abstract fun getLayoutIdForPosition(position: Int): Int
}
