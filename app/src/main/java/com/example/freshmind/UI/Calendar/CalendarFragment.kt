package com.example.freshmind.UI.Calendar
//https://github.com/kizitonwose/Calendar


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshmind.Authentication.globalUser
import com.example.freshmind.Database.DBHelper
import com.example.freshmind.Database.Task_DataFiles
import com.example.freshmind.Extras.changeTextBoxColor
import com.example.freshmind.Extras.changeTextColors
import com.example.freshmind.Extras.getColorResource
import com.example.freshmind.Extras.getToolbarColor
import com.example.freshmind.R
import com.example.freshmind.UI.BaseFragment
import com.example.freshmind.UI.Calendar.Utils.addStatusBarColorUpdate
import com.example.freshmind.UI.Calendar.Utils.getColorCompat
import com.example.freshmind.UI.Calendar.Utils.makeInVisible
import com.example.freshmind.UI.Calendar.Utils.makeVisible
import com.example.freshmind.UI.Calendar.Utils.setTextColorRes
import com.example.freshmind.UI.HasBackButton
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.example.freshmind.databinding.CalendarDayLayoutBinding
import com.example.freshmind.databinding.CalendarHeaderLayoutBinding
import com.example.freshmind.databinding.FragmentCalendarBinding
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/**
 * CalendarFragment is a fragment that displays a calendar with events.
 * It uses a [RecyclerView] to display the events for the selected date.
 * It also shows a dialog to add new events.
 *
 * It uses the library referenced on line 2 to allow customisation of the calendar view.
 */

class CalendarFragment : BaseFragment(R.layout.fragment_calendar), HasBackButton {
    private lateinit var recyclerView: RecyclerView
    private lateinit var calendarTaskAdapter: CalendarAdapter
    override val titleRes: Int = R.string.calendar_title
    private lateinit var dbHelper: DBHelper
    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private val titleSameYearFormatter = DateTimeFormatter.ofPattern("MMMM") // Format for the title when the year is the same
    private val titleFormatter = DateTimeFormatter.ofPattern("MMM yyyy") // Format for the title when the year is different
    private val selectionFormatter = DateTimeFormatter.ofPattern("d MMM yyyy") // Format for the selected date

    private val calendarTasks: MutableList<Pair<LocalDate, Task_DataFiles>> = mutableListOf()
    private val calendarTasksView: MutableList<Pair<LocalDate, Task_DataFiles>> = mutableListOf()


    private lateinit var binding: FragmentCalendarBinding

    /**
     * This function is called when the fragment is created.
     * It sets up the calendar view and the adapter for the recycler view.
     * It also populates the calendarTasks map with tasks.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(resources.getColor(getColorResource(requireContext()))) // Set background color
        addStatusBarColorUpdate(R.color.indigo)
        binding = FragmentCalendarBinding.bind(view)
        selectedDate = today // Set the selected date to today
        calendarTaskAdapter = CalendarAdapter(calendarTasksView) // Create a new adapter for the tasks that are displayed in the calendar

        binding.calendarView.apply { // Set up the calendar view
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = calendarTaskAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), RecyclerView.VERTICAL))
        }
        dbHelper = DBHelper(requireContext())

        // Call getAllTasks to retrieve tasks from the database
        val allTasks = getAllTasks()
        recyclerView = view.findViewById(R.id.recycleView_Calendar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recyclerView.adapter = calendarTaskAdapter // Set the adapter for the recycler view

        // Populate the calendarTasks map with tasks
        calendarTasks.addAll(populateCalendarFromTaskList(allTasks)) // This is used to display the dots in the calendar
        calendarTasksView.addAll(populateCalendarFromTaskList(allTasks)) // This is used to update the adapter when a date is selected

        // This sets the scroll listener so the month is changed when the user scrolls to the left/right
        binding.calendarView.monthScrollListener = {
            activityToolbar.title = if (it.yearMonth.year == today.year) {
                titleSameYearFormatter.format(it.yearMonth)
            } else {
                titleFormatter.format(it.yearMonth)
            }
            // Select the first day of the visible month.
            selectDate(it.yearMonth.atDay(1))
        }
        val daysOfWeek = daysOfWeek()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(50)
        val endMonth = currentMonth.plusMonths(50)
        configureBinders(daysOfWeek) // Configure the binders for the calendar view
        binding.calendarView.apply {
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        if (savedInstanceState == null) {
            // Show today's events initially.
            binding.calendarView.post { selectDate(today) }
        }
    }

    private fun getAllTasks(): List<Task_DataFiles> {
        return dbHelper.showAllTasks(globalUser)
    }

    /**
     * This function is called when a date is selected in the calendar.
     * It updates the selectedDate variable and updates the adapter to show the tasks for the selected date.
     * It also updates the selected date text view.
     */
    private fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
            binding.calendarView.notifyDateChanged(date)

