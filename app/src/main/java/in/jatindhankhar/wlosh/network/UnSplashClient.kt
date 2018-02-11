package `in`.jatindhankhar.wlosh.network

import `in`.jatindhankhar.wlosh.model.Response
import android.util.Log
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by jatin on 2/10/18.
 */
abstract class UnSplashClient   {
    private var mUnsplashService: UnsplashService = ServiceGenerator.create()

    fun fetchWallPapers(page:Int = 1, category: String ="")
    {
        Log.d("Yolo","Fetching wallpapers")

        mUnsplashService.processPhotos(page = page,category = category).enqueue( object : Callback<List<Response>>
        {

            override fun onFailure(call: Call<List<Response>>?, t: Throwable?) {
                onFetchWallpapersFailure()
            }

            override fun onResponse(call: Call<List<Response>>?, response: retrofit2.Response<List<Response>>?) {
                if(response != null && response.isSuccessful)
                {
                    response.body().let { onFetchWallpapersSuccess(it!!) }
                }
                else
                {
                    onFetchWallpapersFailure()
                }
            }

        });
    }

    abstract fun onFetchWallpapersFailure()

    abstract fun onFetchWallpapersSuccess(response: List<Response>)

}