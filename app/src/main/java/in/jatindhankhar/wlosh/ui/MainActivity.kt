package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.ui.adapters.SimpleFragmentPagerAdapter
import `in`.jatindhankhar.wlosh.ui.fragments.ImagesFragment
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import org.jetbrains.anko.design.longSnackbar


class MainActivity : AppCompatActivity() {


    private lateinit var mPagerAdapter: SimpleFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mPagerAdapter = SimpleFragmentPagerAdapter(supportFragmentManager, this)
        viewpager.adapter = mPagerAdapter
        mPagerAdapter.addFragment(ImagesFragment.newInstance(""))
        mPagerAdapter.addFragment(ImagesFragment.newInstance("curated"))
        //mPagerAdapter.addFragment(PageFragment.newInstance(2))
        sliding_tabs.setupWithViewPager(viewpager)
        if(!haveNetworkConnection())
        {
            longSnackbar(coordinator_layout,"Looks like, you are not connected. Some functionality will not be available")

        }


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false

        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals("WIFI", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedWifi = true
            if (ni.typeName.equals("MOBILE", ignoreCase = true))
                if (ni.isConnected)
                    haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }
}
