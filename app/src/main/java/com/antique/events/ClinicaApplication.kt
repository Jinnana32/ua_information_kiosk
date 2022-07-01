package com.antique.events

import android.app.Application
import android.content.ContextWrapper
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pixplicity.easyprefs.library.Prefs

class ClinicaApplication : Application() {

    private val FIREBASE_INSTANCE_TAG = "FIREBASE_INSTANCE_TAG"

    override fun onCreate() {
        super.onCreate()
        // Init shared prefs
        this.initSharedPrefs()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(FIREBASE_INSTANCE_TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.w("FCM token", token);
        })
    }

    private fun initSharedPrefs() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}