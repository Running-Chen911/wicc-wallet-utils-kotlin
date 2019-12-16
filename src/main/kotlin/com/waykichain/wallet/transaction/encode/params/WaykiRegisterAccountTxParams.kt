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
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt

class WaykiRegisterAccountTxParams( nValidHeight: Long, fees: Long):
        BaseSignTxParams(nValidHeight, fees, WaykiTxType.TX_REGISTERACCOUNT, 1) {
    private var userPubKey:ByteArray?=null
    private var signature:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(nTxType.value)
                .add(VarInt(nValidHeight).encodeInOldWay())
                .add(VarInt(33).encodeInOldWay())
                .add(this.userPubKey)
                .add(VarInt(0).encodeInOldWay())
                .add(VarInt(fees).encodeInOldWay())

        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val sigSize = signature.size
        val ss = HashWriter()
        ss.add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .add(33)
                .add(this.userPubKey)
                .add(0)
                .add(VarInt(fees).encodeInOldWay())
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val bytes = ss.toByteArray()
        val hexStr =  Utils.HEX.encode(bytes)
        return hexStr
    }

    companion object {
        fun unSerializeTx(ss: HashReader): BaseSignTxParams {
            //val ss = HashReader(Utils.HEX.decode(param))
            // val nTxType = WaykiTxType.init(ss.readVarInt().value.toInt())
            val nVersion = ss.readVarInt().value
            val nValidHeight = ss.readVarInt().value
            val userPubKey = ss.readPubKey()
            ss.read()// 0
            val fees = ss.readVarInt().value
            val signature = ss.readByteArray()
            val ret = WaykiRegisterAccountTxParams(nValidHeight, fees)
            //ret.nTxType = nTxType
            ret.nVersion = nVersion
            ret.signature = signature
            return ret
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("[nTxType]=").append(nTxType).append("\n")
                .append("[nVersion]=").append(nVersion).append("\n")
                .append("[nValidHeight]=").append(nValidHeight).append("\n")
                .append("[pubKey]=").append(userPubKey).append("\n")
                .append("[fees]=").append(fees).append("\n")
                .append("[signature]=").append(Utils.HEX.encode(signature)).append("\n")

        return builder.toString()
    }
}