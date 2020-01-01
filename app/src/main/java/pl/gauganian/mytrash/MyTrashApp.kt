package pl.gauganian.mytrash

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import androidx.lifecycle.MutableLiveData
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.helper.jsonObjectArrayDump
import pl.gauganian.mytrash.helper.jsonObjectArrayParse
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import pl.gauganian.mytrash.service.BackgroundNotifier


class MyTrashApp : Application() {

    val settings: AppSettings by lazy { AppSettings(this) }


    val trashAddressPoints: MutableLiveData<ArrayList<TrashAddressPoint>> by lazy {
        val liveData = MutableLiveData<ArrayList<TrashAddressPoint>>()

        liveData.value = settings.trashAddressPoints

        liveData.observeForever {
            settings.trashAddressPoints = it
        }

        return@lazy liveData
    }


    override fun onCreate() {
        super.onCreate()
        settings
    }


    companion object {

        fun get(c: Context): MyTrashApp {
            return c.applicationContext as MyTrashApp
        }
    }
}