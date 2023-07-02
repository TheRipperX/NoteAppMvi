package com.example.noteappmvi.ui.detail

import android.os.Bundle
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

    private var categoryData = ""
    private var priorityData = ""
    private var idNote = 0

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

        binding.apply {
            //close fragments
            imgClose.setOnClickListener { dismiss() }

            //set data spinner
            lifecycleScope.launch {
                //send
                detailViewModel.detailIntent.send(DetailIntent.SpinnerList)

                detailViewModel.state.collect {
                    when (it) {
                        is DetailState.SpinnerList -> {
                            spinnerCategorise.setAdapterSpinner(it.category.toMutableList()) { str ->
                                categoryData = str
                            }
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

                lifecycleScope.launch {
                    detailViewModel.detailIntent.send(DetailIntent.SaveNote(noteEntity = noteEntity))
                }
            }

        }
    }


}