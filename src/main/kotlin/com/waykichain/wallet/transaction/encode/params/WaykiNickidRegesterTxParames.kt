package com.waykichain.wallet.transaction.encode.params

import com.waykichain.wallet.encode.HashWriter
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.WaykiTxType
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt


class WaykiNickidRegesterTxParames( nValidHeight: Long, fees: Long,var nickId:String,var srcRegid:String,var feeSymbol:String):
        BaseSignTxParams(nValidHeight, fees, WaykiTxType.NICKID_REGISTER_TX, 1) {
    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(srcRegid,userPubKey)
                .add(feeSymbol)
                .add(nickId)
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
                .writeUserId(srcRegid,this.userPubKey)
                .add(feeSymbol)
                .add(nickId)
                .add(VarInt(fees).encodeInOldWay())
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val bytes = ss.toByteArray()
        val hexStr =  Utils.HEX.encode(bytes)
        return hexStr
    }
}