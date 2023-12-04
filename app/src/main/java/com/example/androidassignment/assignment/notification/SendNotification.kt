package com.example.androidassignment.assignment.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.androidassignment.R
import com.example.androidassignment.assignment.AssignmentApp
import com.example.androidassignment.assignment.constant.Constant
import java.util.Timer

open class SendNotification(private val context: Context) {
    fun sendWeatherNotificationToUser(data: String) {

        /*We need to create notification channel for device above Android version 8*/
        createNotificationChannel()

        val intent = Intent(context, AssignmentApp::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, Constant.CHANNEL_ID).apply {
            setCategory(NotificationCompat.CATEGORY_MESSAGE)
            setAutoCancel(false)
            setOngoing(true)
            setSmallIcon(R.drawable.assignment)
            setContentTitle("Today's Weather \n")
            setContentText(data)
            setContentIntent(pendingIntent)
            color = Color.BLUE

        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, notificationBuilder.build())

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constant.CHANNEL_ID,
                Constant.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = context.getString(R.string.weather_notifications)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    canBubble()
                    enableLights(true)
                }
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

        }

    }

}