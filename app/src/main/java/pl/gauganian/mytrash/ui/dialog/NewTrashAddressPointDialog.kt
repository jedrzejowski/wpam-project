package pl.gauganian.mytrash.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import pl.gauganian.mytrash.MyTrashApp
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.DataProvider
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.helper.doAsync
import pl.gauganian.mytrash.helper.notifyObserver
import java.lang.Exception


class NewTrashAddressPointDialog : DialogOnMainActivity() {

    private lateinit var searchInput: AutoCompleteTextView
    private lateinit var searchAdapter: ThrashAddressPointAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val rootView = inflater.inflate(R.layout.dialog_trashaddresspoint_new, null)

            searchInput = rootView.findViewById(R.id.searchInput)
            searchAdapter = ThrashAddressPointAdapter()
            searchInput.setAdapter(searchAdapter)
            searchInput.addTextChangedListener(searchAdapter)
            searchInput.onItemSelectedListener = searchAdapter
            searchInput.onItemClickListener = searchAdapter

            builder.apply {
                setView(rootView)
                setMessage(R.string.dialog_new_title)
                setNegativeButton(R.string.dialog_new_cancel, cancelFun)
            }

            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private val cancelFun = DialogInterface.OnClickListener { dialog, id ->
        dialog.cancel()
    }

    fun handleTrashAddressPointSelected(trashAddressPoint: TrashAddressPoint) {
        val data = (context?.applicationContext as MyTrashApp).trashAddressPoints
        Log.w("handleTrashAddressPointSelected", trashAddressPoint.toJSON().toString())
        data.value?.add(trashAddressPoint)
        data.notifyObserver()
        dialog.cancel()
    }

    inner class ThrashAddressPointAdapter :
        ArrayAdapter<TrashAddressPoint>(
            context ?: throw IllegalStateException("Activity cannot be null"),
            android.R.layout.simple_dropdown_item_1line,
            android.R.id.text1
        ), TextWatcher, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

        private val suggestions = MutableLiveData<ArrayList<TrashAddressPoint>>()
        private var asyncTask: doAsync? = null

        init {
            suggestions.observe(
                this@NewTrashAddressPointDialog,
                Observer { notifyDataSetChanged() }
            )
        }

        override fun getItem(position: Int): TrashAddressPoint {
            return suggestions.value?.get(position) ?: throw ArrayIndexOutOfBoundsException()
        }

        override fun getItemId(position: Int): Long {
            return try {
                getItem(position).id.toLong()
            } catch (e: NumberFormatException) {
                position.toLong()
            }
        }

        override fun getCount(): Int {
            return suggestions.value?.size ?: 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent) as TextView
            view.text = getItem(position).fullName
            return view
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s == null || s.length < 3) return

            asyncTask = doAsync {
                try {
                    val jarray = DataProvider.downloadAutocomplete(s.toString())
                    val array = ArrayList<TrashAddressPoint>()

                    for (i in 0 until jarray.length())
                        array.add(TrashAddressPoint(jarray.getJSONObject(i)))

                    suggestions.postValue(array)

                } catch (e: Exception) {
                    TODO("pokazać błąd")
                }
            }
            asyncTask?.execute()
        }

        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            asyncTask?.cancel(true)
            asyncTask = null
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            handleTrashAddressPointSelected(getItem(position))
        }

        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            handleTrashAddressPointSelected(getItem(position))
        }
    }
}