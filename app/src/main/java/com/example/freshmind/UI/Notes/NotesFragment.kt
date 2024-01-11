package com.example.freshmind.UI.Notes

// NotesFragment.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshmind.databinding.FragmentNotesBinding
import com.example.freshmind.Database.Note

class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.recyclerViewNotes.layoutManager = LinearLayoutManager(requireContext())
        val notes = generateDummyNotes()
        noteAdapter = NoteAdapter(notes)
        binding.recyclerViewNotes.adapter = noteAdapter
    }

    private fun generateDummyNotes(): List<Note> {
        // Replace this with your actual data retrieval logic
        return listOf(
            Note(1, "Note 1", "Content for Note 1"),
            Note(2, "Note 2", "Content for Note 2"),
            Note(3, "Note 3", "Content for Note 3")
            // Add more notes as needed
        )
    }
}
