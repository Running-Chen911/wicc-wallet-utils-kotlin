package com.waykichain.wallet

import com.waykichain.wallet.transaction.WaykiTxType
import com.waykichain.wallet.transaction.decode.HashReader
import com.waykichain.wallet.transaction.encode.params.BaseSignTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiRegisterAccountTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiUCoinContractTxParams
import com.waykichain.wallet.transaction.encode.params.WaykiUCoinTxParams
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.core.Utils

class WaykiTransactions(var txParams: BaseSignTxParams?, var wallet: Wallet?) {

    constructor() : this(null,null)

    fun genRawTx():String{
        val signature = wallet?.signTx(txParams?.getSignatureHash(wallet?.publicKeyAsHex())!!)
        val rawTxAsHex=txParams?.serializeTx(signature!!)
        return rawTxAsHex!!
    }

    fun decodeTxRaw(rawtx:String,params: NetworkParameters): BaseSignTxParams?{
        val ss = HashReader(Utils.HEX.decode(rawtx))
        var ret:BaseSignTxParams
        val nTxType = WaykiTxType.init(ss.readVarInt().value.toInt())
        when(nTxType){
            WaykiTxType.TX_UCOIN_TRANSFER ->{
                ret = WaykiUCoinTxParams.unSerializeTx(ss, params)
                ret.nTxType = nTxType
            }
            WaykiTxType.TX_REGISTERACCOUNT ->{
                ret = WaykiRegisterAccountTxParams.unSerializeTx(ss)
                ret.nTxType = nTxType
            }
            WaykiTxType.UCONTRACT_INVOKE_TX ->{
                ret = WaykiUCoinContractTxParams.unSerializeTx(ss)
            }
            else ->{
                ret = WaykiRegisterAccountTxParams.unSerializeTx(ss)
            }
        }
        return ret
    }
}