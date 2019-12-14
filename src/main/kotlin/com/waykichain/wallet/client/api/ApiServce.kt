package com.waykichain.wallet.client.api

import com.waykichain.wallet.model.node.rpc.RpcBean
import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.BlockHeight
import com.waykichain.wallet.model.baas.TxDetail
import com.waykichain.wallet.model.baas.parameter.AddressBean
import com.waykichain.wallet.model.baas.parameter.RawTx
import com.waykichain.wallet.model.node.rpc.BlockCountBean
import com.waykichain.wallet.model.node.rpc.RpcAccountInfoBean
import com.waykichain.wallet.model.node.rpc.SubmitTxRawBean
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface NodeApiService {

    @POST("/")
    fun getAccountInfo(@Body body: RpcBean): Observable<RpcAccountInfoBean>

    @POST("/")
    fun getBlockCount(@Body body: RpcBean): Observable<BlockCountBean>

    @POST("/")
    fun broadcastTransaction(@Body body: RpcBean): Observable<SubmitTxRawBean>
}

interface BaasApiServce {

    @POST("/v2/api/account/getaccountinfo")
    fun getAccountInfo(@Body body: AddressBean): Observable<AccointInfo>

    @POST("/v2/api/block/getblockcount")
    fun getBlockCount(): Observable<BlockHeight>

    @POST("/v2/api/transaction/sendrawtx")
    fun broadcastTransaction(@Body body: RawTx): Observable<TxDetail>
}