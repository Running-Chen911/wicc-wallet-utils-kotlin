package com.waykichain.wallet.transaction

import com.waykichain.wallet.Wallet
import com.waykichain.wallet.WalletManager
import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.client.ApiClientFactory
import com.waykichain.wallet.client.ApiClient
import com.waykichain.wallet.transaction.encode.params.BaseSignTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiCdpLiquidateTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiCdpRedeemTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiCdpStakeTxParams
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

class TestCdpTransaction {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var apiClient: ApiClient? = null
    private var wallet: Wallet? = null

    @Before
    fun setup() {
        apiClient = ApiClientFactory.instance.newTestNetBaasClient()
        wallet = WalletManager.init(NetWorkType.WAYKICHAIN_TESTNET).importWalletFromPrivateKey("Y6J4aK6Wcs4A3Ex4HXdfjJ6ZsHpNZfjaS4B9w7xqEnmFEYMqQd13")
    }

    /*
    * 创建,追加cdp交易
    * Create or append an  cdp transaction
    * fee Minimum 0.001 wicc
    * */
    @Test
    fun testGenerateCdpStakeTx() {
        val nValidHeight = apiClient?.getBlockHeight()//获得区块高度
        val fee = 10000000L
        val srcRegId = apiClient?.getRegid(wallet?.address!!)
        val cdpTxid = "" //wallet cdp create tx hash
        val feeSymbol = CoinType.WUSD.type  //fee symbol
        val bCoinSymbol = CoinType.WICC.type //stake coin symbol
        val sCoinSymbol = CoinType.WUSD.type  // get coind symbol
        val bCoinToStake = 10000000000L  //stake amount WICC
        val sCoinToMint = 60000000L   //get amount WUSD
        val map = mapOf<String, Long>(Pair(bCoinSymbol, bCoinToStake))
        val txParams = WaykiCdpStakeTxParams(nValidHeight!!, fee, srcRegId, cdpTxid, feeSymbol, map, sCoinSymbol, sCoinToMint)
        broadcastTransaction(txParams)
    }

    /*
    * 赎回cdp交易
    * Redeem cdp transaction
    * fee Minimum 0.001 wicc
    * */
    @Test
    fun testRedeemCdpTx() {
        val nValidHeight = apiClient?.getBlockHeight()
        val fee = 10000000L
        val srcRegId = apiClient?.getRegid(wallet?.address!!)
        val cdpTxid = "009c0e665acdd9e8ae754f9a51337b85bb8996980a93d6175b61edccd3cdc144" //wallet cdp create tx hash
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val sCoinsToRepay = 50000000L  //repay amount
        val redeemSymbol = CoinType.WICC.type  //redeem asset symbol
        val bCoinsToRedeem = 100000000L   //redeem amount
        val map = mapOf<String, Long>(Pair(redeemSymbol, bCoinsToRedeem));
        val txParams = WaykiCdpRedeemTxParams(nValidHeight!!, fee, srcRegId, cdpTxid, feeSymbol, sCoinsToRepay, map)
        broadcastTransaction(txParams)
    }

    /*
    * 清算cdp交易
    * Liquidate cdp transaction
    * fee Minimum 0.001 wicc
    * */
    @Test
    fun testLiquidateCdpTx() {
        val nValidHeight = apiClient?.getBlockHeight()
        val fee = 10000000L
        val srcRegId = apiClient?.getRegid(wallet?.address!!)
        val cdpTxid = "009c0e665acdd9e8ae754f9a51337b85bb8996980a93d6175b61edccd3cdc144" //wallet cdp create tx hash
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val sCoinsToLiquidate = 10000000L  //Liquidate amount
        val liquidateAssetSymbol = CoinType.WICC.type  //Asset symbol

        val txParams = WaykiCdpLiquidateTxParams(nValidHeight!!, fee, srcRegId, cdpTxid, feeSymbol, sCoinsToLiquidate, liquidateAssetSymbol)
        broadcastTransaction(txParams)
    }

    /*
  * 广播交易
  * broadcast Transaction
  * */
    fun broadcastTransaction(txParams: BaseSignTxParams) {
        var transaction = WaykiTransactions(txParams, wallet!!)

        var rawTxAsHex = transaction.genRawTx()//生成冷签名
        logger.info("raw tx as hex:" + rawTxAsHex)

        val txId = apiClient?.broadcastTransaction(transaction)
        logger.info("txId:" + txId)
    }

}