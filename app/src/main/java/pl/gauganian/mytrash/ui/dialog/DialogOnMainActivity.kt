package pl.gauganian.mytrash.ui.dialog

import android.content.Context
import androidx.fragment.app.DialogFragment
import pl.gauganian.mytrash.MainActivity
import pl.gauganian.mytrash.data.TrashAddressPoint

abstract class DialogOnMainActivity : DialogFragment() {

    protected lateinit var mainActivity: MainActivity

    protected lateinit var trashAddressPoint: TrashAddressPoint
    protected var trashAddressPointIndex: Int = -1

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mainActivity = context as MainActivity
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must MainActivity")
        }

        trashAddressPoint = mainActivity.getThrashAddressPoint()
            ?: throw ClassCastException("$context has no ThrashAddressPoint selected")
        trashAddressPointIndex = mainActivity.getThrashAddressPointIndex()
    }
}