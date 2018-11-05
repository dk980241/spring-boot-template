package site.yuyanjia.template.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5算法
 *
 * @author seer
 * @date 2018/6/19 16:07
 */
public class Md5Encrypter {

    /**
     * 默认编码
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 十六进制数组
     */
    private static final char[] HEXADECIMAL = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 计算md5
     *
     * @param srcBytes
     * @return
     */
    private static byte[] encode(byte[] srcBytes) {
        byte[] md5Bytes;
        try {
            md5Bytes = MessageDigest.getInstance("MD5").digest(srcBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return md5Bytes;
    }

    /**
     * 计算md5
     *
     * @param srcBytes
     * @return
     */
    public static String encodeByte(byte[] srcBytes) {
        byte[] md5Bytes = encode(srcBytes);
        char[] buffer = new char[32];
        for (int i = 0; i < HEXADECIMAL.length; i++) {
            int low = md5Bytes[i] & 0x0f;
            int high = (md5Bytes[i] & 0xf0) >> 4;
            buffer[i * 2] = HEXADECIMAL[high];
            buffer[i * 2 + 1] = HEXADECIMAL[low];
        }
        return new String(buffer);
    }

    /**
     * 计算md5
     *
     * @param srcString
     * @return
     */
    public static String encodeStr(String srcString) {
        return encodeStr(srcString, DEFAULT_ENCODING);
    }

    /**
     * 计算md5
     *
     * @param srcString
     * @param encoding
     * @return
     */
    public static String encodeStr(String srcString, String encoding) {
        byte[] srcBytes;
        try {
            srcBytes = srcString.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return encodeByte(srcBytes);
    }
}
