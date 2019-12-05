package com.waykichain.wallet.transaction.params

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
class WaykiDexCancelOrderTxParams(nValidHeight: Long, fees: Long,val userId: String, userPubKey: String?, var feeSymbol: String,
                            val orderId: String) :
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.DEX_CANCEL_ORDER_TX, 1) {

    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        val orderIdByte = Utils.HEX.decode(orderId).reversedArray()
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(nTxType.value)
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(userId, userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(orderIdByte)
        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val sigSize = signature!!.size
        val orderIdByte = Utils.HEX.decode(orderId).reversedArray()
        val ss = HashWriter()
        ss.add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(userId, userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(orderIdByte)
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val hexStr = Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }
}
