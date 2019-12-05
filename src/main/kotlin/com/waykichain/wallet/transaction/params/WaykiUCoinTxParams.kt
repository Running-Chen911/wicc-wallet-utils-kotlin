package com.waykichain.wallet.transaction.params

import com.waykichain.wallet.encode.HashWriter
import com.waykichain.wallet.encode.UCoinDest
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
class WaykiUCoinTxParams(nValidHeight: Long, val userId: String,val dests:List<UCoinDest>, var feeSymbol: String, fees: Long, val memo: String) :
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.TX_UCOIN_TRANSFER, 1) {

    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        val ss = HashWriter()
       this.userPubKey = Utils.HEX.decode(pubKey)
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(nTxType.value)
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(userId, this.userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .addUCoinDestAddr(dests)
                .add(memo)

        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val sigSize = signature.size
        val ss = HashWriter()
        ss.add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(userId, this.userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .addUCoinDestAddr(dests)
                .add(memo)
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val hexStr = Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }
}
