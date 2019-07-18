package site.yuyanjia.template.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA非对称加密
 *
 * @author seer
 * @date 2019/7/17 19:57
 */
public class RsaUtil {

    private static final Logger log = LoggerFactory.getLogger(RsaUtil.class);

    /**
     * 编码
     */
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * RSA算法
     */
    private static final String ALGORITHM_KEY = "RSA";

    /**
     * 签名算法
     */
    private static final String ALGORITHM_SIGNATURE = "MD5withRSA";

    private static KeyFactory keyFactory;

    private static Cipher cipher;

    private static Signature signature;

    private static Base64.Decoder decoder = Base64.getDecoder();

    private static Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 公钥加密
     *
     * @param original  原始数据
     * @param publicKey 公钥
     * @return
     */
    public static String encryptWithPublicKey(String original, String publicKey) {
        byte[] originalBytes = original.getBytes(CHARSET);
        byte[] publicKeyBytes = decoder.decode(publicKey);
        byte[] encryptBytes = encryptWithPublicKey(originalBytes, publicKeyBytes);
        return null == encryptBytes ? null : encoder.encodeToString(encryptBytes);
    }

    /**
     * 私钥解密
     *
     * @param encrypt    密文数据
     * @param privateKey 私钥
     * @return
     */
    public static String decryptWithPrivateKey(String encrypt, String privateKey) {
        byte[] encryptBytes = decoder.decode(encrypt);
        byte[] privateKeyBytes = decoder.decode(privateKey);
        byte[] originalBytes = decryptWithPrivateKey(encryptBytes, privateKeyBytes);
        return null == originalBytes ? null : new String(originalBytes, CHARSET);
    }

    /**
     * 私钥加密
     *
     * @param original   原始数据
     * @param privateKey 私钥
     * @return
     */
    private static String encryptWithPrivateKey(String original, String privateKey) {
        byte[] originalBytes = original.getBytes(CHARSET);
        byte[] privateKeyBytes = decoder.decode(privateKey);
        byte[] encryptBytes = encryptWithPrivateKey(originalBytes, privateKeyBytes);
        return null == encryptBytes ? null : encoder.encodeToString(encryptBytes);
    }

    /**
     * 公钥解密
     *
     * @param encrypt
     * @param publicKey
     * @return
     */
    private static String decryptWithPublicKey(String encrypt, String publicKey) {
        byte[] encryptBytes = decoder.decode(encrypt);
        byte[] publicKeyBytes = decoder.decode(publicKey);
        byte[] originalBytes = decryptWithPublicKey(encryptBytes, publicKeyBytes);
        return null == originalBytes ? null : new String(originalBytes, CHARSET);
    }

    /**
     * 生成签名
     *
     * @param data
     * @param privateKey
     * @return
     */
    private static String generateSignature(String data, String privateKey) {
        byte[] dataBytes = data.getBytes(CHARSET);
        byte[] privateKeyBytes = decoder.decode(privateKey);
        byte[] signBytes = generateSignature(dataBytes, privateKeyBytes);
        return null == signBytes ? null : encoder.encodeToString(signBytes);
    }

    /**
     * 验证签名
     *
     * @param data
     * @param publicKey
     * @param sign
     * @return
     */
    private static boolean verifySinature(String data, String publicKey, String sign) {
        byte[] dataBytes = data.getBytes(CHARSET);
        byte[] signBytes = decoder.decode(sign);
        byte[] publicKeyBytes = decoder.decode(publicKey);
        return verifySinature(dataBytes, publicKeyBytes, signBytes);
    }


