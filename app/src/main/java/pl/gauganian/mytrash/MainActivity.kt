package pl.gauganian.mytrash

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.ui.dialog.*
import pl.gauganian.mytrash.ui.main.TrashSchedulePagerAdapter


class MainActivity : AppCompatActivity(), TrashPointDialogListener {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: TrashSchedulePagerAdapter
    private lateinit var tabs: TabLayout

    private lateinit var trashAddressPoints: MutableLiveData<ArrayList<TrashAddressPoint>>

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
        tabs.visibility = if ((trashAddressPoints.value?.size ?: 0) >= 2)
            View.VISIBLE else View.GONE

        trashAddressPoints.observe(this, Observer {
            pagerAdapter.notifyDataSetChanged()
            tabs.visibility = if ((trashAddressPoints.value?.size ?: 0) >= 2)
                View.VISIBLE else View.GONE
        })

        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu?.findItem(R.id.add)?.actionView as SearchView?
        searchView?.isSubmitButtonEnabled = true
//        searchView.setOnQueryTextListener(onQueryTextListener)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
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

        R.id.about -> {
            startActivity(Intent(this, AboutActivity::class.java))
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    override fun getThrashAddressPointIndex(): Int {
        return viewPager.currentItem
    }

    override fun getThrashAddressPoint(): TrashAddressPoint? {
        return (applicationContext as MyTrashApp).trashAddressPoints.value?.get(
            getThrashAddressPointIndex()
        )
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

    companion object {
        private const val FRG_DIALOG_EDIT = "editDialog"
        private const val FRG_DIALOG_NEW = FRG_DIALOG_EDIT
        private const val FRG_DIALOG_DELETE = "deleteDialog"
    }
}