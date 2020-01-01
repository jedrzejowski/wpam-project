package pl.gauganian.mytrash

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.helper.jsonObjectArrayDump
import pl.gauganian.mytrash.helper.jsonObjectArrayParse
import pl.gauganian.mytrash.service.BackgroundNotifier

class AppSettings(val app: MyTrashApp) : SharedPreferences.OnSharedPreferenceChangeListener {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)

    init {
        sharedPreferences.edit().apply {
            if (!sharedPreferences.contains(PREF_NAME_BACKGROUNDSYNC))
                putBoolean(PREF_NAME_BACKGROUNDSYNC, true)

            apply()
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        BackgroundNotifier.syncWithSettings(this)
    }

    var trashAddressPoints: ArrayList<TrashAddressPoint>
        get() {
            return jsonObjectArrayParse(
                sharedPreferences.getString(
                    PREF_NAME_TRASHADDRESSPOINTS,
                    "[]"
                ) ?: "[]"
            )
            { obj -> TrashAddressPoint(obj) }
        }
        set(value) {
            sharedPreferences.edit().apply {
                putString(
                    PREF_NAME_TRASHADDRESSPOINTS,
                    jsonObjectArrayDump(value) { tap -> tap.toJSON() }.toString()
                )
                apply()
            }
        }

    var backgroundSync: Boolean
        get() {
            return sharedPreferences.getBoolean(PREF_NAME_BACKGROUNDSYNC, true)
        }
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(PREF_NAME_BACKGROUNDSYNC, value)
                apply()
            }
        }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            PREF_NAME_BACKGROUNDSYNC -> BackgroundNotifier.syncWithSettings(this)
        }
    }

    companion object {
        const val PREF_NAME_TRASHADDRESSPOINTS = "trashAddressPoints"
        const val PREF_NAME_BACKGROUNDSYNC = "background_notifier"
    }
}