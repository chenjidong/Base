package com.kuky.baselib

import com.kuky.baselib.baseUtils.LogUtils
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Kuky
 */
object RetrofitManager {
    private var CONN_TIME = 5L
    private var READ_TIME = 10L
    private var WRITE_TIME = 30L
    private var mRetrofit: Retrofit? = null

    fun provideClient(baseUrl: String, headers: HashMap<String, String>?): Retrofit {
        if (mRetrofit == null) {
            synchronized(RetrofitManager.javaClass) {
                if (mRetrofit == null) {
                    mRetrofit = Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .client(generateOkClient(headers))
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                }
            }
        }
        return mRetrofit!!
    }

    private fun generateOkClient(headers: HashMap<String, String>?): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            LogUtils.e("Interceptor Message => $message")
        })

        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .connectTimeout(CONN_TIME, TimeUnit.SECONDS)
                .readTimeout(READ_TIME, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor { chain ->
                    val builder = chain.request().newBuilder()
                    if (headers != null)
                        for (key in headers.keys)
                            builder.addHeader(key, headers[key]!!)
                    chain.proceed(builder.build())
                }
                .build()
    }
}