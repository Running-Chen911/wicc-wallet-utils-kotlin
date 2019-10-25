package com.waykichain.wallet.transaction

import com.google.gson.Gson
import com.waykichain.wallet.client.ApiClientFactory
import com.waykichain.wallet.client.BaasClient
import com.waykichain.wallet.client.impl.BaasClientImpl
import com.waykichain.wallet.client.mSubscribe
import com.waykichain.wallet.model.baas.parameter.AddressBean
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

class WaykiChainTransaction {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var baasClient: BaasClient?=null

    @Before
    fun setup() {
        baasClient = ApiClientFactory.instance.newTestNetBaasClient()
    }

    @Test
    fun getAccountInfo() {
        val address="wLKf2NqwtHk3BfzK5wMDfbKYN1SC3weyR4"
        val accountInfo=  baasClient?.getAccountInfo(address)
        logger.info(accountInfo.toString())
    }

    /*
      * 账户注册交易,新版本已基本废弃，可改用公钥注册，免注册费用
      * Account registration transaction, the new version has been abandoned, you can use public key registration, free registration fee
       * fee Minimum 0.1 wicc
      * */
    @Test
    fun generateRegisterTx() {

    }
}