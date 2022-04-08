package com.pklproject.checkincheckout.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.api.models.Setting
import com.pklproject.checkincheckout.ui.settings.TinyDB
import java.util.*

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        createNotificationChannel(context!!)
        if (intent!!.action == "android.intent.action.BOOT_COMPLETED") {
            setAlarm(context)
        }
    }

    private fun createNotificationChannel(context: Context) {
        //Create notification channel for android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NotificationReceiver().CHANNEL_ID, NotificationReceiver().CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
                lightColor = Color.RED
                enableLights(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAlarm(context: Context) {
        val tinyDB = TinyDB(context)
        val waktuAbsenPagiAkhir =
            tinyDB.getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenPagiAkhir
        val waktuAbsenSiangAkhir =
            tinyDB.getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenSiangAkhir
        val waktuAbsenPulangAkhir =
            tinyDB.getObject(MainActivity.PENGATURANABSENKEY, Setting::class.java).absenPulangAkhir

        triggerSetAlarmPagi(context, waktuAbsenPagiAkhir)
        triggerSetAlarmSiang(context, waktuAbsenSiangAkhir)
        triggerSetAlarmPulang(context, waktuAbsenPulangAkhir)
    }

    private fun triggerSetAlarmPagi(context: Context, scheduledTime: String) {

        val hour = scheduledTime.split(":")[0].toInt()
        val minute = scheduledTime.split(":")[1].toInt()

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute - 5
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).putExtra("id", 1)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun triggerSetAlarmSiang(context: Context, scheduledTime: String) {
        val hour = scheduledTime.split(":")[0].toInt()
        val minute = scheduledTime.split(":")[1].toInt()

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute - 5
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).putExtra("id", 2)
        val pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun triggerSetAlarmPulang(context: Context, scheduledTime: String) {
        val hour = scheduledTime.split(":")[0].toInt()
        val minute = scheduledTime.split(":")[1].toInt()

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute - 5
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).putExtra("id", 3)
        val pendingIntent = PendingIntent.getBroadcast(context, 2, intent, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}