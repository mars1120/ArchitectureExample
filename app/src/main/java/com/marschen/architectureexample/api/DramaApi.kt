package com.marschen.architectureexample.api

import android.util.Log
import com.marschen.architectureexample.BuildConfig
import com.marschen.architectureexample.db.Drama
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


/**
 * API communication setup
 */
interface DramaApi {
    @GET("/interview/dramas-sample.json")
    fun getDramaSample(): Call<ListingResponse>

    data class ListingResponse(
        var data: List<Drama>
    )


    companion object {
        private const val BASE_URL = "https://static.linetv.tw/"
        fun create(): DramaApi = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): DramaApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                if (BuildConfig.DEBUG) Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DramaApi::class.java)
        }
    }
}