package com.pklproject.checkincheckout.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.api.models.Setting
import com.pklproject.checkincheckout.ui.settings.TinyDB

class NotificationReceiver : BroadcastReceiver() {

    val CHANNEL_ID = "channelId"
    val CHANNEL_NAME = "channelName"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {
            val i = Intent(context, MainActivity::class.java)
            val waktuAbsenPagi = TinyDB(context).getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenPagiAkhir
            val waktuAbsenSiang = TinyDB(context).getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenSiangAkhir
            val waktuAbsenPulang = TinyDB(context).getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenPulangAkhir

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntentPagi = PendingIntent.getActivity(context, 0, i, 0)
            val pendingIntentSiang = PendingIntent.getActivity(context, 1, i, 0)
            val pendingIntentPulang = PendingIntent.getActivity(context, 2, i, 0)

            val alarm = NotificationCompat.Builder(context!!, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Absen Pagi Menunggumu")
                .setContentText("Segera absen sebelum $waktuAbsenPagi")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntentPagi)
                .build()

            val alarmSiang = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Absen Siang Menunggumu")
                .setContentText("Segera absen sebelum $waktuAbsenSiang")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntentSiang)
                .build()
//
            val absenPulang = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Absen Pulang Menunggumu")
                .setContentText("Segera absen sebelum $waktuAbsenPulang")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntentPulang)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, alarm)
            notificationManager.notify(2, alarmSiang)
            notificationManager.notify(3, absenPulang)
        }
    }
}