package com.waykichain.wallet.transaction

import com.waykichain.wallet.WaykiTransactions
import com.waykichain.wallet.transaction.encode.params.WaykiTestNetParams
import org.junit.Test
import org.slf4j.LoggerFactory

class TransactionDecode {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Test
    fun testParseTransactionRaw() {
        val waykiTransactions=WaykiTransactions()
        val netParams = WaykiTestNetParams.instance
        var rawtx = "0b0180e3b7740480fe4a050457494343bc83400214079b9296a00a2b655787fa90e66ec3cde4bf1c8c0457494343cd1004baef1a020457494343cd1006e8bdace8b4a64630440220338e8bcbfb81b5ad6024afe0030e6529139b82f47736317ab3f31202dd71f526022037512c60f4a3b84ae0fbca4ac4d528c8d22c002727611fba99325baa8bfa30fb"
        var ret = waykiTransactions.decodeTxRaw(rawtx, netParams)
        logger.info(ret.toString())

        rawtx = "0201999c7d2102a722a3a94fb41d92bcf9d54cd76ea40c8b0c223d6f0570389b775120c5e487640083e1ac0046304402205304902f6ae8470e7c294b8abe7fdd5a9847d8980914234c9ddb9b6098e473d002200ad2d0238292285394447905cb20b7275cd2daf3a68d1237a1200982b99172bc"
        ret =  waykiTransactions.decodeTxRaw(rawtx, netParams)
        logger.info(ret.toString())

        rawtx = "0f0180a394350482de23020496f82e03a0f0210000775a437374387746677869614e7074716868654d76527567646e674d4a4d5a414b4c3135373839363536323435323563376b3374303030303030303030303030333839380065cd1d0000000000000000000000000000000000000000c80000000000000064000000030000000000000000573200000000000000000000000000000044320000000000000000000000000000004c3200000000000000bc83400457555344045755534480edb4c900463044022070b8a94dc19e67c5ea71cf88e27bb726c8ab730ed1a05974ab4fc457ed4736fe02204c6ba856e275e2b11a03248ce0df46a95dc436bef32e37004493840bf59b90b5"
        ret =  waykiTransactions.decodeTxRaw(rawtx, netParams)
        logger.info(ret.toString())
    }

}