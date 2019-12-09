package com.waykichain.wallet.transaction.params

import com.waykichain.wallet.encode.HashWriter
import com.waykichain.wallet.encode.cdpHash
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.WaykiTxType
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt

/**
 * srcRegId: (regHeight-regIndex PubKeyHash)
 * destAddr: 20-byte PubKeyHash
 * fee Minimum 0.001 wicc
 */
class WaykiCdpLiquidateTxParams(nValidHeight: Long, fees: Long,
                                val srcRegId: String?,  val cdpTxid: String,
                               var feeSymbol: String, val sCoinsToLiquidate: Long, val liquidateAssetSymbol:String) :
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.TX_CDPLIQUIDATE, 1) {

    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val cdpTxHex = Utils.HEX.decode(cdpTxid).reversedArray()
       this.userPubKey = Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(srcRegId, this.userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(cdpTxHex)
                .add(liquidateAssetSymbol)
                .add(VarInt(sCoinsToLiquidate).encodeInOldWay())
        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val sigSize = signature.size
        val cdpTxHex = Utils.HEX.decode(cdpTxid).reversedArray()
        val ss = HashWriter()
        ss.add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(srcRegId, this.userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(cdpTxHex)
                .add(liquidateAssetSymbol)
                .add(VarInt(sCoinsToLiquidate).encodeInOldWay())
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val hexStr = Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }
}
