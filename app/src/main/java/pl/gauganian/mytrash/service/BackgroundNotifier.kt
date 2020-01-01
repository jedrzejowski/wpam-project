package pl.gauganian.mytrash.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.util.Log
import pl.gauganian.mytrash.AppSettings

// Dobry tutorial
// https://stackoverflow.com/questions/35578586/background-process-timer-on-android

class BackgroundNotifier : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        //Do something every 30 seconds
        Log.e("MY TAG", "Emit")
    }



    companion object {

        fun start(context: Context) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val i = Intent(context, BackgroundNotifier::class.java)
            val pi = PendingIntent.getBroadcast(context, 0, i, 0)
            am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                (1000 * 60 * 1).toLong(),
                pi
            )
            Log.e("MY TAG", "REGISTER")
        }

        fun stop(context: Context) {
            val intent = Intent(context, BackgroundNotifier::class.java)
            val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(sender)
        }

        fun syncWithSettings(settings: AppSettings) {
            if (settings.backgroundSync)
                start(settings.app)
            else stop(settings.app)
        }
    }
}