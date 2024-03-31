package com.example.freshmind.UI.Notes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.R

class NotesPinnedAdapter(private val notes: MutableList<Notes_DataFiles>) : RecyclerView.Adapter<NotesPinnedAdapter.NotesPinnedViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION
    private lateinit var dbHelper: DBHelper

    /**
     * This class is the view holder for the RecyclerView
     * It holds the views for the item_task layout
     * It also sets the click listeners for the delete and edit icons
     */

    inner class NotesPinnedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.txtNotes_Title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.txtNotes_Content)
        private val deleteIcon: ImageView = itemView.findViewById(R.id.icon_DeleteNotes)
        private val editIcon: ImageView = itemView.findViewById(R.id.icon_EditNotes)
        var currentNoteID: Int = -1
        init {
            dbHelper = DBHelper(itemView.context)
            deleteIcon.visibility = View.GONE
            editIcon.visibility = View.GONE
            // Set item click listener
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(selectedItemPosition)
                    selectedItemPosition = position
                    notifyItemChanged(selectedItemPosition)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesPinnedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notes, parent, false)
        return NotesPinnedViewHolder(view)
    }

    /**
     * This function is called when the RecyclerView needs to display an item
     * It binds the data to the view holder
     */

    override fun onBindViewHolder(holder: NotesPinnedViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.noteTitle
        holder.descriptionTextView.text = note.noteContent
        holder.currentNoteID = note.noteID

        // Highlight selected item
        holder.itemView.isSelected = position == selectedItemPosition
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}

class NotesAdapter(private val notes: MutableList<Notes_DataFiles>, private val editNoteClickListener: EditNoteClickListener) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

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
                    editNoteClickListener.iconEditNote(adapterPosition, itemView.context)
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
     * When the Edit icon is clicked, the EditNoteClickListener interface is called
     * This will callback to NotesFragment to handle the edit note action
     * When the function finishes, the list will refresh
     */
    interface EditNoteClickListener {
        fun iconEditNote(position: Int, context: Context)
    }
}

