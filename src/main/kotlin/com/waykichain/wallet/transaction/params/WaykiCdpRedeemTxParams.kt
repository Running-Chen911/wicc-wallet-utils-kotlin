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
class WaykiCdpRedeemTxParams(nValidHeight: Long, fees: Long = 1000L,
                             val userId: String, userPubKey: String, val cdpTxid: String? = cdpHash,
                             feeSymbol: String, val sCoinsToRepay: Long, val assetMap:Map<String,Long>) :
        BaseSignTxParams(feeSymbol, userPubKey, null, nValidHeight, fees, WaykiTxType.TX_CDPREDEEM, 1) {

    override fun getSignatureHash(): ByteArray {
        val ss = HashWriter()
        val cdpTxHex = Utils.HEX.decode(cdpTxid).reversedArray()
        val pubKey = Utils.HEX.decode(userPubKey)
        ss.add(VarInt(nVersion).encodeInOldWay())
                .add(nTxType.value)
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(userId, pubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(cdpTxHex)
                .add(VarInt(sCoinsToRepay).encodeInOldWay())
                .add(VarInt(assetMap?.size.toLong()).encodeInOldWay())
                .addCdpAssets(assetMap)
        val hash = Sha256Hash.hashTwice(ss.toByteArray())
        val hashStr = Utils.HEX.encode(hash)
        System.out.println("hash: $hashStr")

        return hash
    }

    override fun signTx(key: ECKey): ByteArray {
        val sigHash = this.getSignatureHash()
        val ecSig = key.sign(Sha256Hash.wrap(sigHash))
        signature = ecSig.encodeToDER()
        return signature!!
    }

    override fun serializeTx(): String {
        assert(signature != null)
        val sigSize = signature!!.size
        val cdpTxHex = Utils.HEX.decode(cdpTxid).reversedArray()
        val pubKey = Utils.HEX.decode(userPubKey)
        val ss = HashWriter()
        ss.add(VarInt(nTxType.value.toLong()).encodeInOldWay())
                .add(VarInt(nVersion).encodeInOldWay())
                .add(VarInt(nValidHeight).encodeInOldWay())
                .writeUserId(userId, pubKey)
                .add(feeSymbol)
                .add(VarInt(fees).encodeInOldWay())
                .add(cdpTxHex)
                .add(VarInt(sCoinsToRepay).encodeInOldWay())
                .add(VarInt(assetMap?.size.toLong()).encodeInOldWay())
                .addCdpAssets(assetMap)
                .add(VarInt(sigSize.toLong()).encodeInOldWay())
                .add(signature)

        val hexStr = Utils.HEX.encode(ss.toByteArray())
        return hexStr
    }
}
