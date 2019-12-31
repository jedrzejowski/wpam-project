package pl.gauganian.mytrash.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import pl.gauganian.mytrash.R


class EditTrashAddressPointDialog : DialogFragment() {

    internal lateinit var listener: TrashPointDialogListener

    private lateinit var idInputView: TextInputEditText
    private lateinit var fullNameInputView: TextInputEditText
    private lateinit var customNameInputView: TextInputEditText

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as TrashPointDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement NoticeDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val rootView = inflater.inflate(R.layout.dialog_trashaddresspoint_edit, null)

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

    }

    private val deleteHandler = DialogInterface.OnClickListener { dialog, id ->

    }

    private val cancelFun = DialogInterface.OnClickListener { dialog, id ->

    }
}