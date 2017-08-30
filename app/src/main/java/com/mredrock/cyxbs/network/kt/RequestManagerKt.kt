package com.mredrock.cyxbs.network.kt

import com.mredrock.cyxbs.BuildConfig
import com.mredrock.cyxbs.config.Const
import com.mredrock.cyxbs.network.interceptor.StudentNumberInterceptor
import com.mredrock.cyxbs.network.setting.QualifiedTypeConverterFactory
import io.reactivex.rxkotlin.OnErrorNotImplementedException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by mingyang.zeng on 2017/8/29.
 */

object RequestManagerKt {

    private const val DEFAULT_TIMEOUT = 30
    private lateinit var api: RedrockApiServiceKt
    private lateinit var client: OkHttpClient

    private val onNextStub: (Any?) -> Unit = {}
    private val onErrorStub: (Throwable) -> Unit = { throw OnErrorNotImplementedException(it) }
    private val onCompleteStub: () -> Unit = {}

    fun init() {
        client = configureOkHttp(OkHttpClient.Builder())
        val retrofit = Retrofit.Builder()
                .baseUrl(Const.END_POINT_REDROCK)
                .client(client)
                .addConverterFactory(QualifiedTypeConverterFactory(
                        GsonConverterFactory.create(), SimpleXmlConverterFactory.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        api = retrofit.create(api::class.java)
    }

    private fun configureOkHttp(builder: OkHttpClient.Builder): OkHttpClient {
        builder.connectTimeout(DEFAULT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }
        builder.addInterceptor(StudentNumberInterceptor())
        return builder.build()
    }
}
