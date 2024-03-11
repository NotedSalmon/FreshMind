package com.example.freshmind.UI.Notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.R
import com.example.freshmind.Database.Notes_DataFiles

class NoteAdapter(private val notes: MutableList<Notes_DataFiles>) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

        init {
            // Set item click listener
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedItemPosition)
                    selectedItemPosition = position
                    notifyItemChanged(selectedItemPosition)
                }
            }

            // Set item long click listener for deletion
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    deleteNote(position)
                }
                true // Consume the long click event
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.noteTitle
        holder.contentTextView.text = note.noteContent

        holder.itemView.isSelected = position == selectedItemPosition

        holder.itemView.setOnClickListener {
            val previousPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition

            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedItemPosition)
        }

        if (selectedItemPosition == position) {
            holder.itemView.setBackgroundResource(R.color.teal_200)
        } else {
            holder.itemView.setBackgroundResource(R.color.white)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    private fun deleteNote(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
        if (selectedItemPosition == position) {
            selectedItemPosition = RecyclerView.NO_POSITION
        }
    }
}
