package com.waykichain.wallet.client

import com.waykichain.wallet.model.baas.AccointInfo
import com.waykichain.wallet.model.baas.parameter.AddressBean
import io.reactivex.Observable

interface BaasClient :BaseClient{
    fun getAccountInfo(body: AddressBean): Observable<AccointInfo>
}