    /**
     * 公钥加密
     *
     * @param originalBytes  原始数据
     * @param publicKeyBytes 公钥
     * @return
     */
    private static byte[] encryptWithPublicKey(byte[] originalBytes, byte[] publicKeyBytes) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(originalBytes);
        } catch (Exception e) {
            log.warn("私钥加密失败", e);
            return null;
        }
    }

    /**
     * 私钥解密
     *
     * @param encryptBytes    密文数据
     * @param privateKeyBytes 私钥
     * @return
     */
    private static byte[] decryptWithPrivateKey(byte[] encryptBytes, byte[] privateKeyBytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptBytes);
        } catch (Exception e) {
            log.warn("私钥加密失败", e);
            return null;
        }
    }

    /**
     * 私钥加密
     *
     * @param originalBytes   原始数据
     * @param privateKeyBytes 私钥
     * @return
     */
    private static byte[] encryptWithPrivateKey(byte[] originalBytes, byte[] privateKeyBytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(originalBytes);
        } catch (Exception e) {
            log.warn("私钥加密失败", e);
            return null;
        }
    }

    /**
     * 公钥解密
     *
     * @param encryptBytes   密文数据
     * @param publicKeyBytes 公钥
     * @return
     */
    private static byte[] decryptWithPublicKey(byte[] encryptBytes, byte[] publicKeyBytes) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return cipher.doFinal(encryptBytes);
        } catch (Exception e) {
            log.warn("公钥解密失败", e);
            return null;
        }
    }

    /**
     * 生成签名
     *
     * @param dataBytes
     * @param privateKeyBytes
     * @return
     */
    private static byte[] generateSignature(byte[] dataBytes, byte[] privateKeyBytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        try {
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            signature.initSign(privateKey);
            signature.update(dataBytes);
            return signature.sign();
        } catch (Exception e) {
            log.warn("生成签名失败", e);
            return null;
        }
    }

    /**
     * 验证签名
     *
     * @param dataBytesbyte
     * @param publicKeyBytes
     * @param signBytes
     * @return
     */
    private static boolean verifySinature(byte[] dataBytesbyte, byte[] publicKeyBytes, byte[] signBytes) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        try {
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            signature.initVerify(publicKey);
            signature.update(dataBytesbyte);
            return signature.verify(signBytes);
        } catch (Exception e) {
            log.warn("公钥解密失败", e);
            return false;
        }
    }


    /**
     * 生成密钥对
     * <p>
     * 密钥长度1024
     * 代码内部调用生成一下即可
     */
    private static void generateKeyPair() {
        KeyPairGenerator generator;
        try {
            generator = KeyPairGenerator.getInstance(ALGORITHM_KEY);
        } catch (NoSuchAlgorithmException e) {
            log.warn("", e);
            return;
        }
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        log.info("RSA 公钥：" + Base64.getEncoder().encodeToString(publicKey.getEncoded()));
        log.info("RSA 私钥：" + Base64.getEncoder().encodeToString(privateKey.getEncoded()));
    }

    static {
        try {
            keyFactory = KeyFactory.getInstance(ALGORITHM_KEY);
            cipher = Cipher.getInstance(ALGORITHM_KEY);
            signature = Signature.getInstance(ALGORITHM_SIGNATURE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIsWha+voOJeDh6UfUkBZkhXywe/nN9xE9VliF5KkfeOTI4I0p+hzE7XB8+MreTL8nGgD55e7KBLMwA5spxch929fs5+/opSFh9SFEYRmBcDPL3/J3gu69CUCXK3HXDKc+hS9Y8GsWlWpK+KPRB/xY7UQSODlxFUnER2WdKco65PAgMBAAECgYAEW2dCsqjCjpm4oYQvNkkC+SUWEA2U/lDVvYGBGqrp615AkMwwTfw9eAT5nnmQEkd9AbubhXO6LcDiP+x1c3yaDNIZXs7D5VltKDK979ock7hgbPyLi3MdQg0FbRFNgMZTm5cqcQbV244WdGYA1UEeWhz9uLtNvsrNxw/7WXs+QQJBAMOrpl9gLjnRsyFwN5qmBz+OLhHg3+az1D8tyMfd5KPXUyu3Ux2Q6aYVKDFvtIO5KzqwkG7sS7EEJKjFrKmhQK8CQQC1+Mp9H5TRJo6nHT5l+xxhLZvaM3M/iFN5HBJrGtDZMmtEE3We5gfu6k5M5iUWHaVpA/BDTNEZXPKOI38sa5RhAkBnqiwFwa4b4dZ2c75xx151tH0B6dKVplZfdoE2Kn/0saKVuZVmR50R6YmWz2iilNqrSFns+nGLgfduFvRIFm9vAkAkQGwWgTQkdon0wN6tclk1AuEJlzZeccovVmhU7gKtQl4cMcQ845axAfyXVUIOJdnOEfrLbYI6i0nHB0tOjiABAkACald7s8j4eAezZF+Xmtqb3aVr1boUCuuTCyVBc1ta7R7UOXXIMWh+xVcrjhB13IPnvic6OUIslJBqk66Oa1H/";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLFoWvr6DiXg4elH1JAWZIV8sHv5zfcRPVZYheSpH3jkyOCNKfocxO1wfPjK3ky/JxoA+eXuygSzMAObKcXIfdvX7Ofv6KUhYfUhRGEZgXAzy9/yd4LuvQlAlytx1wynPoUvWPBrFpVqSvij0Qf8WO1EEjg5cRVJxEdlnSnKOuTwIDAQAB";
        String data = "{\"username\":\"yuyanjia\",\"password\":\"123456\"}";
        String privateEncrypt = encryptWithPrivateKey(data, privateKey);
        log.info("---私钥加密 encrypt: [{}]", privateEncrypt);
        String publicOriginal = decryptWithPublicKey(privateEncrypt, publicKey);
        log.info("---公钥解密 original: [{}]", publicOriginal);

        String publicEncrypt = encryptWithPublicKey(data, publicKey);
        log.warn("+++公钥加密 encrypt: [{}]", publicEncrypt);
        String privateOriginal = decryptWithPrivateKey(publicEncrypt, privateKey);
        log.warn("+++私钥解密 original: [{}]", privateOriginal);

        String sign = generateSignature(data, privateKey);
        log.error("===生成签名 sign: [{}]", sign);
        boolean verify = verifySinature(data, publicKey, sign);
        log.error("===验证签名 verfiy: [{}]", verify);
    }
}
