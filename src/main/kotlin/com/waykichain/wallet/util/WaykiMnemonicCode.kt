package com.waykichain.wallet.util

import com.waykichain.wallet.transaction.Language
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.crypto.MnemonicCode
import java.io.*
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class WaykiMnemonicCode{
    private val BIP39_CHINESE_SIMPLE_RESOURCE_NAME = "mnemonic/wordlist/chinese_simple.txt"
    private val BIP39_CHINESE_SIMPLE_SHA256 = "bfd683b91db88609fabad8968c7efe4bf69606bf5a49ac4a4ba5e355955670cb"
    private val SECURE_RANDOM = SecureRandom()
    private val BIP39_ENGLISH_RESOURCE_NAME = "mnemonic/wordlist/english.txt"
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

        /**
         * 是否是英文
         * @param c
         * @return
         */
        fun isEnglish(charaString: String): Boolean {
            return charaString.matches("^[a-zA-Z]*".toRegex());
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
                val stream = openDefaultWords(BIP39_CHINESE_SIMPLE_RESOURCE_NAME)
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

    fun checkMnemonic(mnemonicCodes: List<String>){
        var mn= ArrayList<String>(12)
        if(isChinese(mnemonicCodes?.get(0).get(0))){
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

}
