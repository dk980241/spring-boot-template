package site.yuyanjia.template.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片工具类
 *
 * @author seer
 * @date 2018/6/13 9:11
 */
@Component
public class ImageUtil {

    /**
     * 图片base64前缀
     */
    private static final String IMAGE_BASE64_DATA = "data";

    /**
     * 静态资源目录前缀·
     */
    private static final String STATIC_LOCATION_PREFIX = "file:";

    /**
     * 静态资源路径
     */
    private static String staticLocation;

    public static void setStaticLocation(String staticLocation) {
        if (staticLocation.startsWith(ImageUtil.STATIC_LOCATION_PREFIX)) {
            staticLocation = staticLocation.substring(ImageUtil.STATIC_LOCATION_PREFIX.length());
        }
        ImageUtil.staticLocation = staticLocation;
    }

    /**
     * 构建图片
     *
     * @param imgBase64             base64字符串
     * @param relativeDirectoryPath 相对目录路径
     * @param fileName              保存文件名
     * @return 相对文件路径
     * @throws IOException
     */
    public static synchronized String buildImage(String imgBase64, String relativeDirectoryPath, String fileName, PrefixEnum prefixEnum) throws IOException {
        if (StringUtils.startsWith(imgBase64, IMAGE_BASE64_DATA)) {
            int inx = StringUtils.indexOf(imgBase64, ",");
            imgBase64 = StringUtils.substring(imgBase64, inx + 1);
        }

        byte[] bytes = Base64Utils.decodeFromString(imgBase64);
        for (int i = 0; i < bytes.length; ++i) {
            if (bytes[i] < 0) {
                bytes[i] += 256;
            }
        }

        String relativeFilePath = prefixEnum.toString() + "-" + fileName + ".jpg";

        if (StringUtils.isNotEmpty(relativeDirectoryPath)) {
            if (!relativeDirectoryPath.endsWith(File.separator)) {
                relativeDirectoryPath += File.separator;
            }
            relativeFilePath = relativeDirectoryPath + relativeFilePath;
        }

        String absoluteFilePath = staticLocation + relativeFilePath;

        File file = new File(absoluteFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        try (OutputStream outputStream = new FileOutputStream(absoluteFilePath)) {
            outputStream.write(bytes);
            outputStream.flush();
        }
        return relativeFilePath;
    }

    /**
     * 前缀枚举
     */
    public enum PrefixEnum {
    }

}
