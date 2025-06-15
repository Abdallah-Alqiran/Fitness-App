package com.example.fitnessapp.presentation.screens.waterScreen

import android.content.Context
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration

object ReminderScheduler {
    private const val WORK_TAG = "WaterReminderWork"

    fun scheduleWaterReminders(context: Context) {
        val prefs = context.getSharedPreferences("WaterReminderPrefs", Context.MODE_PRIVATE)

        val startHour = prefs.getInt("start_hour", 9)
        val startMinute = prefs.getInt("start_minute", 0)
        val endHour = prefs.getInt("end_hour", 20)
        val endMinute = prefs.getInt("end_minute", 0)
        val intervalMinutes = prefs.getInt("interval_minutes", 120)

        val workManager = WorkManager.getInstance(context)


        workManager.cancelAllWorkByTag(WORK_TAG)

        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val startTime = LocalDateTime.of(today, LocalTime.of(startHour, startMinute))
        val endTime = LocalDateTime.of(today, LocalTime.of(endHour, endMinute))

        val nextReminderTime = if (now.isBefore(startTime)) {
            startTime
        } else if (now.isAfter(endTime)) {

            return
        } else {
            val minutesSinceStart = Duration.between(startTime, now).toMinutes()
            val intervalsPassed = (minutesSinceStart / intervalMinutes) + 1
            startTime.plusMinutes(intervalMinutes.toLong() * intervalsPassed)
        }

        var reminderTime = nextReminderTime

        while (reminderTime.isBefore(endTime) || reminderTime.isEqual(endTime)) {
            val delay = Duration.between(now, reminderTime).toMillis()

            if (delay > 0) {
                val workRequest = OneTimeWorkRequestBuilder<WaterReminderWorker>()
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .addTag(WORK_TAG)
                    .build()

                workManager.enqueue(workRequest)
            }

            reminderTime = reminderTime.plusMinutes(intervalMinutes.toLong())
        }
    }

}