package com.waykichain.wallet.transaction

import com.waykichain.wallet.Wallet
import com.waykichain.wallet.WalletManager
import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.client.ApiClientFactory
import com.waykichain.wallet.client.ApiClient
import com.waykichain.wallet.transaction.encode.params.BaseSignTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiDexCancelOrderTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiDexLimitTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiDexMarketTxParams
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory

class TestDexTransaction {

    private val logger = LoggerFactory.getLogger(javaClass)
    private var apiClient: ApiClient? = null
    private var wallet: Wallet? = null

    @Before
    fun setup() {
        apiClient = ApiClientFactory.instance.newTestNetBaasClient()
        wallet = WalletManager.init(NetWorkType.WAYKICHAIN_TESTNET).importWalletFromPrivateKey("Y6J4aK6Wcs4A3Ex4HXdfjJ6ZsHpNZfjaS4B9w7xqEnmFEYMqQd13")
    }

    /*
  * Dex 限价买单交易
  * Dex limit price transaction
  * fee Minimum 0.001 wicc/wusd,if no regid, fee Minimum 0.002 wicc/wusd
  * */
    @Test
    fun testDexBuyLimitTx() {
        val nValidHeight = apiClient?.getBlockHeight()
        val fee = 10000000L
        val srcRegId = apiClient?.getRegid(wallet?.address!!)
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val coinSymbol = CoinType.WUSD.type
        val assetSymbol = CoinType.WICC.type
        val assetAmount = 1 * 100000000L
        val bidPrice = 1 * 100000000L
        val txParams = WaykiDexLimitTxParams(nValidHeight!!, fee, srcRegId,
                feeSymbol, coinSymbol, assetSymbol, assetAmount, bidPrice, WaykiTxType.DEX_BUY_LIMIT_ORDER_TX)
        broadcastTransaction(txParams)
    }

    /*
    * Dex 限价卖单交易
    * Dex limit sell price transaction
    * fee Minimum 0.001 wicc/wusd,if no regid, fee Minimum 0.002 wicc/wusd
    * */
    @Test
    fun testDexSellLimitTx() {
        val nValidHeight = apiClient?.getBlockHeight()
        val fee = 10000000L
        val srcRegId = apiClient?.getRegid(wallet?.address!!)
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val coinSymbol = CoinType.WUSD.type
        val assetSymbol = CoinType.WICC.type
        val assetAmount = 1 * 100000000L
        val askPrice = 1 * 10000L
        val txParams = WaykiDexLimitTxParams(nValidHeight!!, fee, srcRegId,
                feeSymbol, coinSymbol, assetSymbol, assetAmount, askPrice, WaykiTxType.DEX_SELL_LIMIT_ORDER_TX)
        broadcastTransaction(txParams)
    }

    /*
     *  Dex 市价买单交易
     * Dex market buy price transaction
     * fee Minimum 0.001 wicc/wusd,if no regid, fee Minimum 0.002 wicc/wusd
    * */
    @Test
    fun testDexMarketBuyTx() {
        val nValidHeight = apiClient?.getBlockHeight()
        val fee = 10000000L
        val srcRegId = apiClient?.getRegid(wallet?.address!!)
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val coinSymbol = CoinType.WUSD.type
        val assetSymbol = CoinType.WICC.type
        val coinAmount = 1 * 100000000L
        val txParams = WaykiDexMarketTxParams(nValidHeight!!, fee, srcRegId,
                feeSymbol, coinSymbol, assetSymbol, coinAmount, WaykiTxType.DEX_BUY_MARKET_ORDER_TX)
        broadcastTransaction(txParams)
    }

    /*
     *  Dex 市价卖单交易
     * Dex market sell price transaction
     * fee Minimum 0.001 wicc/wusd,if no regid, fee Minimum 0.002 wicc/wusd
    * */
    @Test
    fun testDexMarketSellTx() {
        val nValidHeight = apiClient?.getBlockHeight()
        val fee = 10000000L
        val srcRegId = apiClient?.getRegid(wallet?.address!!)
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val coinSymbol = CoinType.WUSD.type
        val assetSymbol = CoinType.WICC.type
        val assetAmount = 100 * 100000000L
        val txParams = WaykiDexMarketTxParams(nValidHeight!!, fee, srcRegId,
                feeSymbol, coinSymbol, assetSymbol, assetAmount, WaykiTxType.DEX_SELL_MARKET_ORDER_TX)
        broadcastTransaction(txParams)
    }

    /*
    *  Dex 取消挂单交易
    * Dex cancel order tx
    * fee Minimum 0.001 wicc/wusd,if no regid, fee Minimum 0.002 wicc/wusd
    * */
    @Test
    fun testDexCancelOrderTx() {
        val nValidHeight = apiClient?.getBlockHeight()
        val fee = 10000000L
        val srcRegId = apiClient?.getRegid(wallet?.address!!)
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val dexOrderId = "009c0e665acdd9e8ae754f9a51337b85bb8996980a93d6175b61edccd3cdc144" //dex order tx hash
        val txParams = WaykiDexCancelOrderTxParams(nValidHeight!!, fee, srcRegId,
                feeSymbol, dexOrderId)
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
        logger.info("txid:" + txId)
    }

}