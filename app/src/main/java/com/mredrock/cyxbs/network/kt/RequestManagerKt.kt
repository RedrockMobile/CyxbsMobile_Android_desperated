package com.mredrock.cyxbs.network.kt

/**
 * Created by mingyang.zeng on 2017/8/29.
 */

object RequestManagerKt {

/*    private const val DEFAULT_TIMEOUT = 30
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
    }*/
}
