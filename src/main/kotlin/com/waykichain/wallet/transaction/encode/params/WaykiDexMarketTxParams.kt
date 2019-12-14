package com.waykichain.wallet.transaction.encode.params

import com.waykichain.wallet.encode.HashWriter
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.WaykiTxType
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt

/**
 * srcRegId: (regHeight-regIndex PubKeyHash)
 * destAddr: 20-byte PubKeyHash
 * fee Minimum 0.0001 wicc
 */
class WaykiDexMarketTxParams(nValidHeight: Long, fees: Long,val srcRegId: String?, var feeSymbol: String,
                            val coinSymbol: String,val assetSymbol: String,val assetAmount: Long,
                            txType: WaykiTxType) :
        BaseSignTxParams(nValidHeight, fees, txType, 1) {

    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(nTxType.value)
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(srcRegId, userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(coinSymbol)
                .add(assetSymbol)
                .add(VarInt(assetAmount).encodeInOldWay())
        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        val hashStr = Utils.HEX.encode(hash)
        System.out.println("hash: $hashStr")

        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val sigSize = signature!!.size
        val ss = HashWriter()
        ss.add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(srcRegId, userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(coinSymbol)
                .add(assetSymbol)
                .add(VarInt(assetAmount).encodeInOldWay())
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val hexStr = Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }
}
