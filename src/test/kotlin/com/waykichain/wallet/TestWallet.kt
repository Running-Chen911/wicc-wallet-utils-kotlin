/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2018 The Waykichain Core developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 */

package com.waykichain.wallet

import com.waykichain.wallet.transaction.Language
import com.waykichain.wallet.transaction.NetWorkType
import com.waykichain.wallet.transaction.decode.params.WaykiSignMsgParams
import com.waykichain.wallet.transaction.decode.params.WaykiVerifyMsgSignParams
import com.waykichain.wallet.transaction.encode.params.WaykiMainNetParams
import com.waykichain.wallet.transaction.encode.params.WaykiTestNetParams
import org.bitcoinj.core.*
import org.junit.Before
import org.junit.Test
import org.slf4j.LoggerFactory


/**
 * @Author: Richard Chen
 * @Date 2018/08/29 下午6:27
 */

class TestWallet {

    private val logger = LoggerFactory.getLogger(javaClass)
    private var walletManager:WalletManager?=null

    @Before
    fun setup(){
        walletManager=WalletManager.init(NetWorkType.WAYKICHAIN_TESTNET)
    }

    /*
    * 生成助记词 generate Mnemonic
    * Language.ENGLISH
    * Language.CHINESE
    * */
    @Test
    fun generateMnemonic() {
       var words= walletManager?.randomMnemonic(Language.CHINESE)

        logger.info(words.toString())
    }

    /*
    * 助记词生成维基链钱包
    * Mnemonic to WaykiChain Wallet
    * */
    @Test
    fun generateWalletFromMnemonic() {
        val mn = "星 交 直 成 开 冯 姑 迁 消 板 沙 晶"
        //"hope cause blade aerobic artwork velvet ocean unfold current engine group inner"
        val words = mn.split(" ")
        val passphrase="" // 修改不同的passphrase 生成不同私钥 默认passphrase为空
        val bip44Path="/0'/0/0" //  通过修改bip44层级生成不同私钥  / account' / change / address_index
        val wallet = walletManager?.importWalletFromMnemonic(words,passphrase,bip44Path)
        assert("wMdciD4Zd2YW8NqJDjs3VwMm3ERRSAMMNA".equals(wallet?.address))
        logger.info(wallet.toString())
    }

    /*
       * 私钥生成维基链钱包 PrivateKey to WaykiChain Wallet
       * */
    @Test
    fun testImportPrivKey() {
       /* val privKeyWiF = "Y7UiVRpTAZNDtZakSHZwebHD6romu9jcuj1tjjujzwbSqdKLCEQZ"//"YBb6tdJvQyD8VwxJ4HUjDfpcpmFc359uGFQLbegaaKr6FJY863iw"//"YAHcraeGRDpvwBWVccV7NLGAU6uK39nNUTip8srbJSu6HKSTfDcC"
        val wallet = walletManager?.importWalletFromPrivateKey(privKeyWiF)
        assert("wZCst8wFgxiaNptqhheMvRugdngMJMZAKL".equals(wallet?.address))
        logger.info(wallet.toString())*/

        val pubkey="03c89c66ee32e26ee2c1bf624dc01d6d3e8eb9a09d0a0c86383944871054c1fcc6"
        val ecKey=ECKey.fromPublicOnly(Utils.HEX.decode(pubkey))
         val  address1 = LegacyAddress.fromKey(WaykiMainNetParams.instance,ecKey).toBase58()
        logger.info(address1)
        val ecKey2=ECKey.fromPublicOnly(Utils.HEX.decode( "021be050c7e67004dc494f52ca81ff7c100a7e8b527b1c5c18091c3ad7065c4d94"))
        val  address2 = LegacyAddress.fromKey(WaykiTestNetParams.instance,ecKey2).toBase58()
        logger.info(address2)
        val ecKey3=ECKey.fromPublicOnly(Utils.HEX.decode("033f51c7ef38ee34d1fe436dbf6329821d1863f22cee69c281c58374dcb9c35569"))
        val  address3 = LegacyAddress.fromKey(WaykiTestNetParams.instance,ecKey3).toBase58()
        logger.info(address3)
    }

    @Test
    fun checkWalletAddress(){
        val netParams = WaykiTestNetParams.instance //网络类型 （WaykiMainNetParams.instance 正式网）
        val address="wZCst8wFgxiaNptqhheMvRugdngMJMZAKL" //维基钱包地址
        try {
         val legacyAddress= LegacyAddress.fromBase58(netParams,address) //地址检测
        }catch (e:Exception){
          e.printStackTrace()
        }
    }

    @Test
    fun testSignMessage() {
        //地址:wX7cC6qK6RQCLShCevpeciqQaQNEtqLRa8
        //钱包地址对应的私钥:Y8WXc3RYw4TRxdGEpTLPd5GR7VrsAvRgCdiZMZakwFyVST1P7NnC
        //公钥:034edcac8efda301a0919cdf2feeb0376bfcd2a1a29b5d094e5e9ce7a580c82fcc (压缩后)
        val msg = "WaykiChain" //原始数据,由开发者后台生成传给前端,生成规则由开发者自己决定
        val privateKey = "Y8WXc3RYw4TRxdGEpTLPd5GR7VrsAvRgCdiZMZakwFyVST1P7NnC"
        val wallet = walletManager?.importWalletFromPrivateKey(privateKey)
        val msgParams = WaykiSignMsgParams(msg)
        msgParams.signatureMsg(wallet?.ecKey!!)
        val signResult = msgParams.serializeSignature()
        logger.info("\nsignResult.publicKey: ${signResult.publicKey} \nsignResult.signature: ${signResult.signature} \n")
    }

    @Test
    fun testVerifyMsgSignature() {

        val signature = "3044022024fafdf62a8414ad28c96354cc310daffee04e8ad46276420bdaafe1aa35091e02205b2c1b1a1e7fe97a74f2e3dc16f790a28cafea2ec40911fd40cff856899a851e"
        val publicKey = "034edcac8efda301a0919cdf2feeb0376bfcd2a1a29b5d094e5e9ce7a580c82fcc"
        val msg = "WaykiChain"

        val netParams = WaykiTestNetParams.instance
        //  val netParams = WaykiMainNetParams.instance
        val msgParams = WaykiVerifyMsgSignParams(signature,publicKey,msg,netParams)
        val verifyMsgSignatureResult = msgParams.verifyMsgSignature()

        logger.info("\nVerifyMsgSignatureResult.publicKey: ${verifyMsgSignatureResult.isValid} \nVerifyMsgSignatureResult.address: ${verifyMsgSignatureResult.address} \n")
    }
}
