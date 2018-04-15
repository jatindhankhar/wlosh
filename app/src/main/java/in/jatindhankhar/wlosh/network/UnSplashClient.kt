package `in`.jatindhankhar.wlosh.network

import `in`.jatindhankhar.wlosh.model.DownloadLinkResponse
import `in`.jatindhankhar.wlosh.model.Response
import retrofit2.Call
import retrofit2.Callback

/**
 * Created by jatin on 2/10/18.
 */
abstract class UnSplashClient(accessKey: String) {
    private var mUnsplashService: UnsplashService = ServiceGenerator.create(accessKey)

    fun fetchWallPapers(page: Int = 1, category: String = "") {
        mUnsplashService.processPhotos(page = page, category = category).enqueue(object : Callback<List<Response>> {

            override fun onFailure(call: Call<List<Response>>?, t: Throwable?) {
                onFetchWallpapersFailure()
            }

            override fun onResponse(call: Call<List<Response>>?, response: retrofit2.Response<List<Response>>?) {
                if (response != null && response.isSuccessful) {
                    response.body().let { onFetchWallpapersSuccess(it!!) }
                } else {
                    onFetchWallpapersFailure()
                }
            }

        });
    }

    // Notify Unsplash that an image was downloaded
    fun notifyDownload(id: String) {
        mUnsplashService.pingDownloadPoint(id = id).enqueue(object : Callback<DownloadLinkResponse> {
            override fun onFailure(call: Call<DownloadLinkResponse>?, t: Throwable?) {
                onNotifyDownloadFailure()
            }

            override fun onResponse(call: Call<DownloadLinkResponse>?, response: retrofit2.Response<DownloadLinkResponse>?) {
                if (response != null && response.isSuccessful) {
                    response.body()?.let { onNotifyDownloadSuccess() }
                } else {
                    onNotifyDownloadFailure()
                }
            }

        })
    }

    abstract fun onFetchWallpapersFailure()

    abstract fun onFetchWallpapersSuccess(response: List<Response>)

    abstract fun onNotifyDownloadSuccess()

    abstract fun onNotifyDownloadFailure()

}