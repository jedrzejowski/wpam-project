package pl.gauganian.mytrash.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import pl.gauganian.mytrash.data.TrashScheduleDownloader

class TrashSchedulePagerDataModel : ViewModel() {

    init {
        Log.e("MY TAG", "WOlolo")
    }

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

//    private var bgTask: TrashScheduleDownloader? = null

    fun downloadSchedule() {
        Log.d("MY TAG", "staring")
        var bgTask = TrashScheduleDownloader()
        bgTask.execute("")
    }

    fun needDownloadTrashSchedule(): Boolean {
        return false
    }

    fun isDownloadingTrashShedule(): Boolean {
        return false
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}