package com.example.freshmind.UI.Notes

// NotesFragment.kt
import TaskAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.databinding.FragmentNotesBinding
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R

class NotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tasks, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val notes = generateDummyNotes()
        noteAdapter = NoteAdapter(notes)
        recyclerView.adapter = noteAdapter

        return view
    }

    private fun generateDummyNotes(): MutableList<Notes_DataFiles> {
        // Replace this with your actual data retrieval logic
        return mutableListOf(
            Notes_DataFiles(1, "Note 1", "Content for Note 1", null, null),
            Notes_DataFiles(2, "Note 2", "Content for Note 2", null, null),
            Notes_DataFiles(3, "Note 3", "Content for Note 3", null, null)
            // Add more notes as needed
        )
    }
}
