package com.waykichain.wallet.client.impl

import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.client.BaasClient
import com.waykichain.wallet.client.BaasRetrofit
import com.waykichain.wallet.client.mSubscribe
import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.BlockHeight
import com.waykichain.wallet.model.baas.TxDetail
import com.waykichain.wallet.model.baas.parameter.AddressBean
import com.waykichain.wallet.model.baas.parameter.RawTx
import io.reactivex.disposables.CompositeDisposable


class BaasClientImpl(baseUrl: String) :BaasClient{
    private var baasRetrofit: BaasRetrofit?=null
    init{
        this.baasRetrofit =BaasRetrofit(baseUrl)
    }

    override fun getRegid(address: String): String? {
     var accointInfo:AccointInfo?=null
     baasRetrofit!!.apiService?.getAccountInfo(AddressBean(address)).mSubscribe {
        accointInfo=it
     }
        return accointInfo?.data?.regid
    }

    override fun getBlockHeight(): Long? {
        var blockHeight:BlockHeight?=null
        baasRetrofit!!.apiService?.getBlockCount().mSubscribe {
            blockHeight=it
        }
        return blockHeight?.data?.toLong()
    }

    override fun broadcastTransaction(transactions: WaykiTransactions):String? {
        var txDetail: TxDetail?=null
        baasRetrofit!!.apiService?.broadcastTransaction(RawTx(transactions.genRawTx())).mSubscribe {
            txDetail=it
        }
        return txDetail?.data?.txid
    }


    override val mDisposablePool: CompositeDisposable by lazy { CompositeDisposable() }

}