package pl.gauganian.mytrash.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import pl.gauganian.mytrash.AppSettings
import pl.gauganian.mytrash.MyTrashApp
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.DataProvider
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.data.TrashFraction
import pl.gauganian.mytrash.helper.doAsync
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


// Dobry tutorial
// https://stackoverflow.com/questions/35578586/background-process-timer-on-android

class BackgroundNotifier : BroadcastReceiver() {

    lateinit var context: Context
    lateinit var appSettings: AppSettings

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("BackgroundNotifier", "onReceive")

        this.context = context
        appSettings = AppSettings(context)

        doAsync {
            try {
                syncData(context)
            } catch (e: Exception) {
                Log.e("MY TAG", e.toString())
            }
        }.execute()
    }

    private fun syncData(context: Context) {
        val trashAddressPoints = appSettings.trashAddressPoints

        val datePattern = DateTimeFormatter.ofPattern("u-MM-d")
        val tomorrow = LocalDate.now().plus(1 + 5, ChronoUnit.DAYS).format(datePattern)

        val list = ArrayList<NotificationLine>()

        for (trashAddressPoint in trashAddressPoints) {
            val schedule = DataProvider.downloadSchedule(trashAddressPoint.id)

            val fractions = ArrayList<TrashFraction>()

            fractions@ for (item in schedule.items) {
                if (item.date?.format(datePattern) == tomorrow)
                    fractions.add(item.fraction ?: continue@fractions)
            }

            list.add(NotificationLine(trashAddressPoint, fractions))
        }

        notifyUser(list, context)
    }

    private fun notifyUser(list: ArrayList<NotificationLine>, context: Context) {
        if (list.size == 0) return

        Log.e("SIZE", "" + list.size + "")

        createNotificationChannel()

        val linePrefix = context.getString(R.string.notification_channel1_line_prefix)
        val lineSeparator = context.getString(R.string.notification_channel1_line_separator)
        val lineSuffix = context.getString(R.string.notification_channel1_line_suffix)

        val nText = list.joinToString("") { line ->
            line.fractions
                .map { trashFraction -> context.getString(trashFraction.name) }
                .joinToString(
                    lineSeparator,
                    linePrefix,
                    lineSuffix.format(
                        line.trashAddressPoint.customName
                    )
                )
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_stat_mytrash)
            setContentTitle(context.getString(R.string.notification_channel1_title))
            setStyle(NotificationCompat.BigTextStyle().bigText(nText))
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.notification_channel1_name)
            val descriptionText = context.getString(R.string.notification_channel1_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    internal data class NotificationLine(
        val trashAddressPoint: TrashAddressPoint,
        val fractions: ArrayList<TrashFraction>
    )

    companion object {

        private const val CHANNEL_ID = "channel1"



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
                start(settings.context)
            else stop(settings.context)
        }
    }
}