package com.example.noteappmvi.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteappmvi.databinding.ActivityMainBinding
import com.example.noteappmvi.ui.detail.NoteFragment
import com.example.noteappmvi.utils.setAdapterItems
import com.example.noteappmvi.viewmodel.main.MainIntent
import com.example.noteappmvi.viewmodel.main.MainState
import com.example.noteappmvi.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //inject
    @Inject
    lateinit var mainAdapter: AdapterMain

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        main()
    }

    private fun main() {
        binding.apply {
            //ACTION BAR
            setSupportActionBar(toolbarMain)
            //open fragments
            floatingBtn.setOnClickListener {
                NoteFragment().show(supportFragmentManager, NoteFragment().tag)
            }

            mainViewModel.handelIntent(MainIntent.AllNotes)

            lifecycleScope.launch {
                mainViewModel.state.collect {

                    when(it) {
                        is MainState.Empty -> {
                            listMain.isVisible = false
                            emptyLayout.isVisible = true
                        }
                        is MainState.AllNotes -> {
                            listMain.isVisible = true
                            emptyLayout.isVisible = false
                            mainAdapter.setDataAdapter(it.list)
                            listMain.setAdapterItems(mainAdapter, StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))

                            mainAdapter.clickItems { noteEntity, s ->

                            }
                        }
                    }
                }
            }


            //listMain.setAdapterItems(mainAdapter, StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL))


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}