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
import org.bitcoinj.core.*

/**
 * srcRegId: (regHeight-regIndex)
 * destAddr: 20-byte PubKeyHash
 */
class WaykiCommonTxParams(nValidHeight: Long, fees: Long, val value: Long, val srcRegId: String?,var destAddr: String, val memo: String):
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.TX_COMMON, 1) {

    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(srcRegId,userPubKey)
                .writeAddress(destAddr)
                .add(VarInt(fees).encodeInOldWay())
                .add(VarInt(value).encodeInOldWay())
                .add(memo)

        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val sigSize = signature!!.size
        val ss = HashWriter()
        ss.add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(srcRegId,this.userPubKey)
                .writeAddress(destAddr)
                .add(VarInt(fees).encodeInOldWay())
                .add(VarInt(value).encodeInOldWay())
                .add(memo)
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val hexStr = Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }

}
