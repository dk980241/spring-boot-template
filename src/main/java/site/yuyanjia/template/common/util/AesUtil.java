package site.yuyanjia.template.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

/**
 * AES对称加密
 *
 * @author seer
 * @date 2019/7/17 14:23
 */
public class AesUtil {

    private static final Logger log = LoggerFactory.getLogger(AesUtil.class);

    /**
     * 编码
     */
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * AES算法
     */
    private static final String ALGORITHM_KEY = "AES";

    /**
     * 算法/模式/填充
     */
    private static final String ALGORITHM_CONTENT = "AES/ECB/PKCS5Padding";

    /**
     * key长度
     */
    private static final int KEY_LENGTH = 16;

    /**
     * 随机串字符数组
     */
    private static final String RANDOM_CHAR_ARRAY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static Cipher cipher;

    /**
     * 生成密钥
     * 16位
     *
     * @return
     */
    public static String generateKey() {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < KEY_LENGTH; i++) {
            int idx = random.nextInt(RANDOM_CHAR_ARRAY.length());
            sb.append(RANDOM_CHAR_ARRAY.charAt(idx));
        }
        return sb.toString();
    }

    /**
     * 加密
     *
     * @param original 原始数据
     * @param key      密钥
     * @return
     */
    public static String encrypt(String original, String key) {
        byte[] originalBytes = original.getBytes(CHARSET);
        byte[] keyBytes = key.getBytes(CHARSET);
        byte[] encrypt = encrypt(originalBytes, keyBytes);
        return null == encrypt ? null : Base64.getEncoder().encodeToString(encrypt);
    }

    /**
     * 解密
     *
     * @param encrypt 加密数据
     * @param key     密钥
     * @return
     */
    public static String decrypt(String encrypt, String key) {
        byte[] encryptBytes = Base64.getDecoder().decode(encrypt);
        byte[] keyBytes = key.getBytes(CHARSET);
        byte[] original = decrypt(encryptBytes, keyBytes);
        return null == original ? null : new String(original, CHARSET);
    }

    /**
     * 加密
     *
     * @param originalBytes 原始数据
     * @param keyBytes      密钥
     * @return
     */
    private static byte[] encrypt(byte[] originalBytes, byte[] keyBytes) {
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM_KEY);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(originalBytes);
        } catch (Exception e) {
            log.warn("加密失败", e);
            return null;
        }

    }

    /**
     * 解密
     *
     * @param encryptBytes 加密数据
     * @param keyBytes     密钥
     * @return
     */
    private static byte[] decrypt(byte[] encryptBytes, byte[] keyBytes) {
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM_KEY);
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(encryptBytes);
        } catch (Exception ex) {
            log.warn("解密失败", ex);
            return null;
        }
    }

    static {
        try {
            cipher = Cipher.getInstance(ALGORITHM_CONTENT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        //language=JSON
        String data = "{\"username\":\"yuyanjia\",\"password\":\"dafdf\"}";
        String key = generateKey();
        log.info("key: [{}]", key);

        String encrypt = encrypt(data, key);
        log.info("encryptStr: [{}]", encrypt);

        String original = decrypt(encrypt, key);
        log.info("original: [{}]", original);
    }
}
