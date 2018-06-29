package site.yuyanjia.template.common.config;

import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

/**
 * SerializableSimpleByteSource
 *
 * @author seer
 * @date 2018/6/29 23:17
 */
public class SerializableSimpleByteSource extends SimpleByteSource implements Serializable {
    private static final long serialVersionUID = -188069124124976230L;


    public SerializableSimpleByteSource(byte[] bytes) {
        super(bytes);
    }

    public SerializableSimpleByteSource(char[] chars) {
        super(chars);
    }

    public SerializableSimpleByteSource(String string) {
        super(string);
    }

    public SerializableSimpleByteSource(ByteSource source) {
        super(source);
    }

    public SerializableSimpleByteSource(File file) {
        super(file);
    }

    public SerializableSimpleByteSource(InputStream stream) {
        super(stream);
    }

}
