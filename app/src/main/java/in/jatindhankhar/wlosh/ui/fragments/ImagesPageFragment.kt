package `in`.jatindhankhar.wlosh.ui.fragments

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.R.drawable.toggle
import `in`.jatindhankhar.wlosh.R.drawable.view_list
import `in`.jatindhankhar.wlosh.model.Response
import `in`.jatindhankhar.wlosh.network.UnSplashClient
import `in`.jatindhankhar.wlosh.ui.ImagesDetailActivity
import `in`.jatindhankhar.wlosh.ui.adapters.ImagesAdapter
import `in`.jatindhankhar.wlosh.ui.listeners.ImageItemClickListener
import `in`.jatindhankhar.wlosh.ui.listeners.InfiniteScrollListener
import `in`.jatindhankhar.wlosh.utils.Constants
import `in`.jatindhankhar.wlosh.utils.Constants.ARG_PAGE_CATEGORY
import `in`.jatindhankhar.wlosh.utils.Essentials
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.bottom_sheet_load_prompt.*
import kotlinx.android.synthetic.main.fragment_page.view.*


/**
 * Created by jatin on 2/10/18.
 */
class ImagesFragment: Fragment(),ImageItemClickListener {
    override fun OnItemClick(response: Response) {
        val intent = Intent(context,ImagesDetailActivity::class.java)
        intent.putExtra(Constants.INTENT_RESPONSE_KEY,response)
        startActivity(intent)

    }

    private  var mPageCategory:String = ""
    private  lateinit var mUnSplashClient: UnSplashClient
    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mAdapter: ImagesAdapter
    private lateinit var mMenuItem: ImageView
    private var pageNumber = 1
    private var promptIncrease = 7
    private var promptThreshold = pageNumber + promptIncrease
    private var loadingBlocked = true
    private lateinit var mBottomSheetDialog: BottomSheetDialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_page,container,false)
        val context = inflater.context
        mLayoutManager = GridLayoutManager(context,2)
        mAdapter = ImagesAdapter(context, null,this)
        mAdapter.setHasStableIds(true)
        mBottomSheetDialog = initBottomSheetDialog(context)
        view.recycler_view.layoutManager = mLayoutManager
        view.recycler_view.addOnScrollListener(initInfiniteScroller( ))

        view.recycler_view.adapter = mAdapter
        view.recycler_view.layoutManager = mLayoutManager
        view.recycler_view.adapter = mAdapter
        view.recycler_view.visibility = View.VISIBLE
        mPageCategory.let {  view.loading_animation.visibility = View.VISIBLE
            mUnSplashClient.fetchWallPapers(category = it) }
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
     view?.loading_animation?.visibility = View.GONE
    }

    private fun handleLoadSuccess(response: List<Response>)
    {
        view?.loading_animation?.visibility = View.GONE
        pageNumber++
        loadingBlocked = false
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
        this.mPageCategory = arguments?.getString(ARG_PAGE_CATEGORY)!!
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

    private fun initInfiniteScroller() : RecyclerView.OnScrollListener {

        return object : InfiniteScrollListener(mLayoutManager, 10) {
            override fun rePrompt() {
                if (loadingBlocked) {
                    //mBottomSheetDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                    mBottomSheetDialog.show()
                }
            }

            override fun onLoadMore() {

                if (promptThreshold <= pageNumber) {
                    mBottomSheetDialog.show()

                } else {
                    mPageCategory.let { view?.loading_animation?.visibility = View.VISIBLE; mUnSplashClient.fetchWallPapers(pageNumber, it) }
                }
            }

        }
    }

        @SuppressLint("InflateParams")
      private  fun initBottomSheetDialog(context: Context) : BottomSheetDialog
        {
            val dialog = BottomSheetDialog(context)
            dialog.setContentView(this.layoutInflater.inflate(R.layout.bottom_sheet_load_prompt,null))
            dialog.load_affirmation.setOnClickListener { _-> view?.loading_animation?.visibility = View.VISIBLE; mUnSplashClient.fetchWallPapers(pageNumber,mPageCategory); promptThreshold +=promptIncrease;dialog.cancel()  }
            dialog.load_negetation.setOnClickListener { _ -> loadingBlocked = true; dialog.dismiss()}
            dialog.setOnDismissListener { _ -> loadingBlocked = true }
            return dialog
        }
}