package `in`.jatindhankhar.wlosh.ui.fragments

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.R.drawable.toggle
import `in`.jatindhankhar.wlosh.R.drawable.view_list
import `in`.jatindhankhar.wlosh.model.Response
import `in`.jatindhankhar.wlosh.network.UnSplashClient
import `in`.jatindhankhar.wlosh.ui.adapters.ImagesAdapter
import `in`.jatindhankhar.wlosh.utils.Constants.ARG_PAGE_CATEGORY
import `in`.jatindhankhar.wlosh.utils.Essentials
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

import kotlinx.android.synthetic.main.content_main.view.*

/**
 * Created by jatin on 2/10/18.
 */
class ImagesFragment: Fragment() {

    private  var mPageCategory:String? = null
    private  lateinit var mUnSplashClient: UnSplashClient
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mAdapter: ImagesAdapter
    private lateinit var mMenuItem: ImageView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_page,container,false)
        val context = inflater.context
        mLayoutManager = GridLayoutManager(context,2)
        mAdapter = ImagesAdapter(context, null)
        view.recycler_view.layoutManager = mLayoutManager
        view.recycler_view.adapter = mAdapter
        view.recycler_view.layoutManager = mLayoutManager
        view.recycler_view.adapter = mAdapter
        view.recycler_view.visibility = View.VISIBLE
        mPageCategory?.let { mUnSplashClient.fetchWallPapers(category = it) }
        return view

    }

    companion object {
        fun newInstance(category: String): ImagesFragment {
            val args = Bundle()
            args.putString(ARG_PAGE_CATEGORY,category)
            val fragment = ImagesFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private fun handleLoadFailure()
    {

    }

    private fun handleLoadSuccess(response: List<Response>)
    {
        mAdapter.appendData(response)

    }

    private fun initUnSplashClient() :UnSplashClient
    {
        return object: UnSplashClient()
        {
            override fun onFetchWallpapersFailure() {
                handleLoadFailure()
            }

            override fun onFetchWallpapersSuccess(response: List<Response>) {
                handleLoadSuccess(response)
            }

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mPageCategory = arguments?.getString(ARG_PAGE_CATEGORY)
        setHasOptionsMenu(true)
        mUnSplashClient = initUnSplashClient()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater )
         inflater.inflate(R.menu.menu_main, menu)
        mMenuItem = menu.findItem(R.id.switch_layout).actionView as ImageView
        //val toggleButton = menu.findItem(R.id.switch_layout).actionView as ImageView

        // Initial State
        mMenuItem.setOnClickListener({
            Essentials.setFadeInAnimation(it)
            if(mLayoutManager.spanCount == 2)
            {
                mLayoutManager.spanCount = 1
                it.setBackgroundResource(view_list)
            }
            else
            {
                mLayoutManager.spanCount = 2
                it.setBackgroundResource(toggle)
            }
        })


    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        mMenuItem.setBackgroundResource( Essentials.getToggleIcon(mLayoutManager.spanCount))
       // activity?.invalidateOptionsMenu()

    }


}