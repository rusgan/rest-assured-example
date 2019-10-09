package org.sora.utils;

import io.github.novacrypto.bip39.MnemonicGenerator;
import io.github.novacrypto.bip39.wordlists.English;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

public class MnemonicUtil {

    public static String[] splitToArray(String mnemonic) {
        return mnemonic.trim().replaceAll(" +", ",").split(",");
    }

    public static String generateMnemonic(byte[] entropy) {
        StringBuilder sb = new StringBuilder();
        new MnemonicGenerator(English.INSTANCE)
                .createMnemonic(entropy, sb::append);
        return sb.toString();
    }

    public static boolean checkMnemonic(String[] mnemonic) {

        for (String word: mnemonic) {
            if (!EnglishWordList.words.contains(word)) {
                return false;
            }
        }
        return true;
    }


    public static byte[] getBytesFromMnemonic(String mnemonic) {
        return Normalizer.normalize(mnemonic, Normalizer.Form.NFKD).getBytes(StandardCharsets.UTF_8);
    }

}