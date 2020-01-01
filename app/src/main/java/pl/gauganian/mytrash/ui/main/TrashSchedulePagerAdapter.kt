package pl.gauganian.mytrash.ui.main

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import pl.gauganian.mytrash.MyTrashApp
import pl.gauganian.mytrash.data.TrashAddressPoint

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TrashSchedulePagerAdapter(
    private val context: Context,
    fm: FragmentManager
) : FragmentStatePagerAdapter(fm) {

    private val trashAddressPoints = (context.applicationContext as MyTrashApp).trashAddressPoints

    private fun getTrashAddressPoint(position: Int): TrashAddressPoint {
        trashAddressPoints.value?.let {
            return it[position]
        } ?: throw ClassCastException("$context: positions $position is not valid")
    }

    override fun getItem(position: Int): Fragment {
        return TrashScheduleFragment.newInstance(getTrashAddressPoint(position))
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return getTrashAddressPoint(position).customName
    }

    // tu jest problem z odświerzaniem ilosci
    // https://stackoverflow.com/questions/10396321/remove-fragment-page-from-viewpager-in-android

//    override fun getItemId(position: Int): Long {
//        return try {
//            getTrashAddressPoint(position).id.toLong()
//        } catch (e: NumberFormatException) {
//            position.toLong()
//        }
//    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return trashAddressPoints.value?.size ?: 0
    }
}