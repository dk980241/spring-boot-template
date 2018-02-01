package online.yuyanjia.template.mobile.util;

import online.yuyanjia.template.mobile.model.DO.UserInfoDO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author seer
 * @date 2018/2/1 14:55
 */
@Service("demoTestSub")
@CacheConfig
public class DemoTestSub {
    private static Logger LOGGER = LogManager.getLogger(DemoTestSub.class);

    /**
     * redis 注解操作
     *
     * @param userInfoDO
     * @return
     */
    @Cacheable(value = "yuyanjia", key = "#userInfoDO.uid.toString()")
    public UserInfoDO use_redis_save_anno(UserInfoDO userInfoDO) {
        userInfoDO.setNickName("Blanche Chen");
        LOGGER.error(userInfoDO);
        return userInfoDO;

        /**
         *  注解：
         *  @Cacheable(value="redis",key="#key"，condition="#key<10") 用于查询的注解，第一次查询的时候返回该方法返回值，并向redis服务器保存数据，以后的查询将不再执行方法体内的代码，而是直接查询redis服务器获取数据并返回。
         *  @CachePut(value="redis",key="#key"，condition="#key<10") 用于更新数据库或新增数据时的注解，更新redis服务器中该value的值
         *  @CacheEvict(value="redis",key="#key") 用于删除数据操作时的注解，删除redis数据库中该value对应的数据
         *  @CacheConfig 用于类上做相关配置
         *
         *  redis:
         *  flushdb 删除所有key
         *  type key 查询key的类型
         *  zrange key 0 100 取zset值
         */
    }
}
