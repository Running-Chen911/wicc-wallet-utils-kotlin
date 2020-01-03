package com.waykichain.wallet.util

import com.google.common.io.BaseEncoding
import com.waykichain.wallet.transaction.Language
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException
import org.waykichain.wallet.util.Messages.Companion.INVALID_CN_MN
import org.waykichain.wallet.util.TokenException
import java.io.*
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.experimental.and

class WaykiMnemonicCode{
    private val BIP39_CHINESE_SIMPLE_RESOURCE_NAME = "mnemonic/wordlist/chinese_simple.txt"
    private val BIP39_CHINESE_SIMPLE_SHA256 = "bfd683b91db88609fabad8968c7efe4bf69606bf5a49ac4a4ba5e355955670cb"
    private val SECURE_RANDOM = SecureRandom()
    private val BIP39_ENGLISH_RESOURCE_NAME = "mnemonic/wordlist/english.txt"
    val HEX = BaseEncoding.base16().lowerCase()
    companion object {
        private var instance: WaykiMnemonicCode? = null
            get() {
                if (field == null) {
                    field = WaykiMnemonicCode()
                }
                return field
            }

        @Synchronized
        fun create(): WaykiMnemonicCode {
            return instance!!
        }


        fun isChinese(c: Char): Boolean {
            var ub = Character.UnicodeBlock.of(c);
            if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                    || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                    || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                return true;
            }
            return false;
        }
    }

    private fun generateRandomBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        SECURE_RANDOM.nextBytes(bytes)
        return bytes
    }

    fun generateMnemonic(language: Language): List<String> {
        when (language) {
            Language.CHINESE -> {
               // val stream = openDefaultWords(BIP39_CHINESE_SIMPLE_RESOURCE_NAME)
               // val mn = MnemonicCode(stream, BIP39_CHINESE_SIMPLE_SHA256)
                val words=getWordList(BIP39_CHINESE_SIMPLE_RESOURCE_NAME)
                return toMnemonic(generateRandomBytes(16),words)
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

    fun checkMnemonic(mnemonicCodes: List<String>):List<String>{
        var mn= ArrayList<String>(12)
        if(isChinese(mnemonicCodes?.get(0).get(0))){

            mnemonicCodes?.forEach{it->
                if (!isChinese(it.get(0))) throw TokenException(INVALID_CN_MN)
            }


            var lists= ArrayList<Int>(12)
           var wordList_zh =getWordList(BIP39_CHINESE_SIMPLE_RESOURCE_NAME)
            var wordList_en =getWordList(BIP39_ENGLISH_RESOURCE_NAME)
            if (wordList_zh.size != 2048||wordList_en.size != 2048)
                throw IllegalArgumentException("input stream did not contain 2048 words")
            mnemonicCodes.forEach{ item_zh->
                wordList_zh.forEachIndexed{index,item->
                    if(item_zh.equals(item)){
                        lists.add(index)
                    }
               }
            }
            lists.forEach{
                mn.add(wordList_en.get(it))
            }
        }else{
            mn=mnemonicCodes as ArrayList<String>
        }
        MnemonicCode.INSTANCE.check(mn)
        return mn
    }

    fun getWordList(path: String):ArrayList<String>{
        val stream = openDefaultWords(path)
        val br = BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8))
        var wordList = ArrayList<String>(2048)
        val md = Sha256Hash.newDigest()
        var word: String? = null
        do {
            word = br.readLine()
            if (word == null) {
                break
            }
            md.update(word.toByteArray())
            wordList.add(word)
        } while (true)
        br.close()
        return wordList
    }

    @Throws(MnemonicException.MnemonicLengthException::class)
    fun toMnemonic(entropy: ByteArray,wordsList:ArrayList<String>): List<String> {
        if (entropy.size % 4 > 0)
            throw MnemonicException.MnemonicLengthException("Entropy length not multiple of 32 bits.")

        if (entropy.size == 0)
            throw MnemonicException.MnemonicLengthException("Entropy is empty.")

        // We take initial entropy of ENT bits and compute its
        // checksum by taking first ENT / 32 bits of its SHA256 hash.

        val hash = Sha256Hash.hash(entropy)
        val hashBits = bytesToBits(hash)

        val entropyBits = bytesToBits(entropy)
        val checksumLengthBits = entropyBits.size / 32

        // We append these bits to the end of the initial entropy.
        val concatBits = BooleanArray(entropyBits.size + checksumLengthBits)
        System.arraycopy(entropyBits, 0, concatBits, 0, entropyBits.size)
        System.arraycopy(hashBits, 0, concatBits, entropyBits.size, checksumLengthBits)

        // Next we take these concatenated bits and split them into
        // groups of 11 bits. Each group encodes number from 0-2047
        // which is a position in a wordlist.  We convert numbers into
        // words and use joined words as mnemonic sentence.

        val words = java.util.ArrayList<String>()
        val nwords = concatBits.size / 11
        for (i in 0 until nwords) {
            var index = 0
            for (j in 0..10) {
                index = index shl 1
                if (concatBits[i * 11 + j])
                    index = index or 0x1
            }
            words.add(wordsList.get(index))
        }
        return words
    }

    private fun bytesToBits(data: ByteArray): BooleanArray {
        val bits = BooleanArray(data.size * 8)
        for (i in data.indices)
            for (j in 0..7)
                bits[i * 8 + j] = data[i].toInt() and (1 shl 7 - j) != 0
        return bits
    }

}