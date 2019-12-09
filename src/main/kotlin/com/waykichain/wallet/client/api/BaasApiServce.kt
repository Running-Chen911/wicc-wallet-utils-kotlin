package com.waykichain.wallet.client.api

import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.BlockHeight
import com.waykichain.wallet.model.baas.TxDetail
import com.waykichain.wallet.model.baas.parameter.AddressBean
import com.waykichain.wallet.model.baas.parameter.RawTx
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface BaasApiServce {

    @POST("/v2/api/account/getaccountinfo")
    fun getAccountInfo(@Body body: AddressBean): Observable<AccointInfo>


    @POST("/v2/api/block/getblockcount")
    fun getBlockCount(): Observable<BlockHeight>

    @POST("/v2/api/transaction/sendrawtx")
    fun broadcastTransaction(@Body body: RawTx): Observable<TxDetail>
}