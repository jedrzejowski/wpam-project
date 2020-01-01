package pl.gauganian.mytrash

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.helper.ErrorSink
import pl.gauganian.mytrash.service.BackgroundNotifier
import pl.gauganian.mytrash.ui.dialog.*
import pl.gauganian.mytrash.ui.main.TrashSchedulePagerAdapter
import java.lang.Exception
import java.lang.IndexOutOfBoundsException


class MainActivity : AppCompatActivity(), TrashPointDialogListener, ErrorSink {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: TrashSchedulePagerAdapter
    private lateinit var tabs: TabLayout

    private var deleteMenuItem: MenuItem? = null
    private var editMenuItem: MenuItem? = null

    private lateinit var trashAddressPoints: MutableLiveData<ArrayList<TrashAddressPoint>>

    private var mListeners = ArrayList<ActionListener>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        trashAddressPoints = (applicationContext as MyTrashApp).trashAddressPoints
        pagerAdapter = TrashSchedulePagerAdapter(this, supportFragmentManager)

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter

        tabs = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        trashAddressPoints.observe(this, Observer {
            pagerAdapter.notifyDataSetChanged()
            handleUiRefresh()
        })

        // Szybkie powiadomienie na prezentacje
//        sendBroadcast(Intent(this, BackgroundNotifier::class.java))
    }

    override fun onResume() {
        super.onResume()
        handleUiRefresh()
        handleAddAtLeastOne()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        deleteMenuItem = menu?.findItem(R.id.delete)
        editMenuItem = menu?.findItem(R.id.edit)
        handleUiRefresh()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.sync -> {
            mListeners.forEach { it.onUserRefreshRequest() }
            true
        }

        R.id.add -> {
            handleShowDialog(FRG_DIALOG_NEW, NewTrashAddressPointDialog::class.java)
            true
        }

        R.id.edit -> {
            handleShowDialog(FRG_DIALOG_EDIT, EditTrashAddressPointDialog::class.java)
            true
        }

        R.id.delete -> {
            handleShowDialog(FRG_DIALOG_DELETE, DeleteTrashAddressPointDialog::class.java)
            true
        }

        R.id.settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        R.id.about -> {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    fun addActionListener(listener: ActionListener) {
        mListeners.add(listener)
    }

    fun removeActionListener(listener: ActionListener) {
        val i = mListeners.indexOf(listener)
        if (i >= 0) mListeners.remove(listener)
    }

    override fun getThrashAddressPointIndex(): Int {
        return viewPager.currentItem
    }

    override fun getThrashAddressPoint(): TrashAddressPoint? {
        return try {
            (applicationContext as MyTrashApp).trashAddressPoints.value?.get(
                getThrashAddressPointIndex()
            )
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    private fun <T : DialogOnMainActivity> handleShowDialog(tag: String, _class: Class<T>) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag(tag)
        if (prev != null)
            fragmentTransaction.remove(prev)

        fragmentTransaction.addToBackStack(null)
        val dialogFragment = _class.newInstance()
        dialogFragment.show(fragmentTransaction, tag)
    }

    override fun handleWarnSink(tag: String, msg: String) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    override fun handleErrorSink(tag: String, msg: Int, e: Exception) {
        handleErrorSink(tag, getString(msg), e)
    }

    override fun handleErrorSink(tag: String, msg: String, e: Exception) {
        Snackbar.make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show()
    }

    private fun handleUiRefresh() {
        val trashAddressPointsSize = trashAddressPoints.value?.size ?: 0
        // ukrycie zakładek
        tabs.visibility = if (trashAddressPointsSize >= 2) View.VISIBLE else View.GONE

        // wyłączenie opcji w menu
        editMenuItem?.setEnabled(trashAddressPointsSize != 0)
        deleteMenuItem?.setEnabled(trashAddressPointsSize != 0)
    }

    private fun handleAddAtLeastOne() {
        val trashAddressPointsSize = trashAddressPoints.value?.size ?: 0

        if (trashAddressPointsSize == 0)
            handleShowDialog(FRG_DIALOG_NEW, NewTrashAddressPointDialog::class.java)
    }

    companion object {
        private const val FRG_DIALOG_EDIT = "editDialog"
        private const val FRG_DIALOG_NEW = FRG_DIALOG_EDIT
        private const val FRG_DIALOG_DELETE = "deleteDialog"
    }

    interface ActionListener {
        fun onUserRefreshRequest() {}
    }
}