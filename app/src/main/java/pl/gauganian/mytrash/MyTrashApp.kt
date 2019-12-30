package pl.gauganian.mytrash

import android.app.Application
import pl.gauganian.mytrash.data.TrashAddressPoint

class MyTrashApp : Application() {
    init {

    }

    fun getAddressPoints(): ArrayList<TrashAddressPoint> {
        return arrayListOf(
            TrashAddressPoint("52002438", "Bogatki"),
            TrashAddressPoint("51974388", "Jurajska")
        )
    }
}