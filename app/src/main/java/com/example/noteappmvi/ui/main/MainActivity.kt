package com.example.noteappmvi.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteappmvi.R
import com.example.noteappmvi.databinding.ActivityMainBinding
import com.example.noteappmvi.ui.detail.NoteFragment
import com.example.noteappmvi.utils.*
import com.example.noteappmvi.utils.setAdapterItems
import com.example.noteappmvi.viewmodel.main.MainIntent
import com.example.noteappmvi.viewmodel.main.MainState
import com.example.noteappmvi.viewmodel.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //inject
    @Inject
    lateinit var mainAdapter: AdapterMain

    private var priorityId = 0

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
            // show all items
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

                            mainAdapter.clickItems { notes, s ->
                                if (s == DELETE) {
                                    MainIntent.DeleteNote(notes)
                                }else {
                                    val fragment = NoteFragment()
                                    fragment.show(supportFragmentManager, fragment.tag)
                                }
                            }
                        }

                        // delete note
                        is MainState.DeleteNote -> {
                            Toast.makeText(
                                this@MainActivity,
                                "delete success...",
                                Toast.LENGTH_SHORT
                            ).show() }
                    }
                }
            }

            //filter note
            toolbarMain.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_toolbar_filter -> {

                        priorityAlertDialog()
                        return@setOnMenuItemClickListener true
                    }
                    else -> {
                        return@setOnMenuItemClickListener false
                    }
                }
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val search = menu?.findItem(R.id.menu_toolbar_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search ..."

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0.toString().isNotEmpty())
                    mainViewModel.handelIntent(MainIntent.SearchNote(p0.toString()))
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun priorityAlertDialog() {

        val listItem = arrayOf(ALL, HIGH, NORMAL, LOW)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Filter Note...")
        builder.setSingleChoiceItems(listItem, priorityId) { dialog, which ->
            when(which) {
                0 -> {MainIntent.AllNotes}
                in 1 .. listItem.size -> {MainIntent.FilterNote(listItem[which])}
            }
            priorityId = which
            dialog.dismiss()
        }
        builder.create()
        builder.show()

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}