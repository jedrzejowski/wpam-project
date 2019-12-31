package pl.gauganian.mytrash.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.TrashAddressPoint

/**
 * A placeholder fragment containing a simple view.
 */
class TrashScheduleFragment : Fragment() {

    private lateinit var dataModel: TrashSchedulePagerDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataModel = ViewModelProviders.of(this)
            .get(TrashSchedulePagerDataModel::class.java)
            .apply {
                addressPoint.value = TrashAddressPoint(arguments?.getString(ARG_ADRPOINT))
            }

        dataModel.loadSchedule()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val listView: ListView = root.findViewById(R.id.listView)

        dataModel.schedule.observe(this, Observer {
            listView.adapter = if (it != null) TrashScheduleListAdapter(this.context, it) else null
        })

        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_ADRPOINT = "trashAddressPoint"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(trashAddressPoint: TrashAddressPoint): TrashScheduleFragment {
            return TrashScheduleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ADRPOINT, trashAddressPoint.toJSON().toString())
                }
            }
        }
    }
}