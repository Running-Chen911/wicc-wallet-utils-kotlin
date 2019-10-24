package com.waykichain.wallet.client

class ChainEnvirment(val baasUrl: String, val nodeUrl: String) {

    private object Holder {
        val TESTNET_INSTANCE = ChainEnvirment("https://baas-test.wiccdev.org","http://10.0.0.31:6968")
        val MAINNET_INSTANCE = ChainEnvirment("https://baas.wiccdev.org","http://10.0.0.31:6968")
    }

    companion object {
        val TESTNET: ChainEnvirment by lazy { Holder.TESTNET_INSTANCE }
        val MAINNET: ChainEnvirment by lazy { Holder.MAINNET_INSTANCE }
    }

}