package com.example.freshmind.UI.Welcome

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.freshmind.R

class TaskCountdownTimer(
    private val context: Context,
    private val taskTitle: String,
    private val timeInMillis: Long,
    private val channelId: String,
    private val notificationId: Int,
    private val countdownTickListener: CountdownTickListener
) : CountDownTimer(timeInMillis, 1000) {

    companion object {
        const val REQUEST_NOTIFICATION_PERMISSION_CODE = 1001
    }

    override fun onTick(millisUntilFinished: Long) {
        val seconds = millisUntilFinished / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        val remainingHours = hours % 24
        val remainingMinutes = minutes % 60
        val remainingSeconds = seconds % 60

        val remainingTimeString = String.format(
            "Time Remaining: %02d Days, %02d Hours, %02d Minutes, %02d Seconds",
            days,
            remainingHours,
            remainingMinutes,
            remainingSeconds
        )

        countdownTickListener.onTick(remainingTimeString)
    }

    override fun onFinish() {
        // Send notification to the user when countdown finishes
        createNotification()
    }

    private fun createNotification() {
        val notificationManager = NotificationManagerCompat.from(context)

        // Check if the app has the required notification permission
        if (context.checkSelfPermission(Manifest.permission.ACCESS_NOTIFICATION_POLICY) == PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Task Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(context, channelId)
                .setContentTitle("Task Finished")
                .setContentText("Your task '$taskTitle' has finished.")
                .setSmallIcon(androidx.core.R.drawable.notification_icon_background)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()

            try {
                notificationManager.notify(notificationId, notification)
            } catch (e: SecurityException) {
                // Handle the SecurityException here
                // For example, log the error or show a toast message to the user
            }
        } else {
            // Request notification permission from the user
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
                REQUEST_NOTIFICATION_PERMISSION_CODE
            )
        }
    }

    interface CountdownTickListener {
        fun onTick(remainingTime: String)
    }
}
