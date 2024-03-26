package com.example.freshmind.UI.Notes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.R
import com.example.freshmind.UI.Task.TaskAdapter
import com.example.freshmind.UI.Task.TaskList_EditTask


class NotesAdapter(private val notes: MutableList<Notes_DataFiles>) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private lateinit var dbHelper: DBHelper

    /**
     * This class is the view holder for the RecyclerView
     * It holds the views for the item_task layout
     * It also sets the click listeners for the delete and edit icons
     */

    inner class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtNotes_Title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtNotes_Content)
        val deleteIcon: ImageView = itemView.findViewById(R.id.icon_DeleteNotes)
        val editIcon: ImageView = itemView.findViewById(R.id.icon_EditNotes)
        var currentNoteID: Int = -1
        init {
            dbHelper = DBHelper(itemView.context)
            // Set item click listener
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedItemPosition)
                    selectedItemPosition = position
                    notifyItemChanged(selectedItemPosition)
                }
            }

            deleteIcon.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    iconDeleteNote(position)
                }
            }

            editIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    iconEditNote(position, itemView.context)
                }
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return NotesViewHolder(view)
    }

    /**
     * This function is called when the RecyclerView needs to display an item
     * It binds the data to the view holder
     */

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.noteTitle
        holder.descriptionTextView.text = note.noteContent
        holder.currentNoteID = note.noteID

        // Highlight selected item
        holder.itemView.isSelected = position == selectedItemPosition

        if (selectedItemPosition == position) {
            holder.itemView.setBackgroundResource(R.color.purple_200)
            holder.deleteIcon.visibility = View.VISIBLE
            holder.editIcon.visibility = View.VISIBLE
        } else {
            holder.itemView.setBackgroundResource(R.color.white)
            holder.deleteIcon.visibility = View.GONE
            holder.editIcon.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun iconDeleteNote(position: Int) {
        val deletedNoteID = notes[position].noteID
        notes.removeAt(position)
        notifyItemRemoved(position)
        dbHelper.deleteNotes(deletedNoteID)
    }

    /**
     * This function is called when the edit icon is clicked
     * It opens the TaskList_EditTask activity with the task details
     */
    fun iconEditNote(position: Int, context: Context) {
        val note = notes[position]
        val intent = Intent(context, Notes_EditNote::class.java).apply {
            putExtra("noteID", note.noteID)
            putExtra("noteTitle", note.noteTitle)
            putExtra("noteContent", note.noteContent)
            putExtra("dateCreated", note.dateCreated)
        }
        context.startActivity(intent)
    }

}
