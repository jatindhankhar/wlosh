package `in`.jatindhankhar.wlosh.ui

import `in`.jatindhankhar.wlosh.R
import `in`.jatindhankhar.wlosh.model.Response
import `in`.jatindhankhar.wlosh.network.ServiceGenerator
import `in`.jatindhankhar.wlosh.network.UnsplashService
import `in`.jatindhankhar.wlosh.ui.adapters.ImagesAdapter
import `in`.jatindhankhar.wlosh.ui.adapters.SimpleFragmentPagerAdapter
import `in`.jatindhankhar.wlosh.ui.fragments.ImagesFragment
import `in`.jatindhankhar.wlosh.ui.listeners.InfiniteScrollListener
import android.os.Build

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_load_prompt.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback


class MainActivity : AppCompatActivity() {



    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mAdapter: ImagesAdapter
    private lateinit var mUnsplashService: UnsplashService
    private lateinit var mBottomSheetDialog: BottomSheetDialog
    private lateinit var mPagerAdapter: SimpleFragmentPagerAdapter
    private lateinit var mMenu: Menu
    private var pageNumber = 1
    private var promptIncrease = 2
    private var promptThreshold = pageNumber + promptIncrease
    private var loadingBlocked = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mLayoutManager = GridLayoutManager(this,2);
        mAdapter = ImagesAdapter(applicationContext, null)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.adapter = mAdapter
        mUnsplashService = ServiceGenerator.create()
       // loadMoreWallpapers(category = "curated")
        recycler_view.addOnScrollListener(initInfiniteScroller( ))
        mBottomSheetDialog = initBottomSheetDialog()
        mPagerAdapter = SimpleFragmentPagerAdapter(supportFragmentManager,this)
        viewpager.adapter = mPagerAdapter
        mPagerAdapter.addFragment(ImagesFragment.newInstance(""))
        mPagerAdapter.addFragment(ImagesFragment.newInstance("curated"))
        //mPagerAdapter.addFragment(PageFragment.newInstance(2))
        sliding_tabs.setupWithViewPager(viewpager)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        /*var snackbar =Snackbar.make(coordinator_layout,"Load More",Snackbar.LENGTH_INDEFINITE)
                .setAction("Yes", View.OnClickListener {  promptThreshold+=5; loadMoreWallpapers() })
        snackbar.addCallback(object: Snackbar.Callback() {

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
            }
        })
        snackbar.show()*/
        //mBottomSheetDialog.show()



    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }


    private fun initBottomSheetDialog() : BottomSheetDialog
    {
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(this.layoutInflater.inflate(R.layout.bottom_sheet_load_prompt,null))
        dialog.load_affirmation.setOnClickListener { _ -> loadMoreWallpapers(); promptThreshold +=promptIncrease;dialog.cancel()  }
        dialog.load_negetation.setOnClickListener { _ -> loadingBlocked = true; dialog.dismiss()}
        dialog.setOnDismissListener { _ -> loadingBlocked = true }
        return dialog
    }
   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val toggleButton = menu.findItem(switch_layout).actionView as ImageView
        // Initial State
        toggleButton.setBackgroundResource(toggle)
     /*   toggleButton.setOnClickListener {
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
        }*/


        return true
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initInfiniteScroller() : RecyclerView.OnScrollListener
    {

        return object: InfiniteScrollListener(mLayoutManager,10) {
            override fun rePrompt() {
                if(loadingBlocked)
                {
                    //mBottomSheetDialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                    mBottomSheetDialog.show()
                }
            }

            override fun onLoadMore() {
                Log.d("YOLO","Page Number is " + pageNumber)
                Log.d("YOLO","Threshold is " + promptThreshold)
                if(promptThreshold <= pageNumber)
                {
                    mBottomSheetDialog.show()

                }

                else
                {
                    loadMoreWallpapers()
                }
            }

        }

    }

    fun loadMoreWallpapers(page:Int = 1,category: String="curated" )
    {

        Log.d("YOLO","Damn currenrt is " + page)
        loading_animation.visibility = View.VISIBLE
        mUnsplashService.processPhotos(category,page=pageNumber).enqueue( object : Callback<List<Response>>
        {

            override fun onFailure(call: Call<List<Response>>?, t: Throwable?) {
                Log.d("DEBUG","Request failed" + t.toString())
            }

            override fun onResponse(call: Call<List<Response>>?, response: retrofit2.Response<List<Response>>?) {
                if(response != null && response.isSuccessful())
                {
                    Log.d("DEBUG", Gson().toJson(response.body()));
                    response.body()?.let { mAdapter.appendData(it) }
                    loading_animation.visibility = View.GONE
                    pageNumber++
                    loadingBlocked = false
                }
                else
                {
                    Log.d("DEBUG","There was some error" + response?.errorBody())
                }
            }

        });

    }


}
