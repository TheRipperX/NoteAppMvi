package com.example.noteappmvi.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.noteappmvi.R
import com.example.noteappmvi.data.model.NoteEntity
import com.example.noteappmvi.databinding.FragmentNoteBinding
import com.example.noteappmvi.utils.ADD
import com.example.noteappmvi.utils.BUNDLE_ID
import com.example.noteappmvi.utils.EDIT
import com.example.noteappmvi.utils.isSetDataSpinner
import com.example.noteappmvi.utils.setAdapterSpinner
import com.example.noteappmvi.viewmodel.detail.DetailIntent
import com.example.noteappmvi.viewmodel.detail.DetailState
import com.example.noteappmvi.viewmodel.detail.DetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNoteBinding

    //view model
    private val detailViewModel: DetailViewModel by viewModels()

    // note entity
    @Inject
    lateinit var noteEntity: NoteEntity

    //lists spinner
    private var categoryList = mutableListOf<String>()
    private var priorityList = mutableListOf<String>()

    private var categoryData = ""
    private var priorityData = ""
    private var idNote = 0

    private var type = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        main()
    }

    private fun main() {

        idNote = arguments?.getInt(BUNDLE_ID) ?: 0

        type = if (idNote == 0) ADD else EDIT

        binding.apply {
            //close fragments
            imgClose.setOnClickListener { dismiss() }

            //set data spinner
            lifecycleScope.launch {
                //send
                detailViewModel.detailIntent.send(DetailIntent.SpinnerList)

                if(type == EDIT)
                    detailViewModel.detailIntent.send(DetailIntent.FindNote(idNote))

                detailViewModel.state.collect {
                    when (it) {
                        is DetailState.SpinnerList -> {
                            //add list to data


                            categoryList.addAll(it.category)
                            spinnerCategorise.setAdapterSpinner(it.category.toMutableList()) { str ->
                                categoryData = str
                            }
                            priorityList.addAll(it.priority)
                            spinnerPriority.setAdapterSpinner(it.priority.toMutableList()) { str ->
                                priorityData = str
                            }
                        }

                        is DetailState.SaveNote -> {
                            Toast.makeText(requireContext(), "Save Note Success", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }

                        is DetailState.Error -> {
                            Toast.makeText(requireContext(), it.error, Toast.LENGTH_SHORT).show()
                        }
                        is DetailState.Idle -> {}

                        is DetailState.EditNote -> {
                            Toast.makeText(requireContext(), "Edit Note Success", Toast.LENGTH_SHORT).show()
                            dismiss()
                        }

                        is DetailState.DetailNote -> {
                            edtTitle.setText(it.noteEntity.title)
                            edtDec.setText(it.noteEntity.desc)
                            spinnerCategorise.setSelection(categoryList.isSetDataSpinner(it.noteEntity.category))
                            spinnerPriority.setSelection(priorityList.isSetDataSpinner(it.noteEntity.priority))
                        }
                    }
                }
            }

            btnSave.setOnClickListener {

                val title = edtTitle.text.toString()
                val desc = edtDec.text.toString()
                noteEntity.id = idNote
                noteEntity.title = title
                noteEntity.desc = desc
                noteEntity.category = categoryData
                noteEntity.priority = priorityData

                if(title.isEmpty() || desc.isEmpty() || categoryData.isEmpty() || priorityData.isEmpty()){
                    Toast.makeText(
                        requireContext(),
                        "Please Enter Valid Data ...",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                lifecycleScope.launch {
                    if (type == ADD)
                        detailViewModel.detailIntent.send(DetailIntent.SaveNote(noteEntity = noteEntity))
                    else
                        detailViewModel.detailIntent.send(DetailIntent.EditNote(noteEntity = noteEntity))
                }
            }

        }
    }


}