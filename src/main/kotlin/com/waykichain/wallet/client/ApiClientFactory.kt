package com.waykichain.wallet.client

import com.waykichain.wallet.client.impl.BaasClientImpl
import com.waykichain.wallet.client.impl.NodeClientImpl

class ApiClientFactory {

    private object Holder {
        val INSTANCE = ApiClientFactory()
    }

    companion object {
        val instance: ApiClientFactory by lazy { Holder.INSTANCE }
    }

    fun newTestNetBaasClient(): ApiClient {
        return newBaasClient(ChainEnvirment.TESTNET.baasUrl)
    }

    fun newMainNetBaasClient(): ApiClient {
        return newBaasClient(ChainEnvirment.MAINNET.baasUrl)
    }

    private fun newBaasClient(baseUrl: String): ApiClient {
        return BaasClientImpl(baseUrl)
    }

    fun newTestNetNodeClient(authToken:String): ApiClient {
        return newNodeClient(ChainEnvirment.TESTNET.nodeUrl,authToken)
    }

    fun newMainNetNodeClient(authToken:String): ApiClient {
        return newNodeClient(ChainEnvirment.MAINNET.nodeUrl,authToken)
    }

    private fun newNodeClient(baseUrl: String,authToken:String): ApiClient {
        return NodeClientImpl(baseUrl,authToken)
    }
}