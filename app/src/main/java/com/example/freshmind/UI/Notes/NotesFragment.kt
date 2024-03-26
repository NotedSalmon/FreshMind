package com.example.freshmind.UI.Notes

import android.app.Activity
import android.content.Intent
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.UI.Task.TaskList_AddTask


class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private val notes: MutableList<Notes_DataFiles> = mutableListOf() // Initialize an empty list
    private lateinit var dbHelper: DBHelper
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(requireContext()) // Initialize DBHelper in onCreate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(com.example.freshmind.R.layout.fragment_notes, container, false)
        recyclerView = view.findViewById(com.example.freshmind.R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        swipeRefreshLayout = view.findViewById(com.example.freshmind.R.id.swipeRefreshLayoutNotes)


        notes.addAll(getAllNotes())

        notesAdapter = NotesAdapter(notes)
        recyclerView.adapter = notesAdapter

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        loadData()
        return view
    }
    private fun loadData() {
        val updatedNotes = dbHelper.showAllNotes(globalUser)
        notes.clear()
        notes.addAll(updatedNotes)
        notesAdapter.notifyDataSetChanged()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun refreshData() {
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.freshmind.R.menu.menu_notes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getAllNotes() : MutableList<Notes_DataFiles> {
        val allNotes = mutableListOf<Notes_DataFiles>()
        dbHelper.showAllNotes(globalUser).forEach {
            allNotes.add(it)
        }
        return allNotes
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.example.freshmind.R.id.action_add_note -> {
                val i = Intent(activity, Notes_AddNote::class.java)
                i.putExtra("user", globalUser)
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Notes added successfully, refresh the Notes list
            notes.clear()
            notes.addAll(getAllNotes())
            notesAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val ADD_TASK_REQUEST_CODE = 1001
    }
}
