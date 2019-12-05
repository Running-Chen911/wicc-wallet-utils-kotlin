package com.waykichain.wallet.util

import com.waykichain.wallet.WalletManager
import com.waykichain.wallet.transaction.Language
import org.bitcoinj.crypto.MnemonicCode
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.security.SecureRandom

class WaykiMnemonicCode {
    private val BIP39_CHINESE_SIMPLE_RESOURCE_NAME = "mnemonic/wordlist/chinese_simple.txt"
    private val BIP39_CHINESE_SIMPLE_SHA256 = "bfd683b91db88609fabad8968c7efe4bf69606bf5a49ac4a4ba5e355955670cb"
    private val SECURE_RANDOM = SecureRandom()

    companion object {
        var language: Language? = null
        private var instance: WaykiMnemonicCode? = null
            get() {
                if (field == null) {
                    field = WaykiMnemonicCode()
                }
                return field
            }

        @Synchronized
        fun create(lang: Language): WaykiMnemonicCode {
            this.language = lang
            return instance!!
        }

    }

    private fun generateRandomBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        SECURE_RANDOM.nextBytes(bytes)
        return bytes
    }

    fun generateMnemonic(): List<String> {
        when (language) {
            Language.CHINESE -> {
                val stream=openDefaultWords(BIP39_CHINESE_SIMPLE_RESOURCE_NAME)
                val mn = MnemonicCode(stream, BIP39_CHINESE_SIMPLE_SHA256)
                return mn.toMnemonic(generateRandomBytes(16))
            }
            else -> {
                return MnemonicCode().toMnemonic(generateRandomBytes(16))
            }
        }
    }

    @Throws(IOException::class)
    private fun openDefaultWords(resource: String): InputStream {
        return MnemonicCode::class.java.getResourceAsStream(resource)
                ?: throw FileNotFoundException(resource)
    }
}