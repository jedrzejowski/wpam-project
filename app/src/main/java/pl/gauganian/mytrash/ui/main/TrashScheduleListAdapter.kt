package pl.gauganian.mytrash.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.TrashSchedule
import android.widget.TextView
import android.widget.ImageView
import pl.gauganian.mytrash.data.TrashScheduleItem
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class TrashScheduleListAdapter(
    private val context: Context?,
    private val schedule: TrashSchedule
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return schedule.fractions.size
    }

    override fun getItem(position: Int): TrashScheduleItem {
        return schedule.fractions[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            view = inflater.inflate(R.layout.trash_fration_item, null)
            holder = ViewHolder(
                view.findViewById(R.id.image) as ImageView,
                view.findViewById(R.id.title) as TextView,
                view.findViewById(R.id.date1) as TextView,
                view.findViewById(R.id.date2) as TextView
            )
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val item = getItem(position)

        holder.image.setImageResource(item.fraction?.icon ?: R.drawable.ic_trash_fraction_zm)
        holder.title.setText(item.fraction?.title ?: R.string.empty)

        val date = item.date
        if (date == null) {
            holder.date1.setText(R.string.trash_schedule_nodate)
            holder.date2.setText(R.string.empty)
        } else {
            holder.date1.text = date.format(DateTimeFormatter.ofPattern("d MMMM"))
            holder.date2.text = date.format(DateTimeFormatter.ofPattern("eeee"))
        }

        return view
    }

    internal data class ViewHolder(
        val image: ImageView,
        val title: TextView,
        val date1: TextView,
        val date2: TextView
    )
}

