package com.a1tt.security

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.ActionBarContainer
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import com.a1tt.security.R.id.drawer_layout
import com.a1tt.security.R.id.nav_view

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

       val mToolbar : Toolbar = findViewById(R.id.myToolbar) as Toolbar
        setSupportActionBar(mToolbar)

//        val actionBar : ActionBar? = getSupportActionBar()
//        actionBar?.title = "test"

        val toggle = ActionBarDrawerToggle(
                this, findViewById(R.id.drawer_layout), mToolbar, R.string.open, R.string.close)
        (findViewById(R.id.drawer_layout) as DrawerLayout).addDrawerListener(toggle)
        toggle.syncState()

        (findViewById(R.id.nav_view) as NavigationView).setNavigationItemSelectedListener(this)

        val fm: FragmentManager = supportFragmentManager
        var fragment: Fragment? = fm.findFragmentById(R.id.fragment_container)

        if (fragment == null) {
            fragment = TargetAppListFragment()
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit()
        }
    }

    override fun onBackPressed() {
        if ( (findViewById(R.id.drawer_layout) as DrawerLayout).isDrawerOpen(GravityCompat.START)) {
            (findViewById(R.id.drawer_layout) as DrawerLayout).closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
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

        }
//        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
