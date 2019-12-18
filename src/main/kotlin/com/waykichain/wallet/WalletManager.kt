package com.waykichain.wallet

import com.google.common.collect.ImmutableList
import com.waykichain.wallet.transaction.Language
import com.waykichain.wallet.transaction.NetWorkType
import com.waykichain.wallet.transaction.encode.params.WaykiMainNetParams
import com.waykichain.wallet.transaction.encode.params.WaykiTestNetParams
import com.waykichain.wallet.util.BIP44Path
import com.waykichain.wallet.util.WaykiMnemonicCode
import org.bitcoinj.core.DumpedPrivateKey
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.LegacyAddress
import org.bitcoinj.core.NetworkParameters
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.wallet.DeterministicKeyChain
import org.bitcoinj.wallet.DeterministicSeed
import org.waykichain.wallet.util.Messages
import org.waykichain.wallet.util.TokenException
import java.security.SecureRandom
import java.util.ArrayList

class WalletManager {


    companion object {
        private  var network: NetworkParameters? = null
        private var instance: WalletManager? = null
            get() {
                if (field == null) {
                    field = WalletManager()
                }
                return field
            }
        @Synchronized
        fun init(networkType: NetWorkType): WalletManager{
            val type=networkType.type
            if (type != 1&&type != 2) throw TokenException(Messages.INVALID_NETWORK_TYPE)
            network =  if (NetWorkType.WAYKICHAIN_MAINNET.type == type)  WaykiMainNetParams.instance else WaykiTestNetParams.instance
            return instance!!
        }

    }

    fun randomMnemonic(lang: Language):List<String>{
        return randomMnemonicCodes(lang)
    }

    fun importWalletFromMnemonic(mnemonics: List<String>): Wallet{
        validateMnemonics(mnemonics)
        val bip44Path= if (network == WaykiMainNetParams.instance) BIP44Path.WAYKICHAIN_MAINNET_WALLET_PATH else BIP44Path.WAYKICHAIN_TESTNET_WALLET_PATH
        val seed = DeterministicSeed(mnemonics, null, "", 0L)
        val keyChain = DeterministicKeyChain.builder().seed(seed).build()
        val mainKey = keyChain.getKeyByPath(generatePath(bip44Path), true)
        val address = LegacyAddress.fromPubKeyHash(network, mainKey.pubKeyHash).toString()
        val ecKey = ECKey.fromPrivate(mainKey.privKey)
        val privateKey = ecKey.getPrivateKeyAsWiF(network)
        val wallet = Wallet(ecKey, privateKey, address)
        return wallet
    }

    fun importWalletFromPrivateKey(privKeyWiF: String): Wallet {
        val ecKey = DumpedPrivateKey.fromBase58(network, privKeyWiF).key
        val address = LegacyAddress.fromPubKeyHash(network, ecKey.pubKeyHash).toString()
        val wallet = Wallet(ecKey, privKeyWiF, address)
        return wallet
    }

    private  fun validateMnemonics(mnemonicCodes: List<String>) {
        try {
            WaykiMnemonicCode.create().checkMnemonic(mnemonicCodes)
        } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicLengthException) {
            throw TokenException(Messages.MNEMONIC_INVALID_LENGTH)
        } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicWordException) {
            throw TokenException(Messages.MNEMONIC_BAD_WORD)
        } catch (e: Exception) {
            throw TokenException(Messages.MNEMONIC_INVALID_CHECKSUM)
        }
    }

    private fun randomMnemonicCodes(lang: Language): List<String> {
        return toMnemonicCodes(lang)
    }

    private fun toMnemonicCodes(lang: Language): List<String> {
        try {
            return WaykiMnemonicCode.create().generateMnemonic(lang)
        } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicLengthException) {
            throw TokenException(Messages.MNEMONIC_INVALID_LENGTH)
        } catch (e: Exception) {
            throw TokenException(Messages.MNEMONIC_INVALID_CHECKSUM)
        }
    }

    private fun generatePath(path: String): ImmutableList<ChildNumber> {
        val list = ArrayList<ChildNumber>()
        for (p in path.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            if ("m".equals(p, ignoreCase = true) || "" == p.trim { it <= ' ' }) {
                continue
            } else if (p[p.length - 1] == '\'') {
                list.add(ChildNumber(Integer.parseInt(p.substring(0, p.length - 1)), true))
            } else {
                list.add(ChildNumber(Integer.parseInt(p), false))
            }
        }
        val builder = ImmutableList.builder<ChildNumber>()
        return builder.addAll(list).build()
    }

}