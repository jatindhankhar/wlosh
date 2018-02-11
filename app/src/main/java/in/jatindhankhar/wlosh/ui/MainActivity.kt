package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.ui.adapters.SimpleFragmentPagerAdapter
import `in`.jatindhankhar.wlosh.ui.fragments.ImagesFragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*


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


}
