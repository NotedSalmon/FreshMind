package com.example.freshmind.UI.Notes

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.example.freshmind.Extras.getColorResource

/**
 * This fragment displays the list of notes in a RecyclerView
 * It also has a swipe refresh layout to refresh the data
 */
class NotesFragment : Fragment(), NotesAdapter.EditNoteClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private val notes: MutableList<Notes_DataFiles> = mutableListOf() // Initialize an empty list
    private lateinit var dbHelper: DBHelper
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(requireContext()) // Initialize DBHelper in onCreate()
    }

    /**
     * This function inflates the layout for the fragment
     * It also sets up the RecyclerView and SwipeRefreshLayout
     * It also loads the data from the database and displays it in the RecyclerView
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(com.example.freshmind.R.layout.fragment_notes, container, false)
        view?.setBackgroundColor(resources.getColor(getColorResource(requireContext()))) // Set background color
        recyclerView = view.findViewById(com.example.freshmind.R.id.notesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        swipeRefreshLayout = view.findViewById(com.example.freshmind.R.id.swipeRefreshLayoutNotes)


        notes.addAll(getAllNotes()) // Add all notes from the database to the list

        notesAdapter = NotesAdapter(notes,this)
        recyclerView.adapter = notesAdapter

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        loadData()
        return view
    }
    /**
     * This function loads the data from the database and updates the RecyclerView
     */
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

    /**
     * This function inflates the menu for the notes fragment
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.example.freshmind.R.menu.menu_notes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * This function gets all the notes from the database
     */
    private fun getAllNotes() : MutableList<Notes_DataFiles> {
        val allNotes = mutableListOf<Notes_DataFiles>()
        dbHelper.showAllNotes(globalUser).forEach {
            allNotes.add(it)
        }
        return allNotes
    }

    /**
     * This function is called when an item in the menu is selected
     * It opens the AddNote activity when the add note icon is clicked
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            com.example.freshmind.R.id.action_add_note -> {
                val i = Intent(activity, Notes_AddNote::class.java)
                i.putExtra("user", globalUser)
                startActivityForResult(i, ADD_NOTE_REQUEST_CODE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * This function is called when an activity returns a result
     * It refreshes the data when a note is added or edited
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Notes added successfully, refresh the Notes list
            refreshData()
        }
        if(requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Notes edited successfully, refresh the Notes list
            refreshData()
        }
    }

    /**
     * This function is called when the edit icon is clicked,
     * it opens the EditNote activity and sends the note details to it
     * It also sends the request code so that when the activity is finished, the onActivityResult
     * will be called and the data will be refreshed automatically
     */
    override fun iconEditNote(position: Int, context: Context) {
        val note = notes[position]
        val intent = Intent(context, Notes_EditNote::class.java).apply {
            putExtra("noteID", note.noteID)
            putExtra("noteTitle", note.noteTitle)
            putExtra("noteContent", note.noteContent)
            putExtra("noteDateCreated", note.dateCreated)
            putExtra("checkPin", note.isPinned)
        }
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE)
    }

    /**
     * Constants for the request codes, these are used to identify the request
     * when the activity returns a result
     */
    companion object {
        private const val ADD_NOTE_REQUEST_CODE = 1001
        private const val EDIT_NOTE_REQUEST_CODE = 1002
    }
}
