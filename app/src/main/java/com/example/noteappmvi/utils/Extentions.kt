package com.example.noteappmvi.utils

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AdapterDiffer(private val old: List<*>, private val new: List<*>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }

}

fun RecyclerView.setAdapterItems(adapter: RecyclerView.Adapter<*>, layoutManager: RecyclerView.LayoutManager) {
    this.layoutManager = layoutManager
    this.adapter = adapter
    this.setHasFixedSize(false)
}