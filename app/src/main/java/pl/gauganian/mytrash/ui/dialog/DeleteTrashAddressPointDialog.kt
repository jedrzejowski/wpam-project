package pl.gauganian.mytrash.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import pl.gauganian.mytrash.MyTrashApp
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.helper.notifyObserver


class DeleteTrashAddressPointDialog : DialogOnMainActivity() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.apply {
                setMessage(R.string.dialog_delete_title)
                setNeutralButton(R.string.dialog_delete_delete, deleteHandler)
                setNegativeButton(R.string.dialog_delete_cancel, cancelFun)
            }

            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private val deleteHandler = DialogInterface.OnClickListener { dialog, id ->
        val trashAddressPoints = (context?.applicationContext as MyTrashApp).trashAddressPoints
        trashAddressPoints.value?.removeAt(mainActivity.getThrashAddressPointIndex())
        trashAddressPoints.notifyObserver()
    }

    private val cancelFun = DialogInterface.OnClickListener { dialog, id ->
        dialog.cancel()
    }
}