package com.waykichain.wallet.client.api

import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.parameter.AddressBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface BaasApiServce {

    //@Headers("Content-Type: application/json", "Accept: application/json")//需要添加头
    @POST("/v2/api//account/getaccountinfo")
    fun getAccountInfo(@Body body: AddressBean): Observable<AccointInfo>

}