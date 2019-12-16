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
 */

package com.waykichain.wallet.transaction.encode.params

import com.waykichain.wallet.encode.HashWriter
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.WaykiTxType
import com.waykichain.wallet.transaction.decode.HashReader
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt

class WaykiUCoinContractTxParams( nValidHeight: Long, fees: Long, val value: Long, val srcRegId: String?,
                            val destRegId: String, val vContract: ByteArray?,var feeSymbol:String,val coinSymbol:String):
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.UCONTRACT_INVOKE_TX, 1) {
    private var userPubKey:ByteArray?=null
    private var signature:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.write(VarInt(nVersion).encodeInOldWay())
        ss.write(VarInt(nTxType.value.toLong()).encodeInOldWay())
        ss.write(VarInt(nValidHeight).encodeInOldWay())
        ss.writeUserId(srcRegId,userPubKey)
        ss.writeRegId(destRegId)
        ss.writeCompactSize(vContract?.size?.toLong()!!)
        ss.write(vContract)
        ss.write(VarInt(fees).encodeInOldWay())
        ss.add(feeSymbol)
        ss.add(coinSymbol)
        ss.write(VarInt(value).encodeInOldWay())
        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val ss = HashWriter()
        ss.write(VarInt(nTxType.value.toLong()).encodeInOldWay())
        ss.write(VarInt(nVersion).encodeInOldWay())
        ss.write(VarInt(nValidHeight).encodeInOldWay())
        ss.writeUserId(srcRegId,this.userPubKey)
        ss.writeRegId(destRegId)
        ss.writeCompactSize(vContract?.size?.toLong()!!)
        ss.write(vContract)
        ss.write(VarInt(fees).encodeInOldWay())
        ss.add(feeSymbol)
        ss.add(coinSymbol)
        ss.write(VarInt(value).encodeInOldWay())
        val sigSize = signature!!.size
        ss.writeCompactSize(sigSize.toLong())
        ss.write(signature)
        val hexStr =  Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }

    companion object {
        fun unSerializeTx(ss: HashReader): BaseSignTxParams {
            val nVersion = ss.readVarInt().value
            val nValidHeight = ss.readVarInt().value
            val array = ss.readUserId()
            val srcRegId = array[0]
            val publicKey = array[1]
            val destRegId = ss.readRegId()
            val vContract = ss.readByteArray()
            val fees = ss.readVarInt().value
            val feeSymbol = ss.readString()
            val coinSymbol = ss.readString()
            val value = ss.readVarInt().value
            val signature = ss.readByteArray()
            val ret = WaykiUCoinContractTxParams( nValidHeight, fees, value, srcRegId, destRegId, vContract, feeSymbol, coinSymbol)
            ret.signature = signature
            ret.nVersion = nVersion
            return ret
        }
    }
    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("[nTxType]=").append(nTxType).append("\n")
                .append("[nVersion]=").append(nVersion).append("\n")
                .append("[nValidHeight]=").append(nValidHeight).append("\n")
                .append("[srcRegId]=").append(srcRegId).append("\n")
                .append("[destRegId]=").append(destRegId).append("\n")
                .append("[pubKey]=").append(userPubKey).append("\n")
                .append("[feeSymbol]=").append(feeSymbol).append("\n")
                .append("[coinSymbol]=").append(coinSymbol).append("\n")
                .append("[fees]=").append(fees).append("\n")
                .append("[value]=").append(value).append("\n")
                .append("[vContract]=").append(Utils.HEX.encode(vContract)).append("\n")
                .append("[signature]=").append(Utils.HEX.encode(signature)).append("\n")
        return builder.toString()
    }

}
