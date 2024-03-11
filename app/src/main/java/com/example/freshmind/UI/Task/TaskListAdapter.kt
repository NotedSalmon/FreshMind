import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Authentication.User_Login
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.R

class TaskAdapter(private val tasks: MutableList<Task_DataFiles>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val deleteIcon: ImageView = itemView.findViewById(R.id.icon_DeleteTask)
        val editIcon: ImageView = itemView.findViewById(R.id.icon_EditTask)
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

            deleteIcon.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    iconDeleteTask(position)
                }
            }

            editIcon.setOnClickListener{
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    iconEditTask(position)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.titleTextView.text = task.taskTitle
        holder.descriptionTextView.text = task.taskDescription

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
        return tasks.size
    }

    fun iconDeleteTask(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun iconEditTask(position: Int) {

    }

}
