package com.waykichain.wallet

import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash

class Wallet(val ecKey: ECKey,
             val privateKey: String,
             val address: String){

    fun signTx(txHash:ByteArray):ByteArray{
        val ecSig =ecKey?.sign(Sha256Hash.wrap(txHash))
       val signature = ecSig.encodeToDER()
        return signature!!
    }

    fun signMessage(message:String):String{
     return ""
    }

    fun publicKeyAsHex():String{
        return ecKey?.publicKeyAsHex
    }

    override fun toString(): String {
        return StringBuilder().append("privateKey:").append(privateKey).append("\n")
                .append("publickKey:").append(publicKeyAsHex()).append("\n")
                .append("address:").append(address).toString()
    }
}