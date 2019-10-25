package com.waykichain.wallet.client

import com.waykichain.wallet.model.baas.AccointInfo
import io.reactivex.Observable

interface BaasClient :BaseClient{
    fun getAccountInfo(address: String): AccointInfo?
}