            binding.txtCalendarSelectedDate.text = selectionFormatter.format(date)
            updateAdapterForDate(date)
        }
    }

    /**
     * This function takes a list of tasks and returns a list of pairs of LocalDate and Task.
     * This is used to populate the calendarTasks map.
     */
    private fun populateCalendarFromTaskList(tasks: List<Task_DataFiles>): Collection<Pair<LocalDate, Task_DataFiles>> {
        return tasks.map { it.startTime to it }
    }

    /**
     * This function updates the adapter to show the tasks for the selected date.
     * It changes only the calendarTasksView list and notifies the adapter that the data set has changed.
     */
    private fun updateAdapterForDate(date: LocalDate) {
        calendarTaskAdapter.apply {
            calendarTasksView.clear()
            calendarTasksView.addAll(populateCalendarFromTaskList(getAllTasks()))
            notifyDataSetChanged()
        }
        val tasksForSelectedDate = dbHelper.showTasksForDate(globalUser, date)
        val updatedTasksList = tasksForSelectedDate.map { date to it } // Create a list of pairs of LocalDate and Task
        calendarTaskAdapter.updateTasks(updatedTasksList) // Update the adapter with the tasks for the selected date
        binding.txtCalendarSelectedDate.text = selectionFormatter.format(date)
    }

    /**
     * This function configures the binders for the calendar view.
     * It sets the day binder to display the day of the month and the dotView if there are tasks for that day.
     * It sets the month header binder to display the days of the week.
     * It also sets the click listener for each day to select the date.
     */
    private fun configureBinders(daysOfWeek: List<DayOfWeek>) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay // Will be set when this container is bound.
            val binding = CalendarDayLayoutBinding.bind(view)
            init {
                view.setOnClickListener { // Set the click listener for each day
                    if (day.position == DayPosition.MonthDate) {
                        selectDate(day.date)
                    }
                }
            }
        }
        /**
         * This class is used to bind the days of the week to the corresponding TextViews.
         */
        binding.calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                val textView = container.binding.txtCalendarDay
                val dotView = container.binding.txtCalendarDotView
                changeTextBoxColor(requireContext(), binding.txtCalendarSelectedDate) // Change the text color of the selected date text view

                textView.text = data.date.dayOfMonth.toString()

                if (data.position == DayPosition.MonthDate) { // If the day is in the current month
                    textView.makeVisible()
                    // Check if the date has any tasks associated with it
                    val hasTasks = calendarTasks.any { it.first == data.date }
                    dotView.isVisible = hasTasks // Set dot visibility based on tasks
                    when (data.date) {
                        today -> { // If the date is today, adds a yellow circle to indicate it is today's date
                            textView.setTextColorRes(R.color.deepPurple)
                            textView.setBackgroundResource(R.drawable.calendar_today_bg)
                            dotView.makeInVisible() // Hide dot for today's date
                        }
                        selectedDate -> { // If the date is selected, adds a purple circle to indicate it is the selected date
                            textView.setTextColorRes(R.color.deepPurple)
                            textView.setBackgroundResource(R.drawable.calendar_selected_bg)
                            dotView.makeInVisible()
                        }
                        else -> { // If the date is not today or selected, set the text color to white
                            changeTextColors(requireContext(), textView) // Change the text color of the day
                            textView.background = null
                            dotView.isVisible = hasTasks
                        }
                    }
                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }
        /**
         * This class is used to bind the month header to the corresponding TextViews.
         */
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderLayoutBinding.bind(view).legendLayout.root
        }
        binding.calendarView.monthHeaderBinder =
            object : MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                    // Setup each header day text if we have not done that already.
                    if (container.legendLayout.tag == null) {
                        container.legendLayout.tag = data.yearMonth
                        container.legendLayout.children.map { it as TextView }
                            .forEachIndexed { index, tv ->
                                tv.text = daysOfWeek[index].name.first().toString()
                                tv.setTextColorRes(R.color.white)
                            }
                    }
                }
            }
    }
}
