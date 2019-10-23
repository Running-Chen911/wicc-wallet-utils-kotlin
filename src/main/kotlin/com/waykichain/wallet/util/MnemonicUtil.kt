package org.waykichain.wallet.util

import com.google.common.collect.ImmutableList
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.MnemonicCode
import java.security.SecureRandom
import java.util.*

class MnemonicUtil {

    companion object {
        private val SECURE_RANDOM = SecureRandom()
        fun validateMnemonics(mnemonicCodes: List<String>) {
            try {
                MnemonicCode.INSTANCE.check(mnemonicCodes)
            } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicLengthException) {
                throw TokenException(Messages.MNEMONIC_INVALID_LENGTH)
            } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicWordException) {
                throw TokenException(Messages.MNEMONIC_BAD_WORD)
            } catch (e: Exception) {
                throw TokenException(Messages.MNEMONIC_INVALID_CHECKSUM)
            }
        }

        fun randomMnemonicCodes(): List<String> {
            return toMnemonicCodes(generateRandomBytes(16))
        }

        fun generateRandomBytes(size: Int): ByteArray {
            val bytes = ByteArray(size)
            SECURE_RANDOM.nextBytes(bytes)
            return bytes
        }

        fun toMnemonicCodes(entropy: ByteArray): List<String> {
            try {
                return MnemonicCode.INSTANCE.toMnemonic(entropy)
            } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicLengthException) {
                throw TokenException(Messages.MNEMONIC_INVALID_LENGTH)
            } catch (e: Exception) {
                throw TokenException(Messages.MNEMONIC_INVALID_CHECKSUM)
            }
        }

        fun generatePath(path: String): ImmutableList<ChildNumber> {
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
}