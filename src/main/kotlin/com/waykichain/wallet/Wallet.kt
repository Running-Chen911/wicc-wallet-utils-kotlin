package com.waykichain.wallet

import org.bitcoinj.core.ECKey

class Wallet(val eckey: ECKey,
             val privateKey: String,
             val address: String){

    override fun toString(): String {
        return StringBuilder().append("privateKey:").append(privateKey).append("\n")
                .append("publickKey:").append(eckey.publicKeyAsHex).append("\n")
                .append("address:").append(address).toString()
    }
}