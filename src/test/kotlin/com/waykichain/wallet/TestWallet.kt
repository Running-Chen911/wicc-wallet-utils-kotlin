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

import com.waykichain.wallet.encode.WaykiRegId
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.params.WaykiMainNetParams
import com.waykichain.wallet.transaction.params.WaykiTestNetParams
import com.waykichain.wallet.util.BIP44Path
import org.bitcoinj.core.*
import org.bitcoinj.wallet.DeterministicKeyChain
import org.bitcoinj.wallet.DeterministicSeed
import org.junit.Test
import org.slf4j.LoggerFactory
import org.waykichain.wallet.util.MnemonicUtil
import java.io.ByteArrayOutputStream
import java.util.*


/**
 * @Author: Richard Chen
 * @Date 2018/08/29 下午6:27
 */

class TestWallet {

    private val logger = LoggerFactory.getLogger(javaClass)

    /*
    * 生成助记词 generate Mnemonic
    * */
    @Test
    fun generateMnemonic() {
        var words: List<String>
        words = MnemonicUtil.randomMnemonicCodes()
        logger.info(words.toString())
    }

    /*
    * 助记词生成维基链钱包 Mnemonic to WaykiChain Wallet
    * */
    @Test
    fun generateWalletFromMnemonic() {
        val mn = "wreck wheat chunk fiber maze opera recipe must glory empower summer bind"
        val words = mn.split(" ")
        val wallet = WalletManager.importWalletFromMnemonic(words, ChainIds.WAYKICHAIN_MAINNET)
        assert("WjeEKvXWDJyChChcCmU4USYKTrYrGmEXUR".equals(wallet.address))
        logger.info(wallet.toString())
    }

    /*
       * 私钥生成维基链钱包 PrivateKey to WaykiChain Wallet
       * */
    @Test
    fun testImportPrivKey() {
        val privKeyWiF = "Y7UiVRpTAZNDtZakSHZwebHD6romu9jcuj1tjjujzwbSqdKLCEQZ"//"YBb6tdJvQyD8VwxJ4HUjDfpcpmFc359uGFQLbegaaKr6FJY863iw"//"YAHcraeGRDpvwBWVccV7NLGAU6uK39nNUTip8srbJSu6HKSTfDcC"
        val wallet = WalletManager.importWalletFromPrivateKey(privKeyWiF, ChainIds.WAYKICHAIN_TESTNET)
        assert("wZCst8wFgxiaNptqhheMvRugdngMJMZAKL".equals(wallet.address))
        logger.info(wallet.toString())
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
}
