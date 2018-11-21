package site.yuyanjia.template.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.util.Assert;
import site.yuyanjia.template.common.contant.RedisKeyContant;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * mybatis redis 二级缓存
 *
 * @author seer
 * @date 2018/3/13 14:17
 */
@Slf4j
public class MybatisRedisCache implements Cache {

    private final String id;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static RedisTemplate redisTemplate;

    public static void setRedisTemplate(RedisTemplate redisTemplate) {
        MybatisRedisCache.redisTemplate = redisTemplate;
    }

    public MybatisRedisCache(final String id) {
        Assert.notNull(id, "mybatis redis cache need an id.");
        this.id = id;
        log.debug("mybatis redis cache ID: {}", id);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        log.debug("mybatis redis cache put. ID={} K={} V={}", id, key, value);
        redisTemplate.opsForValue().set(generateKey(key), value);
    }

    @Override
    public Object getObject(Object key) {
        Assert.notNull(redisTemplate, "redisTemplate is null");
        log.debug("mybatis redis cache get. ID={} K={}", id, key);
        return redisTemplate.opsForValue().get(generateKey(key));
    }

    @Override
    public Object removeObject(Object key) {
        log.debug("mybatis redis cache remove. ID={} K={}", id, key);
        Object result = redisTemplate.opsForValue().get(generateKey(key));

        redisTemplate.delete(generateKey(key));
        return result;
    }

    @Override
    public void clear() {
        log.debug("mybatis redis cache clear. ID={}", id);
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        Assert.notNull(redisConnection, "redisConnection is null");
        Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions()
                .match(generateKey("*"))
                .count(Integer.MAX_VALUE)
                .build());
        while (cursor.hasNext()) {
            redisConnection.del(cursor.next());
        }
        try {
            cursor.close();
        } catch (IOException e) {
            log.error("mybatis redis cache clear exception", e);
        } finally {
            redisConnection.close();
        }
    }

    @Override
    public int getSize() {
        AtomicInteger count = new AtomicInteger(0);
        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
        Assert.notNull(redisConnection, "redisConnection is null");
        Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions()
                .match(generateKey("*"))
                .count(Integer.MAX_VALUE)
                .build());
        while (cursor.hasNext()) {
            count.getAndIncrement();
        }
        try {
            cursor.close();
        } catch (IOException e) {
            log.error("mybatis redis cache get size exception", e);
        } finally {
            redisConnection.close();
        }
        return count.get();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    /**
     * 重组key
     * 区别其他使用环境的key
     *
     * @param key
     * @return
     */
    private String generateKey(Object key) {
        String keyStr = RedisKeyContant.MYBATIS_CACHE_PREFIX + id + ":" + key;
        keyStr = keyStr.replaceAll("\\n", "").replaceAll(" ", "");
        return keyStr;
    }
}
