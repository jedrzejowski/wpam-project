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
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.DataProvider
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.data.TrashFraction
import pl.gauganian.mytrash.helper.doAsync
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar


// Dobry tutorial
// https://stackoverflow.com/questions/35578586/background-process-timer-on-android

class BackgroundNotifier : BroadcastReceiver() {

    lateinit var context: Context
    lateinit var appSettings: AppSettings
    var extraDays = 0

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("BackgroundNotifier", "onReceive")

        this.context = context
        appSettings = AppSettings(context)

        doAsync {
            try {
                extraDays = intent.getIntExtra(ARG_EXTRADAYS, 0)
                syncData(context)
            } catch (e: Exception) {
                Log.e("BackgroundNotifier", e.toString())
            }
        }.execute()
    }

    private fun syncData(context: Context) {
        val trashAddressPoints = appSettings.trashAddressPoints

        val datePattern = DateTimeFormatter.ofPattern("u-MM-d")
        val theDay = LocalDate.now().plus(extraDays.toLong(), ChronoUnit.DAYS).format(datePattern)

        val list = ArrayList<NotificationLine>()

        for (trashAddressPoint in trashAddressPoints) {
            val schedule = DataProvider.downloadSchedule(trashAddressPoint.id)

            val fractions = ArrayList<TrashFraction>()

            fractions@ for (item in schedule.items) {
                if (item.date?.format(datePattern) == theDay)
                    fractions.add(item.fraction ?: continue@fractions)
            }

            list.add(NotificationLine(trashAddressPoint, fractions))
        }

        notifyUser(list, context)
    }

    private fun notifyUser(list: ArrayList<NotificationLine>, context: Context) {

        createNotificationChannel()

        val linePrefix = context.getString(R.string.notification_channel1_line_prefix)
        val lineSeparator = context.getString(R.string.notification_channel1_line_separator)
        val lineSuffix = context.getString(R.string.notification_channel1_line_suffix)

        val nText = list
            .filter { line -> line.fractions.size > 0 }
            .joinToString("") { line ->
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

        if (nText.isEmpty()) return

        val builder = NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_stat_mytrash)
            setContentTitle(
                context.getString(
                    if (extraDays == 0) R.string.notification_channel1_title_morning
                    else R.string.notification_channel1_title_evening
                )
            )
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
        private const val ARG_EXTRADAYS = "extra_days"

        private fun morningIntent(context: Context): PendingIntent {
            val i = Intent(context, BackgroundNotifier::class.java)
            i.putExtra(ARG_EXTRADAYS, 0)
            return PendingIntent.getBroadcast(context, 0, i, 0)
        }

        private fun eveningIntent(context: Context): PendingIntent {
            val i = Intent(context, BackgroundNotifier::class.java)
            i.putExtra(ARG_EXTRADAYS, 1)
            return PendingIntent.getBroadcast(context, 0, i, 0)
        }

        fun start(context: Context) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            runIntentEveryDayAt(am, morningIntent(context), 7, 0)
            runIntentEveryDayAt(am, eveningIntent(context), 17, 0)
        }

        private fun runIntentEveryDayAt(
            am: AlarmManager,
            pi: PendingIntent,
            hour: Int,
            minute: Int
        ) {
            val now = GregorianCalendar()

            am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                GregorianCalendar().let {
                    it.set(Calendar.HOUR, hour)
                    it.set(Calendar.MINUTE, minute)
                    it.set(Calendar.SECOND, 0)
                    it.set(Calendar.MILLISECONDS_IN_DAY, 0)

                    if (it.time < now.time)
                        it.add(Calendar.HOUR, 24)

                    it.timeInMillis
                },
                (24 * 60 * 60 * 1000).toLong(),
                pi
            )
        }

        fun stop(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(morningIntent(context))
            alarmManager.cancel(eveningIntent(context))
        }

        fun syncWithSettings(settings: AppSettings) {
            if (settings.backgroundSync)
                start(settings.context)
            else stop(settings.context)
        }
    }
}