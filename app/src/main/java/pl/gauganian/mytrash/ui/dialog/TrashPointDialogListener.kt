package pl.gauganian.mytrash.ui.dialog

import pl.gauganian.mytrash.data.TrashAddressPoint

interface TrashPointDialogListener {
    fun getThrashAddressPointIndex(): Int
    fun getThrashAddressPoint(): TrashAddressPoint?
    fun onReloadRequest()
}