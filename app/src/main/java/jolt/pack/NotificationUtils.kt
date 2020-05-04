package jolt.pack

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationUtils {
    @RequiresApi(Build.VERSION_CODES.N)
    fun setNotification(timeInMiliSeconds: Long, activity: Activity) {

        if (timeInMiliSeconds > 0) {
            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(activity.applicationContext, AlarmReceiver::class.java)

            alarmIntent.putExtra("reason", "notification")
            alarmIntent.putExtra("timestamp", timeInMiliSeconds)

            val calendar = android.icu.util.Calendar.getInstance()
            calendar.timeInMillis = timeInMiliSeconds

            val pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        }
    }

}