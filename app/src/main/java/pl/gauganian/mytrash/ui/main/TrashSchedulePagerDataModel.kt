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
    enum class State {
        Initial,
        Loading,
        Ready,
        Error
    }

    var state = MutableLiveData<State>(State.Initial)

    var addressPoint = MutableLiveData<TrashAddressPoint?>()
    var schedule = MutableLiveData<TrashSchedule?>()

    var errorSink: ErrorSink? = null

    fun loadSchedule() {
        doAsync {
            try {
                state.postValue(State.Loading)

                val id = addressPoint.value?.id

                if (id == null) {
                    state.postValue(State.Error)
                    return@doAsync
                }

                val jarray = DataProvider.downloadSchedule(id)

                schedule.postValue(TrashSchedule(jarray.getJSONObject(0)))
                state.postValue(State.Ready)

            } catch (e: Exception) {

                state.postValue(State.Error)
                errorSink?.handleErrorSink(
                    TrashSchedulePagerDataModel::class.java.name,
                    R.string.error_dataprovider,
                    e
                )
            }

        }.execute()
    }
}