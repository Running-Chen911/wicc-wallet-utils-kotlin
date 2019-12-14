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
import com.waykichain.wallet.encode.SYMBOL_MATCH
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.AssetUpdateData
import com.waykichain.wallet.transaction.WaykiTxType
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt
import org.waykichain.wallet.util.Messages
import org.waykichain.wallet.util.TokenException

/**
 * srcRegId: (regHeight-regIndex)
 * destAddr: 20-byte PubKeyHash
 */
class WaykiAssetUpdateTxParams(nValidHeight: Long, fees: Long, val srcRegId: String,var feeSymbol: String,
                               val asset_symbol: String, val asset: AssetUpdateData) :
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.ASSET_UPDATE_TX, 1) {
    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(nTxType.value)
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeRegId(srcRegId)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(asset_symbol)
                .updateAsset(asset)

        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val symbolMatch = asset_symbol.matches(SYMBOL_MATCH.toRegex())
        if (!symbolMatch) throw TokenException(Messages.SYMBOLNOTMATCH)
        val sigSize = signature!!.size
        val ss = HashWriter()
        ss.add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeRegId(srcRegId)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(asset_symbol)
                .updateAsset(asset)
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val hexStr = Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }

}
