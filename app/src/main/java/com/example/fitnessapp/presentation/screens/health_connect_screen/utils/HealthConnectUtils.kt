package com.example.fitnessapp.presentation.screens.health_connect_screen.utils

import android.content.Context
import android.util.Log
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.fitnessapp.presentation.screens.health_connect_screen.model.DataRecord
import com.example.fitnessapp.presentation.screens.health_connect_screen.model.DataType
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone


object HealthConnectUtils {
    private var healthConnectClient: HealthConnectClient? = null
    val PERMISSIONS = setOf(
        HealthPermission.getReadPermission(StepsRecord::class),
        HealthPermission.getReadPermission(SleepSessionRecord::class),
        HealthPermission.getReadPermission(DistanceRecord::class),
        HealthPermission.getReadPermission(ExerciseSessionRecord::class)
    )
    private val dateTimeFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")


    fun checkForHealthConnectInstalled(context: Context): Int {
        val availabilityStatus =
            HealthConnectClient.getSdkStatus(context, "com.google.android.apps.healthdata")
        when (availabilityStatus) {
            HealthConnectClient.SDK_UNAVAILABLE -> {
                // The Health Connect SDK is unavailable on this device at the time.
                // This can be due to the device running a lower than required Android Version.
                // Apps should hide any integration points to Health Connect in this case.
            }

            HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> {
                // The Health Connect SDK APIs are currently unavailable, the provider is either not installed
                // or needs to be updated. You may choose to redirect to package installers to find a suitable APK.
            }

            HealthConnectClient.SDK_AVAILABLE -> {
                // Health Connect SDK is available on this device.
                // You can proceed with querying data from Health Connect using the client.
                healthConnectClient = HealthConnectClient.getOrCreate(context)
            }
        }
        return availabilityStatus
    }


