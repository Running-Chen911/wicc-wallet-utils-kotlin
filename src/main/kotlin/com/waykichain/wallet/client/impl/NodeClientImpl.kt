package com.waykichain.wallet.client.impl

import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.client.ApiClient
import com.waykichain.wallet.client.NodeRetrofit
import com.waykichain.wallet.client.nodeSubscribe
import com.waykichain.wallet.model.node.rpc.RpcBean
import com.waykichain.wallet.model.node.rpc.BlockCountBean
import com.waykichain.wallet.model.node.rpc.RpcAccountInfoBean
import com.waykichain.wallet.model.node.rpc.SubmitTxRawBean
import java.util.ArrayList

class NodeClientImpl (baseUrl: String,authToken:String) :ApiClient{
    private var waykiRetrofit: NodeRetrofit?=null
    init{
        this.waykiRetrofit = NodeRetrofit(baseUrl,authToken)
    }

    override fun getRegid(address: String): String? {
        var accointInfo: RpcAccountInfoBean?=null
        val bean=RpcBean()
        bean.method="getaccountinfo"
        bean.params= ArrayList<Any>()
        bean.params?.add(address)
        waykiRetrofit!!.apiService?.getAccountInfo(bean).nodeSubscribe {
            accointInfo=it
        }
        return accointInfo?.result?.regid
    }

    override fun getBlockHeight(): Long? {
        var blockHeight: BlockCountBean?=null
        val bean=RpcBean()
        bean.method="getblockcount"
        waykiRetrofit!!.apiService?.getBlockCount(bean).nodeSubscribe {
            blockHeight=it
        }
        return blockHeight?.result.toString().toLong()
    }

    override fun broadcastTransaction(transactions: WaykiTransactions):String? {
        var txDetail: SubmitTxRawBean?=null
        val bean=RpcBean()
        bean.method="submittxraw"
        bean.params= ArrayList<Any>()
        bean.params?.add(transactions?.genRawTx())
        waykiRetrofit!!.apiService?.broadcastTransaction(bean).nodeSubscribe {
            txDetail=it
        }
        return txDetail?.result?.txid
    }

}