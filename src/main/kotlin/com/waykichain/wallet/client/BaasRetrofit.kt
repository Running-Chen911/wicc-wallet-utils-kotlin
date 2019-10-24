package com.waykichain.wallet.client

import com.waykichain.wallet.client.api.BaasApiServce

class BaasRetrofit(baseUrl:String) :ApiClientGenerator<BaasApiServce>(baseUrl) {

    override fun getApiService(): Class<BaasApiServce> {
        return BaasApiServce::class.java
    }

}