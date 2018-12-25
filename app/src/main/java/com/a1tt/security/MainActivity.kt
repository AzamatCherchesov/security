package com.a1tt.security

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.SearchView
import android.view.View
import com.a1tt.security.AnalysResults.ResultFragment
import com.a1tt.security.Consts.Companion.GET_SCAN_URL_RESULT
import com.a1tt.security.Consts.Companion.GOT_SCAN_URL_RESULT
import com.a1tt.security.Consts.Companion.SUCCESED_WRITE_TO_DB
import com.a1tt.security.DB.DBHelper
import com.a1tt.security.shedulers.DBSheduler
import com.a1tt.security.shedulers.ScanURLSheduler

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val mToolbar: Toolbar = findViewById<View>(R.id.myToolbar) as Toolbar
        setSupportActionBar(mToolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, findViewById(R.id.drawer_layout), mToolbar, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        dbHelper = DBHelper(this@MainActivity)
        router = Router(this, R.id.fragment_container)
        if (savedInstanceState == null) router.navigateTo(false, ::TargetAppListFragment)
    }

    override fun onBackPressed() {
        if ((findViewById<DrawerLayout>(R.id.drawer_layout)).isDrawerOpen(GravityCompat.START)) {
            (findViewById<DrawerLayout>(R.id.drawer_layout)).closeDrawer(GravityCompat.START)
        } else {
            if (!router.navigateBack()) {
                super.onBackPressed()
            }
        }
    }

    private fun doSearch(str: String?) {
        val existingFragment = supportFragmentManager?.findFragmentById(R.id.fragment_container)
        when (existingFragment) {
            is TargetAppListFragment -> {
                if (str?.length == 0) existingFragment.filterAppList(null)
                existingFragment.filterAppList(str)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView: SearchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setOnCloseListener {
            doSearch(null)
            false
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean {
                doSearch(p0)
                menu.findItem(R.id.search).collapseActionView()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                doSearch(p0)
                menu.findItem(R.id.search).collapseActionView()
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_list -> {
                router.navigateTo(fragmentFactory = ::TargetAppListFragment)
            }
            R.id.scan_results -> {
                router.navigateTo(fragmentFactory = ::ResultFragment)
            }
            R.id.scan_url -> {
                router.navigateTo(fragmentFactory = ::ScanURLFragment)
            }
            R.id.nav_share,
            R.id.nav_send -> {
            }
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        lateinit var router: Router

        @SuppressLint("StaticFieldLeak")
        lateinit var dbHelper: DBHelper

        @SuppressLint("HandlerLeak")
        val mainHandler = object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    GET_SCAN_URL_RESULT -> {
                        val firstPair = Pair(Consts.APIKEY_STR, "746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225")
                        val secondPair = Pair(Consts.RESOURCE_STR, (msg.obj as String))
                        val thirdPair = Pair(Consts.ALLINFO_BOOL, "true")
                        val args: MutableList<Pair<String, String>> = mutableListOf(firstPair, secondPair, thirdPair)
                        Thread(ScanURLSheduler(false, "https://www.virustotal.com/vtapi/v2/url/report", args, this)).start()
                    }
                    GOT_SCAN_URL_RESULT -> {
//                        Thread(DBSheduler()).start()
                    }
                    SUCCESED_WRITE_TO_DB -> {

                    }
                    else -> {

                    }
                }
            }
        }
    }

}
