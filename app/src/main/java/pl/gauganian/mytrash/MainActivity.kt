package pl.gauganian.mytrash

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import pl.gauganian.mytrash.data.TrashAddressPoint
import pl.gauganian.mytrash.ui.dialog.EditTrashAddressPointDialog
import pl.gauganian.mytrash.ui.dialog.TrashPointDialogListener
import pl.gauganian.mytrash.ui.main.TrashSchedulePagerAdapter

class MainActivity : AppCompatActivity(), TrashPointDialogListener {

    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: TrashSchedulePagerAdapter
    private lateinit var tabs: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        pagerAdapter = TrashSchedulePagerAdapter(this, supportFragmentManager)

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = pagerAdapter

        tabs = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        (applicationContext as MyTrashApp).trashAddressPoints.observe(this, Observer {
            pagerAdapter.notifyDataSetChanged()
        })

        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {

            }

            R.id.edit -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                val prev = supportFragmentManager.findFragmentByTag(FRG_DIALOG_EDIT)
                if (prev != null)
                    fragmentTransaction.remove(prev)

                fragmentTransaction.addToBackStack(null)
                val dialogFragment = EditTrashAddressPointDialog()
                dialogFragment.show(fragmentTransaction, FRG_DIALOG_EDIT)
            }

            R.id.about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                return true
            }
        }

        return false
    }

    override fun getThrashAddressPointIndex(): Int {
        return viewPager.currentItem
    }

    override fun getThrashAddressPoint(): TrashAddressPoint? {
        return (applicationContext as MyTrashApp).trashAddressPoints.value?.get(getThrashAddressPointIndex())
    }

    override fun onReloadRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        private const val FRG_DIALOG_EDIT = "editDialog"
    }
}