/*
package com.waykichain.wallet;

import com.waykichain.wallet.encode.UCoinDest;
import com.waykichain.wallet.transaction.LegacyWallet;
import com.waykichain.wallet.transaction.CoinType;
import com.waykichain.wallet.transaction.params.WaykiCommonTxParams;
import com.waykichain.wallet.transaction.params.WaykiTestNetParams;
import com.waykichain.wallet.transaction.params.WaykiUCoinContractTxParams;
import com.waykichain.wallet.transaction.params.WaykiUCoinTxParams;
import com.waykichain.wallet.util.ContractUtil;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.LegacyAddress;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.waykichain.wallet.util.MnemonicUtil;

import java.util.Arrays;
import java.util.List;

public class TestTx {
    private Logger logger = LoggerFactory.getLogger(TestTx.class);

    */
/*
    * 生成助记词
    * generate Mnemonic
    * *//*

    @Test
    public void generateMnemonic(){
      List<String> words=MnemonicUtil.Companion.randomMnemonicCodes();
      logger.info(words.toString());
    }

    */
/*
    * 生成钱包
    * generate wicc Wallet
    * *//*

    @Test
    public void generateWalletFromMnemonic(){
       */
/* String words = "vote despair mind rescue crumble choice garden elite venture cattle oxygen voyage";
        WaykiTestNetParams networkParameters = WaykiTestNetParams.Companion.getInstance(); //generate Testnet Address From Mnemonic
       // WaykiMainNetParams networkParameters = WaykiMainNetParams.Companion.getInstance(); //generate Mainnet Address From Mnemonic
        BIP44Util.WaykiWallet wallet= BIP44Util.Companion.generateWaykiWallet(words,networkParameters);
        logger.info("PrivateKey:"+wallet.getPrivateKey()+"\n"+"Address:"+wallet.getAddress()+"\n"+"PublicKeyKey:"+wallet.getPubKey()+"\n");*//*

    }

    */
/*
    * WICC转账
    * 小费最少0.0001WICC   fee Minimum 0.0001 wicc
    * *//*

    @Test
    public void commonTransaction(){
        LegacyWallet wallet =new LegacyWallet();
        WaykiTestNetParams netParams = WaykiTestNetParams.Companion.getInstance(); // Test net params
        String srcPrivKeyWiF = "Y6J4aK6Wcs4A3Ex4HXdfjJ6ZsHpNZfjaS4B9w7xqEnmFEYMqQd13";// private key
        ECKey srcKey = DumpedPrivateKey.fromBase58(netParams, srcPrivKeyWiF).getKey();
        String pubKey = srcKey.getPublicKeyAsHex(); //wallet public key
        String destAddr = "wWTStcDL4gma6kPziyHhFGAP6xUzKpA5if";//dest address
        String memo="test transfer";//transfer memo
        WaykiCommonTxParams txParams =new WaykiCommonTxParams(ChainIds.Companion.getWAYKICHAIN_MAINNET(), 34550, pubKey,10000,
                1100000000000L, "0-1", destAddr,memo);
        txParams.signTx(srcKey);
        String tx = wallet.createCommonTransactionRaw(txParams);
        logger.info("生成交易Hex:"+tx);
    }

    */
/*
     * 多币种转账交易 ,支持多种币种转账
     * Test nUniversal Coin Transfer Tx
     * fee Minimum 0.0001 wicc
     * *//*

    @Test
    public void  testGenerateUCoinTransferTx() {
        LegacyWallet wallet = new LegacyWallet();
        WaykiTestNetParams netParams = WaykiTestNetParams.Companion.getInstance();

        String srcPrivKeyWiF = "Y6J4aK6Wcs4A3Ex4HXdfjJ6ZsHpNZfjaS4B9w7xqEnmFEYMqQd13";
         ECKey  srcKey = DumpedPrivateKey.fromBase58(netParams, srcPrivKeyWiF).getKey();
        String pubKey = srcKey.getPublicKeyAsHex();  //user publickey hex string
        Long nValidHeight = 838L;
        String coinSymbol = CoinType.WICC.getType();  //coind symbol
        Long coinAmount = 1000000000L ;   //transfer amount
        String feeSymbol = CoinType.WICC.getType();
        Long fees = 100000L;
        String regid = "0-1";
        String destAddr = "wiEEFm8v9UqCucynQWw1sdk57iBf57jjYF";
        String memo = "";

        UCoinDest dest1=new UCoinDest(LegacyAddress.fromBase58(netParams,destAddr),coinSymbol,coinAmount);
        List<UCoinDest> dests= Arrays.asList(dest1);

        WaykiUCoinTxParams txParams = new WaykiUCoinTxParams(nValidHeight, regid, pubKey, dests, feeSymbol, fees, memo);
        txParams.signTx(srcKey);
        String tx = wallet.createUCoinTransactionRaw(txParams);
        logger.info("生成交易Hex:"+tx);
    }

    */
/*
     * 多币种合约调用交易
     * Contract transaction sample
     * fee Minimum 0.0001 wicc
     * *//*

    @Test
    public void testGenerateUCoinContractTx() {
        LegacyWallet wallet = new LegacyWallet();
        WaykiTestNetParams netParams = WaykiTestNetParams.Companion.getInstance();

        String srcPrivKeyWiF = "Y6J4aK6Wcs4A3Ex4HXdfjJ6ZsHpNZfjaS4B9w7xqEnmFEYMqQd13";
        ECKey srcKey = DumpedPrivateKey.fromBase58(netParams, srcPrivKeyWiF).getKey();
        logger.info(LegacyAddress.fromPubKeyHash(netParams, srcKey.getPubKeyHash()).toString());

        Long value = 100000000L;
        String appid = "450687-1";
        byte[] contractByte = ContractUtil.Companion.hexString2binaryString("f001");
        WaykiUCoinContractTxParams txParams = new WaykiUCoinContractTxParams(srcKey.getPrivateKeyAsHex(), 727702,
                100000, value, "0-1",
                appid, contractByte, CoinType.WICC.getType(),CoinType.WUSD.getType());
        txParams.signTx(srcKey);
        String tx = wallet.createUCoinContractInvokeRaw(txParams);
        logger.info("生成交易Hex:"+tx);
    }

}
*/
