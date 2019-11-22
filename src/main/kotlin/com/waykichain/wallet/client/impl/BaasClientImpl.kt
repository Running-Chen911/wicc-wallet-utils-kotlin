package com.waykichain.wallet.client.impl

import com.waykichain.wallet.client.BaasClient
import com.waykichain.wallet.client.BaasRetrofit
import com.waykichain.wallet.client.mSubscribe
import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.parameter.AddressBean
import io.reactivex.disposables.CompositeDisposable


class BaasClientImpl(baseUrl: String) :BaasClient{

    private var baasRetrofit: BaasRetrofit?=null
    init{
        this.baasRetrofit =BaasRetrofit(baseUrl)
    }

    override fun getAccountInfo(address: String): AccointInfo? {
     var accointInfo:AccointInfo?=null
     baasRetrofit!!.apiService?.getAccountInfo(AddressBean(address)).mSubscribe {
        accointInfo=it
     }
        return accointInfo
    }

    override val mDisposablePool: CompositeDisposable by lazy { CompositeDisposable() }

}