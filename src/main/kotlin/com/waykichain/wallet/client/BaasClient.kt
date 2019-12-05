package com.waykichain.wallet.client

import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.BlockHeight
import io.reactivex.Observable

interface BaasClient :BaseClient{
    fun getAccountInfo(address: String): AccointInfo?
    fun getBlockHeight(): BlockHeight?
}