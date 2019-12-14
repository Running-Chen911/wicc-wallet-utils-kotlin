package com.waykichain.wallet.client

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.parameter.BaseBean
import com.waykichain.wallet.model.node.rpc.RpcBaseBean
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class ApiClientGenerator<T>(baseUrl: String) {
    private val time_out: Long = 15//超时时间
    var apiService: T

    init {
        val httpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val authToken = Credentials.basic("wayki", "admin@123")
                    val builder = chain.request().newBuilder()
                             .header("Authorization", authToken)
                            .header("Accept", "application/json")
                    val build = builder.build()

                    chain.proceed(build)
                }
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
                .connectTimeout(time_out, TimeUnit.SECONDS)
                .readTimeout(time_out, TimeUnit.SECONDS)
                .build()

        apiService = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(getApiService())
    }

    abstract fun getApiService(): Class<T>
    private fun buildGson(): Gson {
        return GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()
    }

    fun getService(): T {
        return apiService
    }
}


fun <T : BaseBean> Observable<T>.baasSubscribe(
        onSuccess: (T) -> Unit) {
    this.subscribe(object : Observer<T> {
        override fun onComplete() {
        }
        override fun onSubscribe(d: Disposable) {

        }
        override fun onNext(t: T) {
            if (t.code == 0) {
                onSuccess.invoke(t)
            } else {
                if (!t.msg.isNullOrEmpty()) {
                    t.msg?.let {
                       throw Exception(t?.msg)
                    }
                } else {
                    throw Exception("NetWork Error")
                }
            }
        }
        override fun onError(e: Throwable) {
            throw e
        }
    })
}

fun <T : RpcBaseBean> Observable<T>.nodeSubscribe(
        onSuccess: (T) -> Unit) {
    this.subscribe(object : Observer<T> {
        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {

        }

        override fun onNext(t: T) {
            if (t.error == null) {
                onSuccess.invoke(t)
            } else {
                if (!t.error.message.isNullOrEmpty()) {
                    t.error.message?.let {
                        throw Exception(t?.error.message)
                    }
                } else {
                    throw Exception("NetWork Error")
                }
            }
        }

        override fun onError(e: Throwable) {
            throw e
        }
    })
}