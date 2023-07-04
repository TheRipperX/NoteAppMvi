package com.example.noteappmvi.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noteappmvi.R
import com.example.noteappmvi.data.model.NoteEntity
import com.example.noteappmvi.databinding.MainLayoutAdapterBinding
import com.example.noteappmvi.utils.AdapterDiffer
import com.example.noteappmvi.utils.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AdapterMain @Inject constructor(@ApplicationContext private val context: Context): RecyclerView.Adapter<AdapterMain.ViewAdapter>() {

    private lateinit var binding: MainLayoutAdapterBinding
    private var noteList = emptyList<NoteEntity>()

    inner class ViewAdapter: RecyclerView.ViewHolder(binding.root) {
        fun getItems(noteEntity: NoteEntity) {
            binding.apply {
                txtTitle.text = noteEntity.title
                txtDesc.text = noteEntity.desc

                //set category items
                when(noteEntity.category) {
                    HEALTHY -> imgCategorise.setImageResource(R.drawable.healthy)
                    WORK -> imgCategorise.setImageResource(R.drawable.work)
                    LEARNING -> imgCategorise.setImageResource(R.drawable.learning)
                    HOME -> imgCategorise.setImageResource(R.drawable.home)
                }

                //set priority items
                when(noteEntity.priority) {
                    HIGH -> cardAdapter.setBackgroundResource(R.drawable.bg_high)
                    NORMAL -> cardAdapter.setBackgroundResource(R.drawable.bg_normal)
                    LOW -> cardAdapter.setBackgroundResource(R.drawable.bg_low)
                }

                //set pop up menu
                imgMenuItem.setOnClickListener {
                    val popupMenu = PopupMenu(context, it)
                    popupMenu.menuInflater.inflate(R.menu.menu_item_adapter, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { item ->
                        when(item.itemId) {
                            R.id.menu_edit -> {
                                setClickListener?.let {
                                    it(noteEntity, EDIT)
                                }
                                true
                            }
                            R.id.menu_delete -> {
                                setClickListener?.let {
                                    it(noteEntity, DELETE)
                                }
                                true
                            }
                            else -> false
                        }
                    }

                    popupMenu.show()
                }


                root.setOnClickListener { _->

                }

            }
        }
    }

    private var setClickListener: ((NoteEntity, String) -> Unit)? = null

    fun clickItems(listener: (NoteEntity, String) -> Unit) {
        setClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAdapter {
        binding = MainLayoutAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewAdapter()
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: ViewAdapter, position: Int) {
        holder.getItems(noteEntity = noteList[position])
        holder.setIsRecyclable(false)
    }

    fun setDataAdapter(itemsNote: List<NoteEntity>) {

        val adapterDiffer = AdapterDiffer(noteList, itemsNote)
        val diffUtil = DiffUtil.calculateDiff(adapterDiffer)
        noteList = itemsNote
        diffUtil.dispatchUpdatesTo(this)
    }

}