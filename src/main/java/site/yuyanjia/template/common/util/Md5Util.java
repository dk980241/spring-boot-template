package site.yuyanjia.template.common.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5信息摘要
 *
 * @author seer
 * @date 2018/6/19 16:07
 */
public class Md5Util {

    /**
     * 默认编码
     */
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * 十六进制数组
     */
    private static final String HEXADECIMAL = "0123456789abcdef";

    private static MessageDigest digest;

    /**
     * 计算md5
     *
     * @param data
     * @return 16位小写
     */
    public static String encode16(String data) {
        return encode(data).substring(8, 24);
    }

    /**
     * 计算MD5
     *
     * @param data
     * @return 32位小写
     */
    public static String encode(String data) {
        byte[] dataBytes = data.getBytes(CHARSET);
        return encodeByte(dataBytes);
    }

    /**
     * 计算MD5
     *
     * @param dataBytes
     * @return 32位小写
     */
    public static String encodeByte(byte[] dataBytes) {
        byte[] md5Bytes = digest.digest(dataBytes);
        char[] buffer = new char[32];
        for (int i = 0; i < HEXADECIMAL.length(); i++) {
            int low = md5Bytes[i] & 0x0f;
            int high = (md5Bytes[i] & 0xf0) >> 4;
            buffer[i * 2] = HEXADECIMAL.charAt(high);
            buffer[i * 2 + 1] = HEXADECIMAL.charAt(low);
        }
        return new String(buffer);
    }

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        //language=JSON
        String data = "{\"username\":\"yuyanjia\",\"password\":\"dafdf\"}";
        String md5 = encode(data);
        System.out.println(md5.toUpperCase());
        String md16 = encode16(data);
        System.out.println(md16);
    }
}
