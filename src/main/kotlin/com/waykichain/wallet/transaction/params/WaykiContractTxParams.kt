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

package com.waykichain.wallet.transaction.params

import com.waykichain.wallet.encode.HashWriter
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.WaykiTxType
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt

class WaykiContractTxParams(nValidHeight: Long, fees: Long, val value: Long, val srcRegId: String,
                            val destRegId: String, val vContract: ByteArray?):
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.TX_CONTRACT, 1) {
    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.write(VarInt(nVersion).encodeInOldWay())
        ss.write(VarInt(nTxType.value.toLong()).encodeInOldWay())
        ss.write(VarInt(nValidHeight).encodeInOldWay())
        ss.writeUserId(srcRegId,userPubKey)
        ss.writeRegId(destRegId)
        ss.write(VarInt(fees).encodeInOldWay())
        ss.write(VarInt(value).encodeInOldWay())
        ss.writeCompactSize(vContract!!.size.toLong())//(VarInt(vContract!!.size.toLong()).encodeInOldWay())
        ss.write(vContract)
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
        ss.write(VarInt(fees).encodeInOldWay())
        ss.write(VarInt(value).encodeInOldWay())
        ss.writeCompactSize(vContract!!.size.toLong())
        ss.write(vContract)
        val sigSize = signature!!.size
        ss.writeCompactSize(sigSize.toLong())
        ss.write(signature)
        val hexStr =  Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }
}
