package pl.gauganian.mytrash.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pl.gauganian.mytrash.MyTrashApp
import pl.gauganian.mytrash.R
import pl.gauganian.mytrash.data.TrashAddressPoint

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val addressPoints: ArrayList<TrashAddressPoint> =
        (context.applicationContext as MyTrashApp).getAddressPoints()


    override fun getItem(position: Int): Fragment {
        return TrashScheduleFragment.newInstance(addressPoints[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return addressPoints[position].fullName
    }

    override fun getCount(): Int {
        return addressPoints.size
    }
}