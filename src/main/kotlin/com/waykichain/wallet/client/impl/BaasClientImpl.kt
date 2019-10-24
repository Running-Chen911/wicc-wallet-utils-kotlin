package com.waykichain.wallet.client.impl

import com.waykichain.wallet.client.ApiClientFactory
import com.waykichain.wallet.client.BaasClient
import com.waykichain.wallet.client.BaasRetrofit
import com.waykichain.wallet.client.api.BaasApiServce
import com.waykichain.wallet.client.mSubscribe
import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.parameter.AddressBean
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable


class BaasClientImpl(baseUrl: String) :BaasClient{

    private var baasRetrofit: BaasRetrofit?=null
    init{
        this.baasRetrofit =BaasRetrofit(baseUrl)
    }

    override fun getAccountInfo(body: AddressBean): Observable<AccointInfo> {
     return  baasRetrofit!!.apiService?.getAccountInfo(body)
    }

    override val mDisposablePool: CompositeDisposable by lazy { CompositeDisposable() }

}