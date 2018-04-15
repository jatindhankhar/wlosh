package `in`.jatindhankhar.wlosh.ui.adapters

import `in`.jatindhankhar.wlosh.utils.Constants.TAB_COUNT
import `in`.jatindhankhar.wlosh.utils.Constants.TAB_TITLES
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by jatin on 2/10/18.
 */
class SimpleFragmentPagerAdapter(mFragmentManager: FragmentManager, mContext: Context) : FragmentPagerAdapter(mFragmentManager) {
    private val mFragments: MutableList<Fragment> = mutableListOf()


    fun addFragment(fragment: Fragment) {
        mFragments.add(fragment)
        notifyDataSetChanged()
    }

    fun addAllFragments(fragment: List<Fragment>) {
        mFragments.addAll(fragment)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (position + 1 > TAB_COUNT)
            ""
        else
            TAB_TITLES[position]
    }
}