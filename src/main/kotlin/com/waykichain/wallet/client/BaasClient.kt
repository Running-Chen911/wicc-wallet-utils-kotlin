package com.waykichain.wallet.client

import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.BlockHeight
import io.reactivex.Observable

interface BaasClient :BaseClient{
    fun getRegid(address: String): String?
    fun getBlockHeight(): Long?
    fun broadcastTransaction(transactions: WaykiTransactions):String?
}