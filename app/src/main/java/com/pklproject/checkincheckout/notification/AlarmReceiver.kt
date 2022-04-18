package com.pklproject.checkincheckout.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.pklproject.checkincheckout.R

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var tipeAbsen = ""
        val nomorAbsen = intent?.getIntExtra("tipeAbsen", 0)

        when (nomorAbsen) {
            0 -> {
                tipeAbsen = "Pagi"
            }
            1 -> {
                tipeAbsen = "Siang"
            }
            2 -> {
                tipeAbsen = "Pulang"
            }
        }

        val pendingIntent = PendingIntent.getActivity(context, nomorAbsen!!.toInt(), intent, 0)

        val builder = NotificationCompat.Builder(context!!, NotificationWorker.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentTitle("Sudah melakukan $tipeAbsen?")
            .setContentText("Segera absen sebelum waktu habis")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NotificationWorker.CHANNEL_ID)
            val channel = NotificationChannel(NotificationWorker.CHANNEL_ID, "Pengingat Absen", NotificationManager.IMPORTANCE_DEFAULT)
            channel.lightColor = Color.RED
            channel.description = "Pengingat absen untuk aplikasi Check In Check Out"
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with (NotificationManagerCompat.from(context)) {
            notify(NotificationWorker.NOTIFICATION_ID, builder.build())
        }
    }
}