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
import com.waykichain.wallet.transaction.CoinType
import com.waykichain.wallet.transaction.WaykiTxType
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt

class WaykiDeployContractTxParams( nValidHeight: Long, fees: Long, val srcRegId: String, val vContract: ByteArray?,val description:String):
        BaseSignTxParams(CoinType.WICC.type,null, null, nValidHeight, fees, WaykiTxType.LCONTRACT_DEPLOY_TX, 1) {
    override fun getSignatureHash(): ByteArray {
        val ss = HashWriter()
        ss.write(VarInt(nVersion).encodeInOldWay())
        ss.write(VarInt(nTxType.value.toLong()).encodeInOldWay())
        ss.write(VarInt(nValidHeight).encodeInOldWay())
        ss.writeRegId(srcRegId)
        ss.writeScript(vContract!!,description)
        ss.write(VarInt(fees).encodeInOldWay())
        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        val hashStr = Utils.HEX.encode(hash)

        System.out.println("hash: $hashStr")
        return hash
    }

    override fun signTx(key: ECKey): ByteArray {
        val sigHash = this.getSignatureHash()
        val ecSig = key.sign(Sha256Hash.wrap(sigHash))
        signature =  ecSig.encodeToDER()//NativeSecp256k1.sign(sigHash, key.privKeyBytes)
        return signature!!
    }

    override fun serializeTx(): String {
        assert (signature != null)
        val ss = HashWriter()
        ss.write(VarInt(nTxType.value.toLong()).encodeInOldWay())
        ss.write(VarInt(nVersion).encodeInOldWay())
        ss.write(VarInt(nValidHeight).encodeInOldWay())
        ss.writeRegId(srcRegId)
        ss.writeScript(vContract!!,description)
        ss.write(VarInt(fees).encodeInOldWay())
        val sigSize = signature!!.size
        ss.write(VarInt(sigSize.toLong()).encodeInOldWay())
        ss.write(signature)
        val hexStr =  Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }
}
