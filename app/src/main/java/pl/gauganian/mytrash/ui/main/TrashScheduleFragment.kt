package pl.gauganian.mytrash.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import pl.gauganian.mytrash.MainActivity
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.helper.ErrorSink

/**
 * A placeholder fragment containing a simple view.
 */
class TrashScheduleFragment : Fragment(), MainActivity.ActionListener {

    private lateinit var dataModel: TrashSchedulePagerDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataModel = ViewModelProviders.of(this)
            .get(TrashSchedulePagerDataModel::class.java)
            .apply {
                errorSink = activity as ErrorSink
                addressPoint.value = TrashAddressPoint(arguments?.getString(ARG_ADRPOINT))
            }

        dataModel.loadSchedule()

        (activity as MainActivity).addActionListener(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val listView: ListView = root.findViewById(R.id.listView)
        val loadingIndicator: View = root.findViewById(R.id.loadingIndicator)
        val errorIndicator: View = root.findViewById(R.id.errorIndicator)

        dataModel.state.observe(this, Observer {

            listView.visibility = View.GONE
            loadingIndicator.visibility = View.GONE
            errorIndicator.visibility = View.GONE

            when (it) {
                TrashSchedulePagerDataModel.State.Initial -> {
                }

                TrashSchedulePagerDataModel.State.Loading -> {
                    loadingIndicator.visibility = View.VISIBLE
                    listView.adapter = null
                }

                TrashSchedulePagerDataModel.State.Ready -> {
                    listView.visibility = View.VISIBLE

                    val schedule = dataModel.schedule.value
                    if (schedule != null)
                        listView.adapter = TrashScheduleListAdapter(this.context, schedule)
                }

                TrashSchedulePagerDataModel.State.Error -> {
                    errorIndicator.visibility = View.VISIBLE
                    listView.adapter = null
                }
            }
        })

        return root
    }

    override fun onUserRefreshRequest() {

        dataModel.loadSchedule()
    }

    override fun onDestroy() {
        (activity as MainActivity).removeActionListener(this)
        super.onDestroy()
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