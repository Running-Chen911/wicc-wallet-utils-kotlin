package com.waykichain.wallet

import com.waykichain.wallet.transaction.encode.params.BaseSignTxParams

class WaykiTransactions(var txParams: BaseSignTxParams, var wallet: Wallet) {

    fun genRawTx():String{
        val signature = wallet.signTx(txParams.getSignatureHash(wallet.publicKeyAsHex()))
        val rawTxAsHex=txParams?.serializeTx(signature)
        return rawTxAsHex
    }

    fun decodeTxRaw(rawTx:String): BaseSignTxParams?{
        return null
    }
}