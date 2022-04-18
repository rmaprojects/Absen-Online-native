package com.pklproject.checkincheckout.notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.pklproject.checkincheckout.MainActivity
import com.pklproject.checkincheckout.R
import java.util.*

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {

        val jam = inputData.getString("jam")
        val tipeAbsen = inputData.getInt("tipeAbsen", 0)

        showNotification(jam, tipeAbsen)
        Log.d("NotificationWorker", "Notification sent")

        return Result.success()
    }

    private fun showNotification(jam:String?, tipeAbsen:Int?) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val tahunIni = Calendar.getInstance().get(Calendar.YEAR)
        val bulanIni = Calendar.getInstance().get(Calendar.MONTH)
        val hariIni = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        when (tipeAbsen) {
            0 -> {
                val jamPagiAkhir = jam?.split(":")?.get(0)?.toInt()
                val menitPagiAkhir = jam?.split(":")?.get(1)?.toInt()

                val jamPagi = Calendar.getInstance()
                jamPagi.set(tahunIni, bulanIni, hariIni, jamPagiAkhir!!, menitPagiAkhir!!)

                val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intentAlarm = Intent(applicationContext, AlarmReceiver::class.java)
                intentAlarm.putExtra("tipeAbsen", 0.toInt())
                val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intentAlarm, 0)

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, jamPagi.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
            1 -> {
                val jamSiangAkhir = jam?.split(":")?.get(0)?.toInt()
                val menitSiangAkhir = jam?.split(":")?.get(1)?.toInt()

                val jamSiang = Calendar.getInstance()
                jamSiang.set(tahunIni, bulanIni, hariIni, jamSiangAkhir!!, menitSiangAkhir!!)

                val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intentAlarm = Intent(applicationContext, AlarmReceiver::class.java)
                intentAlarm.putExtra("tipeAbsen", 1.toInt())
                val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intentAlarm, 0)

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, jamSiang.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
            2 -> {
                val jamPulangAkhir = jam?.split(":")?.get(0)?.toInt()
                val menitPulangAkhir = jam?.split(":")?.get(1)?.toInt()

                val jamPulang = Calendar.getInstance()
                jamPulang.set(tahunIni, bulanIni, hariIni, jamPulangAkhir!!, menitPulangAkhir!!)

                val alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                val intentAlarm = Intent(applicationContext, AlarmReceiver::class.java)
                intentAlarm.putExtra("tipeAbsen", 2.toInt())
                val pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, intentAlarm, 0)

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, jamPulang.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
            }
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "notificationId"
    }
}