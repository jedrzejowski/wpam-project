package pl.gauganian.mytrash.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import pl.gauganian.mytrash.R

/**
 * A placeholder fragment containing a simple view.
 */
class TrashScheduleFragment : Fragment() {

    private lateinit var dataModel: TrashSchedulePagerDataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataModel = ViewModelProviders.of(this).get(TrashSchedulePagerDataModel::class.java)
            .apply {
                Log.d("MY TAG", "bum bam")
                setIndex((arguments?.getInt(ARG_SCHEDULE_INDEX) ?: 1) + 1)
            }


        dataModel.downloadSchedule()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val textView: TextView = root.findViewById(R.id.section_label)
        dataModel.text.observe(this, Observer<String> {
            textView.text = it
        })
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SCHEDULE_INDEX = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): TrashScheduleFragment {
            return TrashScheduleFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SCHEDULE_INDEX, sectionNumber)
                }
            }
        }
    }
}