package `in`.jatindhankhar.wlash.network

import `in`.jatindhankhar.wlash.model.Response
import `in`.jatindhankhar.wlash.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by jatin on 2/6/18.
 */
interface UnsplashClient {
@GET("photos/{category}")
fun processPhotos(@Path("category")  category: String = "",@Query("page") page: Int = 1,@Query("per_page") per_page: Int = 20) :Call<List<Response>>
}