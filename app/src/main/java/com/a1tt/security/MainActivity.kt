package com.a1tt.security

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
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


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val mToolbar : Toolbar = findViewById<View>(R.id.myToolbar) as Toolbar
        setSupportActionBar(mToolbar)

//        val actionBar : ActionBar? = getSupportActionBar()
//        actionBar?.title = "test"

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, findViewById(R.id.drawer_layout), mToolbar, R.string.open, R.string.close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

//        val fm: FragmentManager = supportFragmentManager
//        var fragment: Fragment? = fm.findFragmentById(R.id.fragment_container)
//
//        if (fragment == null) {
//            fragment = TargetAppListFragment()
//            fm.beginTransaction()
//                    .add(R.id.fragment_container, fragment)
//                    .commit()
//        }

        router = Router(this, R.id.fragment_container)
        if (savedInstanceState == null) router.navigateTo(false, ::TargetAppListFragment)
    }

    override fun onBackPressed() {
        if ( (findViewById(R.id.drawer_layout) as DrawerLayout).isDrawerOpen(GravityCompat.START)) {
            (findViewById(R.id.drawer_layout) as DrawerLayout).closeDrawer(GravityCompat.START)
        } else {
            if (!router.navigateBack()) {
                super.onBackPressed()
            }
        }
    }

    private fun doSearch(str : String?) {
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

        val searchView : SearchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                doSearch(null)
                return false
            }
        })
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
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.app_list -> {
                router.navigateTo(fragmentFactory = ::TargetAppListFragment)
                //changeFragment(TargetAppListFragment(), "Installed apps")
            }
            R.id.nav_camera -> {
                router.navigateTo(fragmentFactory = ::ResultFragment)
//                changeFragment(ResultFragment(), "cards")
            }
            R.id.nav_gallery -> {
                router.navigateTo(fragmentFactory = ::ScanURLFragment)
                //changeFragment(ScanURLFragment(), "gallery")
            }
            R.id.nav_manage,
            R.id.nav_share,
            R.id.nav_send -> {}
        }
        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    companion object {
        public lateinit var router  : Router
    }

    public fun changeFragment(fragment: Fragment, tag: String) {
        val fragmentManager = supportFragmentManager
        val exist = fragmentManager.findFragmentByTag(tag) ?: fragment
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, exist, tag).commit()
    }
}
