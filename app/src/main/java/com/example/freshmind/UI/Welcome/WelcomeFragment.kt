package com.example.freshmind.UI.Welcome

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
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.R
import com.example.freshmind.UI.Notes.NotesPinnedAdapter
import com.example.freshmind.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesPinnedAdapter: NotesPinnedAdapter
    private val notes: MutableList<Notes_DataFiles> = mutableListOf() // Initialize an empty list
    private lateinit var dbHelper: DBHelper
    private lateinit var txtWelcomeTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DBHelper(requireContext()) // Initialize DBHelper in onCreate()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        txtWelcomeTitle = view.findViewById(R.id.txtWelcomeTitle)
        txtWelcomeTitle.text = "Welcome, $globalUser"
        notesRecyclerView = view.findViewById(R.id.recyclerViewPinnedNotes)
        notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        notes.addAll(getAllPinnedNotes())

        notesPinnedAdapter = NotesPinnedAdapter(notes)
        notesRecyclerView.adapter = notesPinnedAdapter

        loadData()
        return view
    }

    private fun loadData() {
        val updatedNotes = dbHelper.showAllPinnedNotes(globalUser)
        notes.clear()
        notes.addAll(updatedNotes)
        notesPinnedAdapter.notifyDataSetChanged()
    }

    private fun getAllPinnedNotes() : MutableList<Notes_DataFiles> {
        val allNotes = mutableListOf<Notes_DataFiles>()
        dbHelper.showAllPinnedNotes(globalUser).forEach {
            allNotes.add(it)
        }
        return allNotes
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

