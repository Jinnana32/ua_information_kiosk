package com.antique.events.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.antique.events.R
import com.antique.events.ui.dashboard.MainActivity

object PushNotifUtil {

    // Notification ID.
    private const val NOTIFICATION_ID = 0

    fun NotificationManager.sendNotification(messageBody: String, title: String, applicationContext: Context) {


        // TODO: Step 1.11 create intent
        val contentIntent = Intent(applicationContext, MainActivity::class.java)

        // TODO: Step 1.12 create PendingIntent
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // TODO: Step 1.2 get an instance of NotificationCompat.Builder

        // Build the notification
        val builder = NotificationCompat.Builder(
            applicationContext,
            // TODO: Step 1.8 use a notification channel
            applicationContext.getString(R.string.firebase_notification_channel)
        )
            // TODO: Step 1.3 set title, text and icon to builder
            .setSmallIcon(R.drawable.clinica_logo)
            .setContentTitle(title)
            .setContentText(messageBody)
            // TODO: Step 1.13 set content intent
            .setContentIntent(contentPendingIntent)

            // TODO: Step 2.5 set priority
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // TODO Step 1.4 call notify
        // Deliver the notification
        notify(NOTIFICATION_ID, builder.build())
    }


    fun NotificationManager.cancelNotifications() {
        cancelAll()
    }

}