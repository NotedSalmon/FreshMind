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
import com.example.freshmind.UI.Settings.isExpiredTasksEnabled
import com.example.freshmind.UI.Task.ClosestTasksAdapter
import com.example.freshmind.UI.globalTheme
import com.example.freshmind.databinding.FragmentWelcomeBinding
import java.time.ZoneId

/**
 * This is the  Welcome [Fragment].
 * This fragment is the first fragment that the user sees when they open the app.
 * It displays the user's name, pinned notes and the closest tasks.
 * It also displays a countdown timer for the closest task.
 */
class WelcomeFragment : Fragment(), TaskCountdownTimer.CountdownTickListener {

    private var _binding: FragmentWelcomeBinding? = null
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
        dbHelper = DBHelper(requireContext()) // Initialize DBHelper

    }

    /**
     * This function is called when the fragment is created.
     * It inflates the layout of the fragment and sets the text of the textViews.
     * It also sets the background color of the fragment.
     * It also sets the adapter for the recyclerViews.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        globalTheme = dbHelper.retrieveTheme()
        isExpiredTasksEnabled = dbHelper.retrieveHideTasks()
        view?.setBackgroundColor(resources.getColor(getColorResource(requireContext())))
        /**
         * Initialise all the textViews in the Welcome Fragment
         */
        txtWelcomeTitle = view.findViewById(R.id.txtWelcomeTitle)
        txtWelcomeTitle.text = "Welcome, $globalUser"
        txtImportantNotes = view.findViewById(R.id.txtWelcome_Notes)
        txtClosestTasks = view.findViewById(R.id.txtWelcome_Tasks)
        txtCountdownTimer = view.findViewById(R.id.remainingTimeTextView)

        changeTextColors(requireContext(), txtWelcomeTitle, txtImportantNotes, txtClosestTasks, txtCountdownTimer) // Change text colors based on theme

        notesRecyclerView = view.findViewById(R.id.recyclerViewPinnedNotes)
        notesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        tasksRecycleView = view.findViewById(R.id.recyclerViewClosestTasks)
        tasksRecycleView.layoutManager = LinearLayoutManager(requireContext())


        notes.addAll(getAllPinnedNotes()) // Add all pinned notes to the list
        tasks.addAll(getClosestTasks()) // Add all closest tasks to the list


        notesPinnedAdapter = NotesPinnedAdapter(notes)
        notesRecyclerView.adapter = notesPinnedAdapter
        tasksDeadlineAdapter = ClosestTasksAdapter(tasks)
        tasksRecycleView.adapter = tasksDeadlineAdapter

        loadData()
        return view
    }

    /**
     * This function is called when the fragment is first created.
     * It loads the data from the database and updates the recyclerViews.
     */
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

    /**
     * This function gets all the pinned notes from the database.
     * It returns a list of all the pinned notes but only the ones that are pinned.
     */
    private fun getAllPinnedNotes() : MutableList<Notes_DataFiles> {
        val allNotes = mutableListOf<Notes_DataFiles>()
        dbHelper.showAllPinnedNotes(globalUser).forEach {
            allNotes.add(it)
        }
        return allNotes
    }

    /**
     * This function gets all the closest tasks from the database.
     * It returns a list of all the closest tasks but only the ones that are closest.
     */
    private fun getClosestTasks() : MutableList<Task_DataFiles> {
        val allTasks = mutableListOf<Task_DataFiles>()
        dbHelper.closesTasks(globalUser).forEach {
            allTasks.add(it)
        }
        return allTasks
    }

    /**
     * This function is called when the countdown timer ticks.
     */
    override fun onTick(remainingTime: String) {
        txtCountdownTimer = view?.findViewById(R.id.remainingTimeTextView) ?: return
        txtCountdownTimer.text = remainingTime
    }

    /**
     * This function is called when the countdown timer finishes.
     * After the countdown timer finishes, it sends a notification to the user.
     */
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
                    "task_notification_channel",
                    task.taskID,
                    this
                )
                countdownTimer.start()

                // Break out of the loop after creating the countdown timer for the first task
                break
            }
        }
    }

    // TODO: Implement the notification permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == TaskCountdownTimer.REQUEST_NOTIFICATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, handle accordingly
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

