package com.example.freshmind.UI.Welcome

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Notes_DataFiles
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.R
import com.example.freshmind.UI.Notes.NotesPinnedAdapter
import com.example.freshmind.UI.Task.ClosestTasksAdapter
import com.example.freshmind.UI.globalTheme
import com.example.freshmind.databinding.FragmentWelcomeBinding
import org.w3c.dom.Text
import java.time.ZoneId
import java.time.ZoneOffset

class WelcomeFragment : Fragment(), TaskCountdownTimer.CountdownTickListener {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesPinnedAdapter: NotesPinnedAdapter
    private val notes: MutableList<Notes_DataFiles> = mutableListOf() // Initialize an empty list
    private lateinit var tasksRecycleView: RecyclerView
    private lateinit var tasksDeadlineAdapter: ClosestTasksAdapter
    private val tasks: MutableList<Task_DataFiles> = mutableListOf() // Initialize an empty list
    private lateinit var dbHelper: DBHelper
    private lateinit var txtWelcomeTitle: TextView
    private lateinit var txtImportantNotes: TextView
    private lateinit var txtClosestTasks: TextView
    private lateinit var txtCountdownTimer: TextView

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
        view?.setBackgroundColor(resources.getColor(getColorResource(requireContext())))
        /**
         * Innitialise all the textViews in the Welcome Fragment
         */
        txtWelcomeTitle = view.findViewById(R.id.txtWelcomeTitle)
        txtWelcomeTitle.text = "Welcome, $globalUser"
        txtImportantNotes = view.findViewById(R.id.txtWelcome_Notes)
        txtClosestTasks = view.findViewById(R.id.txtWelcome_Tasks)
        txtCountdownTimer = view.findViewById(R.id.remainingTimeTextView)

        changeTextColors(requireContext(), txtWelcomeTitle, txtImportantNotes, txtClosestTasks, txtCountdownTimer)

        notesRecyclerView = view.findViewById(R.id.recyclerViewPinnedNotes)
        notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksRecycleView = view.findViewById(R.id.recyclerViewClosestTasks)
        tasksRecycleView.layoutManager = LinearLayoutManager(requireContext())


        notes.addAll(getAllPinnedNotes())
        tasks.addAll(getClosestTasks())


        notesPinnedAdapter = NotesPinnedAdapter(notes)
        notesRecyclerView.adapter = notesPinnedAdapter
        tasksDeadlineAdapter = ClosestTasksAdapter(tasks)
        tasksRecycleView.adapter = tasksDeadlineAdapter

        loadData()
        return view
    }

    private fun loadData() {
        val updatedNotes = dbHelper.showAllPinnedNotes(globalUser)
        val updatedTasks = dbHelper.closesTasks(globalUser)
        notes.clear()
        notes.addAll(updatedNotes)
        tasks.clear()
        tasks.addAll(updatedTasks)
        notesPinnedAdapter.notifyDataSetChanged()
        tasksDeadlineAdapter.notifyDataSetChanged()

        startCountdownForTasks(updatedTasks)
    }

    private fun getAllPinnedNotes() : MutableList<Notes_DataFiles> {
        val allNotes = mutableListOf<Notes_DataFiles>()
        dbHelper.showAllPinnedNotes(globalUser).forEach {
            allNotes.add(it)
        }
        return allNotes
    }

    private fun getClosestTasks() : MutableList<Task_DataFiles> {
        val allTasks = mutableListOf<Task_DataFiles>()
        dbHelper.closesTasks(globalUser).forEach {
            allTasks.add(it)
        }
        return allTasks
    }

    override fun onTick(remainingTime: String) {
        txtCountdownTimer = view?.findViewById(R.id.remainingTimeTextView) ?: return
        txtCountdownTimer.text = remainingTime
    }


    private fun startCountdownForTasks(tasks: List<Task_DataFiles>) {
        val currentTimeMillis = System.currentTimeMillis()
        for (task in tasks) {
            val endTime = task.endTime.atStartOfDay(ZoneId.of("Europe/London")) // Convert LocalDate to LocalDateTime with BST
            val endTimeInstant = endTime.toInstant()
            val endTimeMillis = endTimeInstant.toEpochMilli()
            val timeInMillis = endTimeMillis - currentTimeMillis
            if (timeInMillis > 0) {
                val countdownTimer = TaskCountdownTimer(
                    requireContext(),
                    task.taskTitle,
                    timeInMillis,
                    "task_notification_channel", // Replace with your channel ID
                    task.taskID,
                    this // Notification ID can be the task ID for uniqueness
                )
                countdownTimer.start()

                // Break out of the loop after creating the countdown timer for the first task
                break
            }
        }
    }




    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == TaskCountdownTimer.REQUEST_NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now create notifications
                // You might want to restart the countdown timers here if needed
            } else {
                // Permission denied, handle accordingly
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

