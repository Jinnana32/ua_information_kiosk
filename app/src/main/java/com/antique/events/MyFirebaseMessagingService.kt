package com.antique.events

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.antique.events.utils.PushNotifUtil.sendNotification

class MyFirebaseMessagingService() : FirebaseMessagingService() {

    private val MY_FIREBASE_SERVICE_TAG = "MyFirebaseService"
    private val NEW_TOKEN_TAG = "NEW_TOKEN_TAG"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(MY_FIREBASE_SERVICE_TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().isNotEmpty()) {
            Log.d(MY_FIREBASE_SERVICE_TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(MY_FIREBASE_SERVICE_TAG, "Message Notification Body: " + remoteMessage.notification!!.body);
            val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
            notificationManager.sendNotification(messageBody = remoteMessage.notification!!.body!!, title = remoteMessage.notification!!.title!!, applicationContext = applicationContext);
        }
    }

    override fun onNewToken(newToken: String) {
        Log.e(NEW_TOKEN_TAG, "New token: $newToken");
        super.onNewToken(newToken)
    }
}