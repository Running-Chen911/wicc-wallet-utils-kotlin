package com.waykichain.wallet.transaction

import com.waykichain.wallet.Wallet
import com.waykichain.wallet.WalletManager
import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.client.ApiClientFactory
import com.waykichain.wallet.client.ApiClient
import com.waykichain.wallet.encode.CAsset
import com.waykichain.wallet.encode.OperVoteFund
import com.waykichain.wallet.encode.UCoinDest
import com.waykichain.wallet.transaction.encode.params.*
import com.waykichain.wallet.util.ContractUtil
import okhttp3.Credentials
import org.bitcoinj.core.Utils
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File

class TestCommonTransaction {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var apiClient: ApiClient? = null
    private var wallet: Wallet? = null

    @Before
    fun setup() {
        /*
        * 连接接节点
        * */
        //val authToken = Credentials.basic("wayki", "admin@123") //需要提供节点rpc 用户名和密码
        //apiClient = ApiClientFactory.instance.newTestNetNodeClient(authToken)
        /*
        * 连接baas
        * */
        apiClient = ApiClientFactory.instance.newTestNetBaasClient()
        wallet = WalletManager.init(NetWorkType.WAYKICHAIN_TESTNET).importWalletFromPrivateKey("Y6J4aK6Wcs4A3Ex4HXdfjJ6ZsHpNZfjaS4B9w7xqEnmFEYMqQd13")
    }

    /*
    * 获得钱包regid
    * get wallet regid
    * */
    @Test
    fun getAccountInfo() {
        val address = "WkMhG5aLEexLUFv7AWzhuFQ1FQxyFtpEQr"
        val regid = apiClient?.getRegid(address)
        logger.info(regid)
    }

    /*
   * 获得区块高度
   * get block height
   * */
    @Test
    fun getBlockHeight() {
        val blockHeight = apiClient?.getBlockHeight()
        logger.info(blockHeight.toString())
    }

    /*
      * 账户注册交易,新版本已基本废弃，可改用公钥注册，免注册费用
      * Account registration transaction, the new version has been abandoned, you can use public key registration, free registration fee
       * fee Minimum 0.1 wicc
      * */
    @Test
    fun generateRegisterTx() {
        val blockHeight = apiClient?.getBlockHeight()
        val txParams = WaykiRegisterAccountTxParams(blockHeight!!, 10000000)
        var transaction = WaykiTransactions(txParams, wallet!!)
        var rawTxAsHex = transaction.genRawTx()//生成冷签名
        logger.info("rawTx:" + rawTxAsHex)
        val txId = apiClient?.broadcastTransaction(transaction)
        logger.info("txId:" + txId)
    }

    /*
    * 转账交易
    * transfer
    * fee Minimum 0.1 wicc
    * */
    @Test
    fun testGenerateCommonTxForTestNet() {
        val blockHeight = apiClient?.getBlockHeight()
        val regid = apiClient?.getRegid(wallet?.address!!)
        val destAddr = "wLKf2NqwtHk3BfzK5wMDfbKYN1SC3weyR4"
        val memo = "test transfer"
        val txParams = WaykiCommonTxParams(blockHeight!!, 100000000,
                100000000, regid, destAddr, memo)
        broadcastTransaction(txParams)
    }

    /*
    * 合约调用交易
    * Contract transaction sample
    * fee Minimum 0.001 wicc
    * */
    @Test
    fun testGenerateContractTx() {
        //WRC20 Transfer
        val regId = apiClient?.getRegid(wallet?.address!!)
        val nValidHeight = apiClient?.getBlockHeight()
        val appId = "128711-1"
        val wrc20Amount = 100000000L // transfer 10000 WRC
        val destAddress = "wNPWqv9bvFCnMm1ddiQdH7fUwUk2Qgrs2N"
        val contractByte = ContractUtil.transferWRC20Contract(wrc20Amount, destAddress)
        val txParams = WaykiContractTxParams(nValidHeight!!, 1000000, 0, regId, appId, contractByte)
        broadcastTransaction(txParams)
    }

    /*
    * 多币种转账交易 ,支持多种币种转账
    * Test nUniversal Coin Transfer Tx
    * fee Minimum 0.001 wicc
    * */
    @Test
    fun testGenerateUCoinTransferTx() {
        val nValidHeight = apiClient?.getBlockHeight()
        val coinSymbol = CoinType.WICC.type  //coind symbol
        val coinAmount = 10000L    //transfer amount
        val feeSymbol = CoinType.WICC.type
        val fees = 1000000L
        val regid = apiClient?.getRegid(wallet?.address!!)
        val memo = "转账"
        val dest1 = UCoinDest("wLKf2NqwtHk3BfzK5wMDfbKYN1SC3weyR4", coinSymbol, coinAmount)
        val dests = arrayListOf<UCoinDest>(dest1)
        val txParams = WaykiUCoinTxParams(nValidHeight!!, regid, dests.toList(), feeSymbol, fees, memo)
        broadcastTransaction(txParams)
    }

