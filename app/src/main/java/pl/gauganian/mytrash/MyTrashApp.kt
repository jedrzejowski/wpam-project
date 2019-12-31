package pl.gauganian.mytrash

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import pl.gauganian.mytrash.data.TrashAddressPoint

class MyTrashApp : Application() {

    val trashAddressPoints = MutableLiveData<ArrayList<TrashAddressPoint>>()

    init {
        trashAddressPoints.value = ArrayList<TrashAddressPoint>().apply {
            //            add(TrashAddressPoint("51974388", "Bogatki"))
//            add(TrashAddressPoint("52002438", "Jurajska"))
        }

        trashAddressPoints.observeForever { saveAppData() }

        readAppData()
    }

    private fun readAppData() {}

    private fun saveAppData() {}

    companion object {
        fun get(c: Context): MyTrashApp {
            return c.applicationContext as MyTrashApp
        }
    }
}