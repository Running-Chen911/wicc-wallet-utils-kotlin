package com.waykichain.wallet.model.baas

import com.waykichain.wallet.model.baas.parameter.BaseBean

class BlockHeight(code:Int, msg:String,val data: Int):BaseBean(code, msg)

class TxDetail(code:Int, msg:String,val data: TxDetailData):BaseBean(code, msg)

data class TxDetailData(val txid:String?)

class AccointInfo(code:Int, msg:String,val data: AccointData):BaseBean(code, msg)

data class AccointData(
    val address: String,
    val balance: String,
    val cdplist: List<Cdplist>,
    val keyid: String,
    val minerpkey: String,
    val nickid: String,
    val position: String,
    val publickey: String,
    val receivedvotes: String,
    val regid: String,
    val regidmature: Boolean,
    val tokens: Tokens,
    val votelist: List<Votelist>
)

data class Cdplist(
    val bcoinsymbol: String,
    val cdpid: String,
    val collateralratio: String,
    val lastheight: String,
    val regid: String,
    val scoinsymbol: String,
    val totalbcoin: String,
    val totalscoin: String
)

class Tokens(
)

data class Votelist(
    val candidateUid: CandidateUid,
    val votedBcoins: String
)

data class CandidateUid(
    val id: String,
    val idType: String
)