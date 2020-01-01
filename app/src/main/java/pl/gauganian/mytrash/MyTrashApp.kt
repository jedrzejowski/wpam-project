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

class MyTrashApp : Application() {

    val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    val trashAddressPoints: MutableLiveData<ArrayList<TrashAddressPoint>> by lazy {
        val liveData = MutableLiveData<ArrayList<TrashAddressPoint>>()

        if (sharedPreferences.contains(PREF_NAME_TRASHADDRESSPOINTS)) {
            liveData.value = jsonObjectArrayParse(
                sharedPreferences.getString(PREF_NAME_TRASHADDRESSPOINTS, "[]") ?: "[]"
            ) { obj -> TrashAddressPoint(obj) }
        }

        Log.e("MY TAG", "init")

        liveData.observeForever {

            sharedPreferences.edit().apply {
                putString(
                    PREF_NAME_TRASHADDRESSPOINTS,
                    jsonObjectArrayDump(liveData.value) { tap -> tap.toJSON() }.toString()
                )
                apply()
            }
        }

        return@lazy liveData
    }

    companion object {
        private const val PREF_NAME_TRASHADDRESSPOINTS = "trashAddressPoints"

        fun get(c: Context): MyTrashApp {
            return c.applicationContext as MyTrashApp
        }
    }
}