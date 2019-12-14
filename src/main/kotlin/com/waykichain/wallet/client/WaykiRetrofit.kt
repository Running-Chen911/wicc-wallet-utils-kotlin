package com.waykichain.wallet.client

import com.waykichain.wallet.client.api.BaasApiServce
import com.waykichain.wallet.client.api.NodeApiService

class BaasRetrofit(baseUrl:String) :ApiClientGenerator<BaasApiServce>(baseUrl,"") {
    override fun getApiService(): Class<BaasApiServce> {
        return BaasApiServce::class.java
    }
}

class NodeRetrofit(baseUrl:String,authToken:String) :ApiClientGenerator<NodeApiService>(baseUrl,authToken) {
    override fun getApiService(): Class<NodeApiService> {
        return NodeApiService::class.java
    }
}