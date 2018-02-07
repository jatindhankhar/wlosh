package `in`.jatindhankhar.wlash.network

import `in`.jatindhankhar.wlash.model.Response
import `in`.jatindhankhar.wlash.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by jatin on 2/6/18.
 */
interface UnsplashClient {
@GET("photos/{category}")
fun processPhotos(@Path("category")  category: String = "") :Call<List<Response>>
}