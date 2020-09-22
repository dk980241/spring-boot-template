package site.yuyanjia.template.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * JdkRedisTemplate
 * <p>
 * 值采用jdk序列化
 *
 * @author seer
 * @date 2020/7/23 10:20
 * @see org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
 */
@Configuration
public class JdkRedisTemplate extends RedisTemplate<String, Object> {

    /**
     * SpringBoot扩展了ClassLoader，进行分离打包的时候，使用到JdkSerializationRedisSerializer的地方
     * 会因为ClassLoader的不同导致加载不到Class
     * 指定使用项目的ClassLoader
     * <p>
     * JdkSerializationRedisSerializer默认使用{@link sun.misc.Launcher.AppClassLoader}
     * SpringBoot默认使用{@link org.springframework.boot.loader.LaunchedURLClassLoader}
     */
    public JdkRedisTemplate(RedisConnectionFactory connectionFactory) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        setKeySerializer(RedisSerializer.string());
        setValueSerializer(new JdkSerializationRedisSerializer(classLoader));
        setHashKeySerializer(RedisSerializer.string());
        setHashValueSerializer(new JdkSerializationRedisSerializer(classLoader));
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }
}
