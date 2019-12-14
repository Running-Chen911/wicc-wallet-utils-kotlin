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

    fun newTestNetNodeClient(): ApiClient {
        return newNodeClient(ChainEnvirment.TESTNET.nodeUrl)
    }

    private fun newNodeClient(baseUrl: String): ApiClient {
        return NodeClientImpl(baseUrl)
    }
}