package com.a1tt.security

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.a1tt.security.AnalysResults.*
import com.a1tt.security.AnalysResults.SingleAppAnalysResult.Companion.addResultsAppServices
import com.a1tt.security.AnalysResults.SingleURLAnalysResult.Companion.addResultsServices
import com.a1tt.security.Constants.Companion.GET_SCAN_FILE_RESULT
import com.a1tt.security.Constants.Companion.GET_SCAN_URL_RESULT
import com.a1tt.security.Constants.Companion.GOT_SCAN_FILE_RESULT
import com.a1tt.security.Constants.Companion.GOT_SCAN_URL_RESULT
import com.a1tt.security.Constants.Companion.SUCCESED_READ_FILE_FROM_DB
import com.a1tt.security.Constants.Companion.SUCCESED_READ_URL_FROM_DB
import com.a1tt.security.Constants.Companion.SUCCESED_WRITE_FILE_TO_DB
import com.a1tt.security.Constants.Companion.SUCCESED_WRITE_URL_TO_DB
import com.a1tt.security.DB.DBHelper
import com.a1tt.security.data.FileScanServicesResult
import com.a1tt.security.data.ScannedFile
import com.a1tt.security.data.ScannedURL
import com.a1tt.security.data.URLScanServicesResult
import com.a1tt.security.shedulers.DBFileWorker
import com.a1tt.security.shedulers.DBURLWorker
import com.a1tt.security.shedulers.ScanFileScheduler
import com.a1tt.security.shedulers.ScanURLScheduler
import org.json.JSONObject

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

        mainHandler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    GET_SCAN_URL_RESULT -> {
                        val firstPair = Pair(Constants.APIKEY_STR, "746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225")
                        val secondPair = Pair(Constants.RESOURCE_STR, (msg.obj as String))
                        val thirdPair = Pair(Constants.ALLINFO_BOOL, "true")
                        val args: MutableList<Pair<String, String>> = mutableListOf(firstPair, secondPair, thirdPair)
                        Thread(ScanURLScheduler(false, "https://www.virustotal.com/vtapi/v2/url/report", args, this)).start()
                    }
                    GET_SCAN_FILE_RESULT -> {
                        val firstPair = Pair(Constants.APIKEY_STR, "746cdb67b9f9ef1b202e04051b84bbec8756e908a0b6a7b6ed409b7f0a616225")
                        val secondPair = Pair(Constants.RESOURCE_STR, (msg.obj as JSONObject).getString("scan_id"))
                        val thirdPair = Pair(Constants.ALLINFO_BOOL, "true")
                        val args: MutableList<Pair<String, String>> = mutableListOf(firstPair, secondPair, thirdPair)
                        Thread(ScanFileScheduler(false, "https://www.virustotal.com/vtapi/v2/file/report", args, this, "",
                                (msg.obj as JSONObject).getString("app_name"))).start()
                    }
                    GOT_SCAN_URL_RESULT -> {
                        val scanResult: JSONObject = msg.obj as JSONObject

                        val scans = mutableListOf<URLScanServicesResult>()
                        val myScans = scanResult.getJSONObject("scans")
                        for (elem in myScans.keys()) {
                            val myElem = myScans.getJSONObject(elem)
                            val detected = myElem.getBoolean("detected")
                            val result = myElem.getString("result")
                            val detail = if (myElem.has("detail")) myElem.getString("detail") else ""
                            scans.add(URLScanServicesResult(elem, detected, result, detail))
                        }
                        val scannedURL = ScannedURL(scanResult.getString("url"), scanResult.getString("scan_date"), scanResult.getString("verbose_msg"),
                                scanResult.getInt("positives"), scanResult.getInt("total"), scans)
                        MainApplication.urlDataManager.addURL(scannedURL)
                        MainApplication.dbScheduler.executor.execute(DBURLWorker(applicationContext, "add", this, scannedURL, null))
                    }
                    GOT_SCAN_FILE_RESULT -> {
                        val scanResult: JSONObject = msg.obj as JSONObject
                        val scans = mutableListOf<FileScanServicesResult>()
                        val myScans = scanResult.getJSONObject("scans")
                        for (elem in myScans.keys()) {
                            val myElem = myScans.getJSONObject(elem)
                            val detected = myElem.getBoolean("detected")
                            val version = myElem.getString("version")
                            val result = myElem.getString("result")
                            val update = myElem.getString("update")
                            scans.add(FileScanServicesResult(elem, detected, version, result, update))
                        }
                        val scannedFile = ScannedFile(scanResult.getString("app_name"), scanResult.getString("scan_date"), scanResult.getString("verbose_msg"),
                                scanResult.getInt("positives"), scanResult.getInt("total"), scans)
                        MainApplication.dbScheduler.executor.execute(DBFileWorker(applicationContext, "add", this, scannedFile, null))
                    }
                    SUCCESED_WRITE_URL_TO_DB -> {

                    }
                    SUCCESED_WRITE_FILE_TO_DB -> {
                    }
                    SUCCESED_READ_URL_FROM_DB -> {

                        val existingFragment = supportFragmentManager?.findFragmentById(R.id.fragment_container)
                        when (existingFragment) {
                            is SingleURLAnalysResult -> {
                                if (existingFragment.isVisible) {
                                    addResultsServices((msg.obj as ScannedURL).scans)
                                }
                            }
                        }
                        MainApplication.singleURLResultController.liveData.postValue((msg.obj as ScannedURL))
                    }
                    SUCCESED_READ_FILE_FROM_DB -> {
                        val existingFragment = supportFragmentManager?.findFragmentById(R.id.fragment_container)
                        when (existingFragment) {
                            is SingleAppAnalysResult -> {
                                if (existingFragment.isVisible) {
                                    addResultsAppServices((msg.obj as ScannedFile).scans)
                                }
                            }
                        }
                        MainApplication.singleFileResultController.liveData.postValue((msg.obj as ScannedFile))
                    }

                    else -> {

                    }
                }
            }
        }

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
                router.navigateTo(fragmentFactory = ::URLAnalysResult)
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

        lateinit var mainHandler: Handler
    }

    fun test(view: View) {

        router.navigateTo(fragmentFactory = {
            val singleURLResult = SingleURLAnalysResult()
            val bundle = Bundle()
            bundle.putString("url", view.findViewById<TextView>(R.id.card_title).text as String)
            singleURLResult.arguments = bundle
            singleURLResult
        })
        Log.e("A1tt", " " + view.findViewById<TextView>(R.id.card_title).text)
        Log.e("A1tt", "click")
    }

}
