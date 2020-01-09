package com.waykichain.wallet.transaction.encode.params

import com.waykichain.wallet.encode.HashWriter
import com.waykichain.wallet.encode.encodeInOldWay
import com.waykichain.wallet.transaction.CoinType
import com.waykichain.wallet.transaction.WaykiTxType
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.core.Utils
import org.bitcoinj.core.VarInt

class WaykiWasmDeployTxParames( nValidHeight: Long, fees: Long, val srcRegId: String, val vContract: ByteArray?,val description:ByteArray):
        BaseSignTxParams( nValidHeight, fees, WaykiTxType.WASM_CONTRACT_TX, 1) {
    private var userPubKey:ByteArray?=null
    override fun getSignatureHash(pubKey:String?): ByteArray {
        this.userPubKey=Utils.HEX.decode(pubKey)
        val ss = HashWriter()
        ss.write(VarInt(nVersion).encodeInOldWay())
        ss.write(VarInt(nTxType.value.toLong()).encodeInOldWay())
        ss.write(VarInt(nValidHeight).encodeInOldWay())
        ss.writeRegId(srcRegId)
        ss.write(VarInt(fees).encodeInOldWay())
        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        return hash
    }

    override fun serializeTx(signature:ByteArray): String {
        val ss = HashWriter()
        ss.write(VarInt(nTxType.value.toLong()).encodeInOldWay())
        ss.write(VarInt(nVersion).encodeInOldWay())
        ss.write(VarInt(nValidHeight).encodeInOldWay())
        ss.writeRegId(srcRegId)
        ss.write(VarInt(fees).encodeInOldWay())
        val sigSize = signature!!.size
        ss.writeCompactSize(sigSize.toLong())
        ss.write(signature)
        val hexStr =  Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }
}