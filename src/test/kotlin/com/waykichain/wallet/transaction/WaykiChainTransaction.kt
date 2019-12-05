package com.waykichain.wallet.transaction

import com.waykichain.wallet.Wallet
import com.waykichain.wallet.WalletManager
import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.client.ApiClientFactory
import com.waykichain.wallet.client.BaasClient
import com.waykichain.wallet.transaction.params.WaykiRegisterAccountTxParams
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

class WaykiChainTransaction {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var baasClient: BaasClient?=null
    private var wallet:Wallet?=null

    @Before
    fun setup() {
        baasClient = ApiClientFactory.instance.newTestNetBaasClient()
        wallet=WalletManager.init(NetWorkType.WAYKICHAIN_TESTNET).importWalletFromPrivateKey("Y6J4aK6Wcs4A3Ex4HXdfjJ6ZsHpNZfjaS4B9w7xqEnmFEYMqQd13")
    }

    @Test
    fun getAccountInfo() {
        val address="WkMhG5aLEexLUFv7AWzhuFQ1FQxyFtpEQr"
        val accountInfo=  baasClient?.getAccountInfo(address)
        val redId=accountInfo?.data?.regid
        logger.info(redId)
    }

    /*
      * 账户注册交易,新版本已基本废弃，可改用公钥注册，免注册费用
      * Account registration transaction, the new version has been abandoned, you can use public key registration, free registration fee
       * fee Minimum 0.1 wicc
      * */
    @Test
    fun generateRegisterTx() {
        val txParams = WaykiRegisterAccountTxParams(429821, 10000000)
        var transaction=WaykiTransactions(txParams,wallet!!)
        var rawTxAsHex=transaction.genRawTx()//生成冷签名
        logger.info("rawTx:"+rawTxAsHex)
    }
}