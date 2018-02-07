package `in`.jatindhankhar.wlash.ui

import `in`.jatindhankhar.wlash.R
import `in`.jatindhankhar.wlash.R.drawable.toggle
import `in`.jatindhankhar.wlash.R.id.switch_layout
import `in`.jatindhankhar.wlash.model.Response
import `in`.jatindhankhar.wlash.network.ServiceGenerator
import `in`.jatindhankhar.wlash.network.UnsplashClient
import `in`.jatindhankhar.wlash.ui.adapters.ImagesAdpater
import android.os.Bundle
import android.support.transition.Fade
import android.support.transition.TransitionManager
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.gson.Gson

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import retrofit2.Call
import retrofit2.Callback
import android.support.v4.view.ViewCompat.setRotation
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.OvershootInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView


class MainActivity : AppCompatActivity() {

    private lateinit var mLayoutManager: GridLayoutManager
    private lateinit var mAdapter: ImagesAdpater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mLayoutManager = GridLayoutManager(this,2);
        mAdapter = ImagesAdpater(baseContext)
        recycler_view.layoutManager = mLayoutManager
        recycler_view.adapter = mAdapter

        val unsplashClient = ServiceGenerator.create();
        unsplashClient.processPhotos().enqueue( object : Callback<List<Response>>
        {

            override fun onFailure(call: Call<List<Response>>?, t: Throwable?) {
                Log.d("DEBUG","Request failed" + t.toString())
            }

            override fun onResponse(call: Call<List<Response>>?, response: retrofit2.Response<List<Response>>?) {
                if(response != null && response.isSuccessful())
                {
                    Log.d("DEBUG", Gson().toJson(response.body()));
                    response.body()?.let { mAdapter.loadData(it) }
                }
                else
                {
                    Log.d("DEBUG","There was some error" + response?.errorBody())
                }
            }

        });

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val toggleButton = menu.findItem(switch_layout).actionView as ImageView
        toggleButton.setBackgroundResource(toggle)
        toggleButton.setOnClickListener {
            it.rotation = 270.0f - it.rotation
            if(mLayoutManager.spanCount == 2) mLayoutManager.spanCount = 1
            else
                mLayoutManager.spanCount = 2
        }


        return true
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
