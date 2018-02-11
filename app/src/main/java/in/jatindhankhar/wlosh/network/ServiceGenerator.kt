package `in`.jatindhankhar.wlosh.network

import `in`.jatindhankhar.wlosh.BuildConfig
import `in`.jatindhankhar.wlosh.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by jatin on 2/6/18.
 */
class ServiceGenerator {
    companion object {
        val retrofit = Retrofit.Builder().baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
        val httpClient = OkHttpClient.Builder()

        fun create(): UnsplashService {

            httpClient.addInterceptor { chain ->
                val urls = chain.request()?.url()?.newBuilder()?.addQueryParameter("client_id", BuildConfig.UNSPLASH_CLIENT_ID)?.build()
                val requests = chain.request()?.newBuilder()?.url(urls)?.build()
                chain.proceed(requests)
            }

            return retrofit.client(httpClient.build())
                    .build().create(UnsplashService::class.java)
        }
    }
}