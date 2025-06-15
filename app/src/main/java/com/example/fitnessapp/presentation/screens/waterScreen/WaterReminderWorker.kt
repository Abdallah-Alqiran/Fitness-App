package com.example.fitnessapp.presentation.screens.waterScreen


import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.fitnessapp.R
import androidx.core.content.ContextCompat

class WaterReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        sendNotification(applicationContext)
        return Result.success()
    }
    private fun sendNotification(context: Context) {
        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java)

        notificationManager?.let {
            val builder = NotificationCompat.Builder(context, "water_reminder_channel")
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Water Reminder 💧")
                .setContentText("It's Time To Drink Water !💙")
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            it.notify(1, builder.build())
        }
    }

}