package com.waykichain.wallet

import com.waykichain.wallet.transaction.params.WaykiMainNetParams
import com.waykichain.wallet.transaction.params.WaykiTestNetParams
import com.waykichain.wallet.util.BIP44Path
import org.bitcoinj.core.DumpedPrivateKey
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.LegacyAddress
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.wallet.DeterministicKeyChain
import org.bitcoinj.wallet.DeterministicSeed
import org.waykichain.wallet.util.Messages
import org.waykichain.wallet.util.MnemonicUtil
import org.waykichain.wallet.util.TokenException

class WalletManager {

    companion object {
        var networkParameters: NetworkParameters? = null
        fun importWalletFromMnemonic(mnemonics: List<String>, networkType: Int): Wallet{
            MnemonicUtil.validateMnemonics(mnemonics)
            if (networkType !== 1&&networkType !== 2) throw TokenException(Messages.INVALID_NETWORK_TYPE)
            if (ChainIds.WAYKICHAIN_MAINNET == networkType) networkParameters = WaykiMainNetParams.instance else networkParameters = WaykiTestNetParams.instance
            val seed = DeterministicSeed(mnemonics, null, "", 0L)
            val keyChain = DeterministicKeyChain.builder().seed(seed).build()
            val mainKey = keyChain.getKeyByPath(MnemonicUtil.generatePath(BIP44Path.WAYKICHAIN_WALLET_PATH), true)
            val address = LegacyAddress.fromPubKeyHash(networkParameters, mainKey.pubKeyHash).toString()
            val ecKey = ECKey.fromPrivate(mainKey.privKey)
            val privateKey = ecKey.getPrivateKeyAsWiF(networkParameters)
            val wallet = Wallet(ecKey, privateKey, address)
            return wallet
        }

         fun importWalletFromPrivateKey(privKeyWiF: String, networkType: Int): Wallet {
             if (networkType !== 1&&networkType !== 2) throw TokenException(Messages.INVALID_NETWORK_TYPE)
             if (ChainIds.WAYKICHAIN_MAINNET == networkType) networkParameters = WaykiMainNetParams.instance else networkParameters = WaykiTestNetParams.instance
             val ecKey = DumpedPrivateKey.fromBase58(networkParameters, privKeyWiF).key
             val address = LegacyAddress.fromPubKeyHash(networkParameters, ecKey.pubKeyHash).toString()
             val wallet = Wallet(ecKey, privKeyWiF, address)
             return wallet
         }
    }

}