package com.example.noteappmvi.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteappmvi.databinding.ActivityMainBinding
import com.example.noteappmvi.ui.detail.NoteFragment
import com.example.noteappmvi.utils.setAdapterItems
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //inject
    @Inject
    lateinit var mainAdapter: AdapterMain

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

            listMain.setAdapterItems(mainAdapter, StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL))

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}