    //Returns false when Health connect permissions are not given or returns true if permissions are already granted
    suspend fun checkPermissions(context: Context): Boolean {
        val granted = healthConnectClient?.permissionController?.getGrantedPermissions()
        val isGranted = granted?.containsAll(PERMISSIONS) == true

        // Save to SharedPreferences
        val prefs = context.getSharedPreferences("health_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("isHealthGranted", isGranted).apply()

        return isGranted
    }


    //Function that gives total steps for each day for given interval
    suspend fun readStepsForInterval(interval: Long): List<DataRecord> {
        val startTime: ZonedDateTime =
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(interval - 1)

        val endTime = LocalDateTime.now().atZone(TimeZone.getDefault().toZoneId()).minusMinutes(1)
            .plusSeconds(59)
        val response =
            healthConnectClient?.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(StepsRecord.COUNT_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        startTime.toLocalDate().atStartOfDay(),
                        endTime.toLocalDateTime()
                    ),
                    timeRangeSlicer = Period.ofDays(1)
                )
            )
        if (response != null) {
            val stepsData = mutableListOf<DataRecord>()
            response.sortedBy { it.startTime }
            var trackTime = startTime.toLocalDate().atStartOfDay()
            for (dailyResult in response) {
                //Checking before every record if there are any days that are missed out and adding 0 value for all those days
                if (dailyResult.startTime.isAfter(trackTime)) {
                    while (trackTime.isBefore(dailyResult.startTime)) {
                        stepsData.add(
                            DataRecord(
                                metricValue = "0",
                                dataType = DataType.STEPS,
                                toDatetime = trackTime.toLocalDate().atTime(LocalTime.MAX)
                                    .atZone(ZoneId.systemDefault()).format(
                                        dateTimeFormatter
                                    ),
                                fromDatetime = if (trackTime.toLocalDate()
                                        .isEqual(startTime.toLocalDate())
                                ) startTime.format(dateTimeFormatter) else trackTime.atZone(ZoneId.systemDefault())
                                    .format(dateTimeFormatter)
                            )
                        )
                        trackTime = trackTime.plusDays(1)
                    }
                }
                val totalSteps = dailyResult.result[StepsRecord.COUNT_TOTAL]
                stepsData.add(
                    DataRecord(
                        metricValue = (totalSteps ?: 0).toString(),
                        dataType = DataType.STEPS,
                        toDatetime = dailyResult.endTime.atZone(ZoneId.systemDefault())
                            .minusSeconds(1)
                            .format(dateTimeFormatter),
                        fromDatetime = if (dailyResult.startTime.toLocalDate()
                                .isEqual(startTime.toLocalDate())
                        ) startTime.format(
                            dateTimeFormatter
                        ) else dailyResult.startTime.atZone(ZoneId.systemDefault())
                            .format(dateTimeFormatter)
                    )
                )
                trackTime = dailyResult.endTime
            }
            //To add data as 0 for the days the data is not available after all the records and before the end date
            while (trackTime.isBefore(endTime.toLocalDateTime()) && Duration.between(
                    trackTime,
                    endTime
                ).toMinutes() > 1
            ) {
                stepsData.add(
                    DataRecord(
                        metricValue = "0",
                        dataType = DataType.STEPS,
                        toDatetime = if (trackTime.toLocalDate().isEqual(endTime.toLocalDate()))
                            endTime.format(dateTimeFormatter)
                        else trackTime.toLocalDate().atTime(LocalTime.MAX)
                            .atZone(ZoneId.systemDefault())
                            .format(dateTimeFormatter),
                        fromDatetime = if (trackTime.toLocalDate()
                                .isEqual(startTime.toLocalDate())
                        )
                            startTime.format(dateTimeFormatter)
                        else trackTime.atZone(ZoneId.systemDefault()).format(dateTimeFormatter)
                    )
                )
                trackTime = trackTime.plusDays(1).toLocalDate().atStartOfDay()
            }
            Log.d("Data", stepsData.toString())
            return stepsData
        }
        return emptyList()
    }

    //Function that gives total distance in miles for each day for given interval
    suspend fun readDistanceForInterval(interval: Long): List<DataRecord> {
        val startTime: ZonedDateTime =
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(interval - 1)

        val endTime = LocalDateTime.now().atZone(TimeZone.getDefault().toZoneId())
        val response =
            healthConnectClient?.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(DistanceRecord.DISTANCE_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        startTime.toLocalDate().atStartOfDay(),
                        endTime.toLocalDateTime()
                    ),
                    timeRangeSlicer = Period.ofDays(1)
                )
            )
        if (response != null) {
            val distanceData = mutableListOf<DataRecord>()
            response.sortedBy { it.startTime }
            var trackTime = startTime.toLocalDate().atStartOfDay()
            for (dailyResult in response) {
                //Checking before every record if there are any days that are missed out and adding 0 value for all those days
                if (dailyResult.startTime.isAfter(trackTime)) {
                    while (trackTime.isBefore(dailyResult.startTime)) {
                        distanceData.add(
                            DataRecord(
                                metricValue = "0",
                                dataType = DataType.DISTANCE,
                                toDatetime = trackTime.toLocalDate().atTime(LocalTime.MAX)
                                    .atZone(ZoneId.systemDefault()).format(dateTimeFormatter),
                                fromDatetime = if (trackTime.toLocalDate()
                                        .isEqual(startTime.toLocalDate())
                                ) startTime.format(dateTimeFormatter) else trackTime.atZone(ZoneId.systemDefault())
                                    .format(dateTimeFormatter)
                            )
                        )
                        trackTime = trackTime.plusDays(1).toLocalDate().atStartOfDay()
                    }
                }
                val totalDistance =
                    (dailyResult.result[DistanceRecord.DISTANCE_TOTAL]?.inKilometers?.times(1.60934))?.let {
                        String.format(
                            Locale.US, "%.3f", it
                        )
                    }
                distanceData.add(
                    DataRecord(
                        metricValue = (totalDistance ?: 0).toString(),
                        dataType = DataType.DISTANCE,
                        toDatetime = dailyResult.endTime.atZone(ZoneId.systemDefault())
                            .minusSeconds(1)
                            .format(dateTimeFormatter),
                        fromDatetime = if (dailyResult.startTime.toLocalDate()
                                .isEqual(startTime.toLocalDate())
                        ) startTime.format(
                            dateTimeFormatter
                        ) else dailyResult.startTime.atZone(ZoneId.systemDefault())
                            .format(dateTimeFormatter)
                    )
                )
                trackTime = dailyResult.endTime
            }
            //To add data as 0 for the days the data is not available after all the records and before the end date
            while (trackTime.isBefore(endTime.toLocalDateTime()) && Duration.between(
                    trackTime,
                    endTime
                ).toMinutes() > 1
            ) {
                distanceData.add(
                    DataRecord(
                        metricValue = "0",
                        dataType = DataType.DISTANCE,
                        toDatetime = if (trackTime.toLocalDate()
                                .isEqual(endTime.toLocalDate())
                        )
                            endTime.format(dateTimeFormatter)
                        else trackTime.toLocalDate().atTime(LocalTime.MAX)
                            .atZone(ZoneId.systemDefault()).format(dateTimeFormatter),
                        fromDatetime = if (trackTime.toLocalDate()
                                .isEqual(startTime.toLocalDate())
                        )
                            startTime.format(dateTimeFormatter)
                        else trackTime.atZone(ZoneId.systemDefault()).format(dateTimeFormatter)
                    )
                )
                trackTime = trackTime.plusDays(1)
            }
            Log.d("Data", distanceData.toString())
            return distanceData
        }
        return emptyList()
    }

    //Function that gives total active minutes by user for each day in minutes for given interval
    suspend fun readMinsForInterval(interval: Long): List<DataRecord> {
        val startTime: ZonedDateTime =
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(interval - 1)

        val endTime = LocalDateTime.now().atZone(TimeZone.getDefault().toZoneId())
        val response =
            healthConnectClient?.aggregateGroupByPeriod(
                AggregateGroupByPeriodRequest(
                    metrics = setOf(ExerciseSessionRecord.EXERCISE_DURATION_TOTAL),
                    timeRangeFilter = TimeRangeFilter.between(
                        startTime.toLocalDate().atStartOfDay(),
                        endTime.toLocalDateTime()
                    ),
                    timeRangeSlicer = Period.ofDays(1)
                )
            )
        if (response != null) {
            val minutesData = mutableListOf<DataRecord>()
            response.sortedBy { it.startTime }
            var trackTime = startTime.toLocalDate().atStartOfDay()
            for (dailyResult in response) {
                //Checking before every record if there are any days that are missed out and adding 0 value for all those days
                if (dailyResult.startTime.isAfter(trackTime)) {
                    while (trackTime.isBefore(dailyResult.startTime)) {
                        minutesData.add(
                            DataRecord(
                                metricValue = "0",
                                dataType = DataType.MINS,
                                toDatetime = trackTime.toLocalDate().atTime(LocalTime.MAX)
                                    .atZone(ZoneId.systemDefault()).format(dateTimeFormatter),
                                fromDatetime = if (trackTime.toLocalDate()
                                        .isEqual(startTime.toLocalDate())
                                ) startTime.format(dateTimeFormatter) else trackTime.atZone(ZoneId.systemDefault())
                                    .format(dateTimeFormatter)
                            )
                        )
                        trackTime = trackTime.plusDays(1).toLocalDate().atStartOfDay()
                    }
                }
                val totalMins =
                    dailyResult.result[ExerciseSessionRecord.EXERCISE_DURATION_TOTAL]?.toMinutes()
                minutesData.add(
                    DataRecord(
                        metricValue = (totalMins ?: 0).toString(),
                        dataType = DataType.MINS,
                        toDatetime = dailyResult.endTime.atZone(ZoneId.systemDefault())
                            .minusSeconds(1).format(dateTimeFormatter),
                        fromDatetime = if (dailyResult.startTime.toLocalDate()
                                .isEqual(startTime.toLocalDate())
                        ) startTime.format(
                            dateTimeFormatter
                        ) else dailyResult.startTime.atZone(ZoneId.systemDefault())
                            .format(dateTimeFormatter)
                    )
                )
                trackTime = dailyResult.endTime
            }
            //To add data as 0 for the days the data is not available after all the records and before the end date
            while (trackTime.isBefore(endTime.toLocalDateTime()) && Duration.between(
                    trackTime,
                    endTime
                ).toMinutes() > 1
            ) {
                minutesData.add(
                    DataRecord(
                        metricValue = "0",
                        dataType = DataType.MINS,
                        toDatetime = if (trackTime.toLocalDate()
                                .isEqual(endTime.toLocalDate())
                        ) endTime.format(dateTimeFormatter) else trackTime.toLocalDate()
                            .atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())
                            .format(dateTimeFormatter),
                        fromDatetime = if (trackTime.toLocalDate()
                                .isEqual(startTime.toLocalDate())
                        )
                            startTime.format(dateTimeFormatter)
                        else trackTime.atZone(ZoneId.systemDefault()).format(dateTimeFormatter)
                    )
                )
                trackTime = trackTime.plusDays(1)
            }
            Log.d("Data", minutesData.toString())
            return minutesData
        }
        return emptyList()
    }

    //Gives the sleep records available in the interval
    suspend fun readSleepSessionsForInterval(interval: Long): List<DataRecord> {
        val startTime: ZonedDateTime =
            LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(interval - 1)
        val sleepData = mutableListOf<DataRecord>()
        val response =
            healthConnectClient?.readRecords(
                ReadRecordsRequest(
                    SleepSessionRecord::class,
                    timeRangeFilter = TimeRangeFilter.between(
                        startTime.toLocalDateTime(),
                        LocalDateTime.now(ZoneId.systemDefault())
                    )
                )
            )
        if (response != null) {
            if (response.records.isEmpty()) {
                sleepData.add(
                    DataRecord(
                        metricValue = "0",
                        dataType = DataType.SLEEP,
                        toDatetime = startTime.plusSeconds(1).format(dateTimeFormatter),
                        fromDatetime = startTime.format(dateTimeFormatter)
                    )
                )
            } else {
                var start = response.records[0].startTime
                var end = response.records[0].endTime

                for (index in 1 until response.records.size) {
                    if (response.records[index].startTime > end) {
                        sleepData.add(
                            DataRecord(
                                metricValue = Duration.between(start, end).toHours()
                                    .toString(),
                                dataType = DataType.SLEEP,
                                toDatetime = end.atZone(ZoneId.systemDefault())
                                    .format(dateTimeFormatter),
                                fromDatetime = start.atZone(ZoneId.systemDefault())
                                    .format(dateTimeFormatter)
                            )
                        )
                        start = response.records[index].startTime
                        end = response.records[index].endTime
                    } else if (response.records[index].endTime >= end) {
                        end = response.records[index].endTime
                    }
                }
                sleepData.add(
                    DataRecord(
                        metricValue = Duration.between(start, end).toHours().toString(),
                        dataType = DataType.SLEEP,
                        toDatetime = end.atZone(ZoneId.systemDefault())
                            .format(dateTimeFormatter),
                        fromDatetime = start.atZone(ZoneId.systemDefault())
                            .format(dateTimeFormatter)
                    )
                )

            }
        }
        Log.d("data", sleepData.toString())
        return sleepData
    }
}