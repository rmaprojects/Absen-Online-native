package com.pklproject.checkincheckout.notif

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.pklproject.checkincheckout.R
import com.pklproject.checkincheckout.databinding.FragmentNotificationBinding
import java.util.*

class NotifikasiFragment : Fragment(R.layout.fragment_notification){
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var picker : MaterialTimePicker
    private lateinit var calendar: Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.inflate(layoutInflater)

        createNotificationchannel()
        alarmManager = requireContext().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)


        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()

            set(Calendar.HOUR_OF_DAY, 3)
            set(Calendar.MINUTE, 56)
        }

        // setRepeating() lets you specify a precise custom interval--in this case,
        // 20 minutes.
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 20,
            pendingIntent
        )
        binding.selecttimebtn.setOnClickListener {

           // showTimePicker()
        }

        binding.settimebtn.setOnClickListener {

           // setAlarm()
        }

        binding.cancelAlarmbtn.setOnClickListener {

           // cancelAlarm()
        }
    }
//    private fun cancelAlarm() {
//        alarmManager = requireContext().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(requireContext(), AlarmReceiver::class.java)
//
//        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
//
//        alarmManager.cancel(pendingIntent)
//        Toast.makeText(requireContext(), "Alarm cancel", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun setAlarm() {
//        alarmManager = requireContext().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(requireContext(), AlarmReceiver::class.java)
//
//        pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,0)
//
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
//            AlarmManager.INTERVAL_DAY,pendingIntent
//        )
//
//        Toast.makeText(requireContext(), "Alarm set Successfuly", Toast.LENGTH_SHORT).show()
//    }

//    private fun showTimePicker() {
//
//        picker = MaterialTimePicker.Builder()
//            .setTimeFormat(TimeFormat.CLOCK_12H)
//            .setHour(12)
//            .setMinute(0)
//            .setTitleText("Select Alarm Time")
//            .build()
//
//        picker.addOnPositiveButtonClickListener {
//
//            if (picker.hour > 12){
//                binding.selectedTime.text =
//                    String.format("%02d",picker.hour - 12)+ ":" + String.format(
//                        "%02d",
//                        picker.minute
//                    ) + "PM"
//            }else {
//                String.format("%02d",picker.hour)+ ":" + String.format(
//                    "%02d",
//                    picker.minute
//                ) + "AM"
//            }
//            calendar = Calendar.getInstance()
//            calendar[Calendar.HOUR_OF_DAY] = 9
//            calendar[Calendar.MINUTE] = 30
//            calendar[Calendar.SECOND] = 0
//            calendar[Calendar.MILLISECOND] = 0
//
//        }
//    }

    private fun setRepeating(){

    }
    private fun createNotificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name : CharSequence = "PT OMG! INDONESIA"
            val description = "OMG"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("PT OMG",name,importance)
            channel.description = description
            val NotificationManager = requireContext().getSystemService(
                NotificationManager::class.java
            )

            NotificationManager.createNotificationChannel(channel)
        }
    }

}