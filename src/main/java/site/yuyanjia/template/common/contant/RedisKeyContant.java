package site.yuyanjia.template.common.contant;

/**
 * Redis Key 常量
 * <p>
 * 规则   业务[:子标识]:key
 * <p>
 * Redis的key 定义到统一常量类，方便问题追溯
 *
 * @author seer
 * @date 2018/11/21 10:15
 */
public interface RedisKeyContant {

    /**
     * mybatis 缓存
     */
    String MYBATIS_CACHE_PREFIX = "mybatis:cache:";

    /**
     * shiro 缓存
     */
    String SHIRO_CACHE_PREFIX = "shiro:cache:";

    /**
     * shiro session
     */
    String SHIRO_SESSION_PREFIX = "shiro:session:";
}