    /*
    * 多币种合约调用交易
    * Contract transaction sample
    * fee Minimum 0.01 wicc
    * */
    @Test
    fun testGenerateUCoinContractTx() {
        val value = 1000000000L
        val appid = "392366-3"
        val regid = apiClient?.getRegid(wallet?.address!!)
        val nValidHeight = apiClient?.getBlockHeight()
        val contractByte = ContractUtil.hexString2binaryString("f001")
        val txParams = WaykiUCoinContractTxParams(nValidHeight!!,
                1000000, value, regid,
                appid, contractByte, CoinType.WUSD.type, CoinType.WUSD.type)
        broadcastTransaction(txParams)
    }

    /*
    * 合约发布交易
    * Deploy Contract transaction sample
    * fee Minimum 1.1 wicc
    * */
    @Test
    fun testDeployContractTx() {
        val regid = apiClient?.getRegid(wallet?.address!!)
        val nValidHeight = apiClient?.getBlockHeight()
        val file = File("hello.lua")
        val contractByte = file.readBytes();
        val description = "description script"
        val txParams = WaykiDeployContractTxParams(nValidHeight!!, 1100000000, regid!!,
                contractByte, description)
        broadcastTransaction(txParams)
    }

    /*
    * 投票交易
    * Voting transaction
    * fee Minimum 0.01 wicc
    * */
    @Test
    fun testGenerateDelegateTx() {
        //VoteOperType.ADD_FUND  投票
        //VoteOperType.MINUS_FUND //撤销投票
        val regid = apiClient?.getRegid(wallet?.address!!)
        val nValidHeight = apiClient?.getBlockHeight()
        val array1 = OperVoteFund(VoteOperType.ADD_FUND.value,
                Utils.HEX.decode("036c5397f3227a1e209952829d249b7ad0f615e43b763ac15e3a6f52627a10df21"),
                100000000)
       val array2 = OperVoteFund(VoteOperType.ADD_FUND.value,
                Utils.HEX.decode("036c5397f3227a1e209952829d249b7ad0f615e43b763ac15e3a6f52627a10df21"),
                100000000)
        val array = arrayOf(array1,array2)
        val txParams = WaykiDelegateTxParams(regid!!, array, 10000000, nValidHeight!!)
       broadcastTransaction(txParams)
    }

    /*
    * 资产发布
    * Asset release
    * symbol 大写字母A-Z 1-7 位 [A_Z]
    * Symbol Capital letter A-Z 1-7 digits [A_Z]
    * fee Minimum 0.01 wicc
    * account - 550 wicc
    * */
    @Test
    fun testCAssetIssueTx() {
        val regid = apiClient?.getRegid(wallet?.address!!)
        val nValidHeight = apiClient?.getBlockHeight()
        val fee = 1000000L
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val symbol = "STOOOOO"
        val asset = CAsset(symbol, "0-1", "SS TOKEN", 1000000000000000, true)
        val txParams = WaykiAssetIssueTxParams(nValidHeight!!, fee, regid!!,
                feeSymbol, asset)
        broadcastTransaction(txParams)
    }

    /*
    * 资产发布
    * Asset Update
    * asset_symbol 大写字母A-Z 1-7 位 [A_Z]
    * Symbol Capital letter A-Z 1-7 digits [A_Z]
    * fee Minimum 0.001 wicc
    * account - 110 wicc
    * */
    @Test
    fun testCAssetUpdateTx() {
        val fee = 1000000L
        val regid = apiClient?.getRegid(wallet?.address!!)
        val nValidHeight = apiClient?.getBlockHeight()
        val feeSymbol = CoinType.WICC.type  //fee symbol
        val asset = AssetUpdateData(AssetUpdateType.OWNER_UID, "0-2")  //update asset owner
        // val asset=AssetUpdateData(AssetUpdateType.NAME,"TestCoin") // update asset name
        //val asset=AssetUpdateData(AssetUpdateType.MINT_AMOUNT,200000000L) //update asset number
        val txParams = WaykiAssetUpdateTxParams(nValidHeight!!, fee, regid!!,
                feeSymbol, "STOOOOO", asset)
        broadcastTransaction(txParams)
    }


    /*
    * 广播交易
    * broadcast Transaction
    * */
    fun broadcastTransaction(txParams: BaseSignTxParams) {
        var transaction = WaykiTransactions(txParams, wallet!!)

        var rawTxAsHex = transaction.genRawTx()//generate rawtx
        logger.info("raw tx as hex:" + rawTxAsHex)

        val txId = apiClient?.broadcastTransaction(transaction)
        logger.info("txId:" + txId)
    }

}