package com.waykichain.wallet.model.node.rpc

import java.util.ArrayList

class RpcBean{
    val id: Int=1
    val jsonrpc: String="2.0"
    var method: String?=null
    var params: ArrayList<Any>?=null
}

open class RpcBaseBean(var id:String,var error:RpcErrorBean)
class BlockCountBean(id:String, error:RpcErrorBean, var result:String):RpcBaseBean(id,error)
class RpcErrorBean(val code:String,val message:String)
class RpcAccountInfoBean(id:String, error:RpcErrorBean, var result:AccountInfoResult):RpcBaseBean(id,error)

data class AccountInfoResult(
    val address: String,
    val cdp_list: List<Any>,
    val keyid: String,
    val miner_pubkey: String,
    val nickid: String,
    val owner_pubkey: String,
    val position: String,
    val received_votes: String,
    val regid: String,
    val regid_mature: Boolean,
    val tokens: Tokens,
    val vote_list: List<Any>
)

data class Tokens(
    val WICC: WICC,
    val WUSD: WUSD
)

data class WICC(
    val free_amount: Long,
    val frozen_amount: String,
    val staked_amount: String,
    val voted_amount: String
)

data class WUSD(
    val free_amount: String,
    val frozen_amount: String,
    val staked_amount: String,
    val voted_amount: String
)

class SubmitTxRawBean(id:String, error:RpcErrorBean, var result:TxResult):RpcBaseBean(id,error)

data class TxResult(
    val txid: String
)