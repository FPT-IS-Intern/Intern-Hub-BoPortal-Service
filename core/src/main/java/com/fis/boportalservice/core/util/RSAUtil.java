package com.fis.boportalservice.core.util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RSAUtil {

    private RSAUtil() {}

    public static final String RSA_ALGORITHM = "RSA";
    private static final String RSA_ECB_PKCS_1_PADDING = "RSA/ECB/PKCS1Padding";

    public static String decrypt(String data, String base64PrivateKey) {
        try {
            return decrypt(
                    Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)),
                    getPrivateKey(base64PrivateKey));
        } catch (Exception e) {
            log.error("decrypt error: {}", e.getMessage());
        }
        return null;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        base64PrivateKey =
                base64PrivateKey
                        .replaceAll("\\\\n", "")
                        .replaceAll("\\r", "")
                        .replace("-----BEGIN PRIVATE KEY-----", "")
                        .replace("-----END PRIVATE KEY-----", "")
                        .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(base64PrivateKey.getBytes(StandardCharsets.UTF_8));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        return keyFactory.generatePrivate(spec);
    }

    public static String decrypt(byte[] data, PrivateKey privateKey)
            throws NoSuchPaddingException,
                    NoSuchAlgorithmException,
                    InvalidKeyException,
                    BadPaddingException,
                    IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(RSA_ECB_PKCS_1_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(data));
    }
}
