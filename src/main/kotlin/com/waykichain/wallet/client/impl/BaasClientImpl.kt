package com.waykichain.wallet.client.impl

import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.client.ApiClient
import com.waykichain.wallet.client.BaasRetrofit
import com.waykichain.wallet.client.baasSubscribe
import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.BlockHeight
import com.waykichain.wallet.model.baas.TxDetail
import com.waykichain.wallet.model.baas.parameter.AddressBean
import com.waykichain.wallet.model.baas.parameter.RawTx


class BaasClientImpl(baseUrl: String) :ApiClient{
    private var waykiRetrofit: BaasRetrofit?=null
    init{
        this.waykiRetrofit =BaasRetrofit(baseUrl)
    }

    override fun getRegid(address: String): String? {
     var accointInfo:AccointInfo?=null
     waykiRetrofit!!.apiService?.getAccountInfo(AddressBean(address)).baasSubscribe {
        accointInfo=it
     }
        return accointInfo?.data?.regid
    }

    override fun getBlockHeight(): Long? {
        var blockHeight:BlockHeight?=null
        waykiRetrofit!!.apiService?.getBlockCount().baasSubscribe {
            blockHeight=it
        }
        return blockHeight?.data?.toLong()
    }

    override fun broadcastTransaction(transactions: WaykiTransactions):String? {
        var txDetail: TxDetail?=null
        waykiRetrofit!!.apiService?.broadcastTransaction(RawTx(transactions.genRawTx())).baasSubscribe {
            txDetail=it
        }
        return txDetail?.data?.txid
    }

}