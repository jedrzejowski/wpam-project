package pl.gauganian.mytrash.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import pl.gauganian.mytrash.MyTrashApp
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.helper.notifyObserver


class EditTrashAddressPointDialog : DialogOnMainActivity() {

    private lateinit var idInputView: TextInputEditText
    private lateinit var fullNameInputView: TextInputEditText
    private lateinit var customNameInputView: TextInputEditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val rootView = inflater.inflate(R.layout.dialog_trashaddresspoint_edit, null)

            idInputView = rootView.findViewById(R.id.id)
            fullNameInputView = rootView.findViewById(R.id.fullName)
            customNameInputView = rootView.findViewById(R.id.customName)

            idInputView.setText(trashAddressPoint?.id)
            fullNameInputView.setText(trashAddressPoint?.fullName)
            customNameInputView.setText(trashAddressPoint?.customName)

            builder.apply {
                setView(rootView)
                setMessage(R.string.dialog_edit_title)
                setPositiveButton(R.string.dialog_edit_save, saveHandler)
                setNeutralButton(R.string.dialog_edit_delete, deleteHandler)
                setNegativeButton(R.string.dialog_edit_cancel, cancelFun)
            }

            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private val saveHandler = DialogInterface.OnClickListener { dialog, id ->
        val data = (context?.applicationContext as MyTrashApp).trashAddressPoints

        data.value?.set(
            trashAddressPointIndex, TrashAddressPoint(
                idInputView.text.toString(),
                fullNameInputView.text.toString(),
                customNameInputView.text.toString()
            )
        )

        data.notifyObserver()
    }

    private val deleteHandler = DialogInterface.OnClickListener { dialog, id ->
        val data = (context?.applicationContext as MyTrashApp).trashAddressPoints
        data.value?.removeAt(trashAddressPointIndex)
        data.notifyObserver()
    }

    private val cancelFun = DialogInterface.OnClickListener { dialog, id ->
        dialog.cancel()
    }
}