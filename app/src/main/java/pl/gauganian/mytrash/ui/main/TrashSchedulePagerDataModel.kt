package pl.gauganian.mytrash.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.gauganian.mytrash.data.DataProvider
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.data.TrashSchedule
import pl.gauganian.mytrash.helper.doAsync

class TrashSchedulePagerDataModel : ViewModel() {

    var addressPoint = MutableLiveData<TrashAddressPoint?>()

    var schedule = MutableLiveData<TrashSchedule?>()

    fun loadSchedule() {
        doAsync {
            val id = addressPoint.value?.id
            if (id == null) return@doAsync

            val jarray = DataProvider.downloadSchedule(id)

            schedule.postValue(TrashSchedule(jarray.getJSONObject(0)))

        }.execute()
    }
}