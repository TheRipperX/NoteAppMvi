package com.example.noteappmvi.utils

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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


fun Spinner.setAdapterSpinner(list: MutableList<*>, text: (String) -> Unit) {
    val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, list)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    this.adapter = adapter
    this.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            text(list[p2].toString())
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }
}

fun MutableList<*>.isSetDataSpinner(str: String): Int{
    var number = 0

    for (i in this.indices) {
        if(this[i] == str){
            number = i
            break
        }
    }


    return number

}