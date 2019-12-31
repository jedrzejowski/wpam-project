package pl.gauganian.mytrash.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.DataProvider
import pl.gauganian.mytrash.data.DataProviderException
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.data.TrashSchedule
import pl.gauganian.mytrash.helper.ErrorSink
import pl.gauganian.mytrash.helper.doAsync

class TrashSchedulePagerDataModel : ViewModel() {

    var addressPoint = MutableLiveData<TrashAddressPoint?>()
    var schedule = MutableLiveData<TrashSchedule?>()

    var errorSink: ErrorSink? = null

    fun loadSchedule() {
        doAsync {
            try {
                val id = addressPoint.value?.id
                if (id == null) return@doAsync

                val jarray = DataProvider.downloadSchedule(id)

                schedule.postValue(TrashSchedule(jarray.getJSONObject(0)))
            } catch (e: Exception) {
                errorSink?.handleErrorSink(
                    TrashSchedulePagerDataModel::class.java.name,
                    R.string.error_dataprovider,
                    e
                )
            }

        }.execute()
    }
}