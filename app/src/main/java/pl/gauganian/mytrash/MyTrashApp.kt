package pl.gauganian.mytrash

import android.app.Application
import android.content.Context
import pl.gauganian.mytrash.data.TrashAddressPoint

class MyTrashApp : Application() {
    init {

    }

    fun getAddressPoints(): ArrayList<TrashAddressPoint> {
        return arrayListOf(
            TrashAddressPoint("51974388", "Bogatki"),
            TrashAddressPoint("52002438", "Jurajska")
        )
    }

    companion object {
        fun get(c: Context): MyTrashApp {
            return c.applicationContext as MyTrashApp
        }
    }
}