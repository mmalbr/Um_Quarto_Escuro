package com.meafocus.adarkworld
/**
 * Created by Md Imran Choudhury on 10/Aug/2018.
 * All rights received InfixSoft
 * Contact email: imrankst1221@gmail.com
 */

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.onesignal.OSNotification
import com.onesignal.OneSignal
import com.meafocus.adarkworld.setting.OneSignalHandler
import infix.imrankst1221.rocket.library.setting.AppDataInstance
import infix.imrankst1221.rocket.library.utility.PreferenceUtils


class ApplicationClass: Application() {
    val TAG = "---ApplicationClass"
    lateinit var mContext: Context

    override fun onCreate() {
        super.onCreate()
        mContext = this

        initConfig()
        applyTheme()
    }

    private fun initConfig(){
        AppDataInstance.getINSTANCE(mContext)
        PreferenceUtils.getInstance().initPreferences(mContext)
        //CustomActivityOnCrash.install(this)

        /*FirebaseApp.initializeApp(mContext)
        val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build()
        FirebaseFirestore.getInstance().firestoreSettings = settings*/

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(OneSignalHandler(mContext))
                .autoPromptLocation(false) // true will be auto show location access when app run
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init()

        OneSignal.idsAvailable { userId, registrationId -> Log.d(TAG, ""+userId) }
    }

    private fun applyTheme(){
        /**
         * Turn on/off dark mode
         * Flag for off: AppCompatDelegate.MODE_NIGHT_NO
         * Flag for on: AppCompatDelegate.MODE_NIGHT_YES
         */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        /**
         * Disable taking screenshot
         */
        //window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
    }

    private inner class NotificationReceivedHandler : OneSignal.NotificationReceivedHandler {
        override fun notificationReceived(notification: OSNotification) {
            // receive a notification
        }
    }
}