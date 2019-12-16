package com.waykichain.wallet.transaction.encode.params

import com.google.common.base.Strings
import com.waykichain.wallet.encode.HashWriter
import com.waykichain.wallet.encode.UCoinDest
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.WaykiTxType
import com.waykichain.wallet.transaction.decode.HashReader
import org.bitcoinj.core.*

/**
 * srcRegId: (regHeight-regIndex PubKeyHash)
 * destAddr: 20-byte PubKeyHash
 * fee Minimum 0.001 wicc
 */
class WaykiUCoinTxParams(nValidHeight: Long, val regId: String?,val dests:List<UCoinDest>, var feeSymbol: String, fees: Long, val memo: String) :
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.TX_UCOIN_TRANSFER, 1) {

    private var userPubKey:ByteArray?=null
    private var signature:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        val ss = HashWriter()
       this.userPubKey = Utils.HEX.decode(pubKey)
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(nTxType.value)
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(regId, this.userPubKey)
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
                .writeUserId(regId, this.userPubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .addUCoinDestAddr(dests)
                .add(memo)
                .writeCompactSize(sigSize.toLong())
                .add(signature)

        val hexStr = Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }

    companion object {
        fun unSerializeTx(ss: HashReader, params: NetworkParameters): BaseSignTxParams {
            //val ss = HashReader(Utils.HEX.decode(param))
            //val nTxType = WaykiTxType.init(ss.readVarInt().value.toInt())
            val nVersion = ss.readVarInt().value
            val nValidHeight = ss.readVarInt().value
            val array = ss.readUserId()
            val userId = array[0]
            val pubKey = array[1]
            val feeSymbol = ss.readString()
            val fees = ss.readVarInt().value
            val dests = ArrayList<UCoinDest>()
            ss.readUCoinDestAddr(dests, params)
            val memo = ss.readString()
            val signature = ss.readByteArray()
            val ret =  WaykiUCoinTxParams(nValidHeight, userId, dests, feeSymbol, fees, memo)
            ret.userPubKey = Utils.HEX.decode(pubKey)
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
                .append("[userId]=").append(if(Strings.isNullOrEmpty(regId)) Utils.HEX.encode(userPubKey) else regId).append("\n")
                //.append("[pubKey]=").append(userPubKey).append("\n")
                .append("[feeSymbol]=").append(feeSymbol).append("\n")
                .append("[fees]=").append(fees).append("\n")
                .append("[memo]=").append(memo).append("\n")
                .append("[signature]=").append(Utils.HEX.encode(signature)).append("\n")
        for(dest in dests) {
            builder.append("[destAddress]=").append(dest.destAddress).append("\n")
                    .append("[coinSymbol]=").append(dest.coinSymbol).append("\n")
                    .append("[transferAmount]=").append(dest.transferAmount).append("\n")
        }
        return builder.toString()
    }
}
