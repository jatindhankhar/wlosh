package `in`.jatindhankhar.wlosh.utils

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/**
 * Created by jatin on 2/17/18.
 */

// Thanks http://akbaribrahim.com/empty-view-for-androids-recyclerview/
class FallbackLayoutRecyclerView : RecyclerView {

    private var emptyView: View? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet,
                defStyle: Int) : super(context, attrs, defStyle)


    internal fun checkIfEmpty() {
        if (emptyView != null && adapter != null) {
            val emptyViewVisible = adapter.itemCount == 0
            emptyView!!.visibility = if (emptyViewVisible) View.VISIBLE else View.GONE
            visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)

        checkIfEmpty()
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
        checkIfEmpty()
    }
}