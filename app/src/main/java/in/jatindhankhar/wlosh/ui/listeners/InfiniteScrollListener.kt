package `in`.jatindhankhar.wlosh.ui.listeners

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.GridLayoutManager


/**
 * Created by jatin on 2/7/18.
 */
// Thanks https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView
abstract class InfiniteScrollListener(val mLayoutManager: GridLayoutManager, var visibleThreshold: Int = 10) : RecyclerView.OnScrollListener() {

    private var mPreviousTotal = 0
    private var mLoading = true
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        var visibleItemCount = recyclerView.childCount
        var totalItemCount =  mLayoutManager.itemCount
        var firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()
        if(mLoading)
        {
            if(totalItemCount > mPreviousTotal)
            {
                mLoading = false
                mPreviousTotal = totalItemCount
            }

        }
        if(!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleItemCount))
        {
            onLoadMore()
            mLoading = true
        }


    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if(!recyclerView.canScrollVertically(1) && recyclerView.canScrollVertically(-1))
        {
           rePrompt()
        }
    }

    abstract fun onLoadMore()

    abstract fun rePrompt()
}