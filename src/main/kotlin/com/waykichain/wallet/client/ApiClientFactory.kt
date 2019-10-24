package com.waykichain.wallet.client

import com.waykichain.wallet.client.impl.BaasClientImpl

class ApiClientFactory {

    private object Holder {
        val INSTANCE = ApiClientFactory()
    }

    companion object {
        val instance: ApiClientFactory by lazy { Holder.INSTANCE }
    }

    fun newTestNetBaasClient(): BaasClient {
        return newBaasClient(ChainEnvirment.TESTNET.baasUrl)
    }

    fun newMainNetBaasClient(): BaasClient {
        return newBaasClient(ChainEnvirment.MAINNET.baasUrl)
    }

    private fun newBaasClient(baseUrl: String): BaasClient {
        return BaasClientImpl(baseUrl)
    }

}