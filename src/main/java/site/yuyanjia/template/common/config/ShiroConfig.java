package site.yuyanjia.template.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import site.yuyanjia.template.website.realm.WebUserRealm;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * shiro配置
 *
 * @author seer
 * @date 2018/2/1 15:41
 */
@Configuration
@AutoConfigureAfter(RedisConfig.class)
@ConfigurationProperties(prefix = ShiroConfig.PREFIX)
@Slf4j
public class ShiroConfig {

    public static final String PREFIX = "yuyanjia.shiro";

    /**
     * 登录路径
     */
    private String loginUrl = "";

    /**
     * 登出路径
     */
    private String logoutUrl = "";

    /**
     * 匿名路径
     */
    private List<String> anonUrlList;

    /**
     * 权限路径
     */
    private List<String> authcUrlList;
    /**
     * 散列算法
     */
    private String hashAlgorithm = "MD5";

    /**
     * 散列迭代次数
     */
    private Integer hashIterations = 2;

    /**
     * 缓存 key 前缀
     */
    private static final String SHIRO_REDIS_CACHE_KEY_PREFIX = ShiroConfig.class.getName() + "_shiro.redis.cache_";

    /**
     * session key 前缀
     */
    private static final String SHIRO_REDIS_SESSION_KEY_PREFIX = ShiroConfig.class.getName() + "shiro.redis.session_";

    /**
     * shiro 过滤器
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put(loginUrl, DefaultFilter.anon.toString());
        filterChainDefinitionMap.put(logoutUrl, DefaultFilter.logout.toString());
        if (CollectionUtils.isNotEmpty(authcUrlList)) {
            authcUrlList.forEach(
                    s -> filterChainDefinitionMap.put(s, DefaultFilter.authc.toString())
            );
        }
        if (CollectionUtils.isNotEmpty(anonUrlList)) {
            anonUrlList.forEach(
                    s -> filterChainDefinitionMap.put(s, DefaultFilter.anon.toString())
            );
        }
        filterChainDefinitionMap.put("/**", DefaultFilter.anon.toString());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(DefaultFilter.authc.toString(), new WebUserFilter());
        filterMap.put(DefaultFilter.perms.toString(), new WebPermissionsAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     * org.apache.shiro.mgt.SecurityManager
     *
     * @return
     */
    @Bean
    public SecurityManager securityManager(WebUserRealm userRealm, DefaultWebSessionManager shiroRedisSessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(shiroRedisSessionManager);
        return securityManager;
    }

    /**
     * 凭证计算匹配
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName(hashAlgorithm);
        hashedCredentialsMatcher.setHashIterations(hashIterations);
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

    /**
     * 用户Realm
     * <p>
     * SQL已经实现缓存 {@link site.yuyanjia.template.common.mapper.WebUserMapper}
     *
     * @return
     */
    @Bean
    public WebUserRealm userRealm(CacheManager shiroRedisCacheManager, HashedCredentialsMatcher hashedCredentialsMatcher) {
        WebUserRealm userRealm = new WebUserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        userRealm.setCachingEnabled(false);
        userRealm.setAuthenticationCachingEnabled(false);
        userRealm.setAuthorizationCachingEnabled(false);
        userRealm.setCacheManager(shiroRedisCacheManager);
        return userRealm;
    }

    /**
     * 缓存管理器
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager shiroRedisCacheManager(RedisTemplate redisTemplate) {
        return new CacheManager() {
            @Override
            public <K, V> Cache<K, V> getCache(String s) throws CacheException {
                log.trace("shiro redis cache manager get cache. name={} ", s);

                return new Cache<K, V>() {
                    @Override
                    public V get(K k) throws CacheException {
                        log.trace("shiro redis cache get.{} K={}", s, k);
                        return ((V) redisTemplate.opsForValue().get(generateCacheKey(s, k)));
                    }

                    @Override
                    public V put(K k, V v) throws CacheException {
                        log.trace("shiro redis cache put.{} K={} V={}", s, k, v);
                        V result = (V) redisTemplate.opsForValue().get(generateCacheKey(s, k));

                        redisTemplate.opsForValue().set(generateCacheKey(s, k), v);
                        return result;
                    }

                    @Override
                    public V remove(K k) throws CacheException {
                        log.trace("shiro redis cache remove.{} K={}", s, k);
                        V result = (V) redisTemplate.opsForValue().get(generateCacheKey(s, k));

                        redisTemplate.delete(generateCacheKey(s, k));
                        return result;
                    }

                    @Override
                    public void clear() throws CacheException {
                        log.trace("shiro redis cache clear.{}", s);
                        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
                        Assert.notNull(redisConnection, "redisConnection is null");
                        try (Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions()
                                .match(generateCacheKey(s, "*"))
                                .count(Integer.MAX_VALUE)
                                .build())) {
                            while (cursor.hasNext()) {
                                redisConnection.del(cursor.next());
                            }
                        } catch (IOException e) {
                            log.error("shiro redis cache clear exception", e);
                        }
                    }

                    @Override
                    public int size() {
                        log.trace("shiro redis cache size.{}", s);
                        AtomicInteger count = new AtomicInteger(0);
                        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
                        Assert.notNull(redisConnection, "redisConnection is null");
                        try (Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions()
                                .match(generateCacheKey(s, "*"))
                                .count(Integer.MAX_VALUE)
                                .build())) {
                            while (cursor.hasNext()) {
                                count.getAndIncrement();
                            }
                        } catch (IOException e) {
                            log.error("shiro redis cache size exception", e);
                        }
                        return count.get();
                    }

                    @Override
                    public Set<K> keys() {
                        log.trace("shiro redis cache keys.{}", s);
                        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
                        Set<K> keys = new HashSet<>();
                        RedisConnection redisConnection = redisTemplate.getConnectionFactory().getConnection();
                        Assert.notNull(redisConnection, "redisConnection is null");
                        try (Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions()
                                .match(generateCacheKey(s, "*"))
                                .count(Integer.MAX_VALUE)
                                .build())) {
                            while (cursor.hasNext()) {
                                keys.add((K) stringRedisSerializer.deserialize(cursor.next()));
                            }
                        } catch (IOException e) {
                            log.error("shiro redis cache keys exception", e);
                        }
                        return keys;
                    }

                    @Override
                    public Collection<V> values() {
                        return null;
                    }
                };
            }
        };
    }


    /**
     * session管理器
     * <p>
     * SimpleSession 使用 JdkSerializationRedisSerializer 进行redis序列化
     * SimpleSession 使用其他方式 进行 redis序列化的时候 老是出现这样那样的问题
     *
     * @param redisTemplateWithJdk
     * @return
     */
    @Bean
    public DefaultWebSessionManager shiroRedisSessionManager(RedisTemplate redisTemplateWithJdk) {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(1800000);
        defaultWebSessionManager.setSessionValidationInterval(900000);
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        defaultWebSessionManager.setSessionDAO(
                new AbstractSessionDAO() {
                    @Override
                    protected Serializable doCreate(Session session) {
                        Serializable sessionId = this.generateSessionId(session);
                        log.trace("shiro redis session create. sessionId={}", sessionId);
                        this.assignSessionId(session, sessionId);
                        redisTemplateWithJdk.opsForValue().set(generateSessionKey(sessionId), session, session.getTimeout(), TimeUnit.MILLISECONDS);
                        return sessionId;
                    }

                    @Override
                    protected Session doReadSession(Serializable sessionId) {
                        log.trace("shiro redis session read. sessionId={}", sessionId);
                        return (Session) redisTemplateWithJdk.opsForValue().get(generateSessionKey(sessionId));
                    }

                    @Override
                    public void update(Session session) throws UnknownSessionException {
                        log.trace("shiro redis session update. sessionId={}", session.getId());
                        redisTemplateWithJdk.opsForValue().set(generateSessionKey(session.getId()), session, session.getTimeout(), TimeUnit.MILLISECONDS);
                    }

                    @Override
                    public void delete(Session session) {
                        log.trace("shiro redis session delete. sessionId={}", session.getId());
                        redisTemplateWithJdk.delete(generateSessionKey(session.getId()));
                    }

                    @Override
                    public Collection<Session> getActiveSessions() {
                        Set<Session> sessionSet = new HashSet<>();
                        RedisConnection redisConnection = redisTemplateWithJdk.getConnectionFactory().getConnection();
                        Assert.notNull(redisConnection, "redisConnection is null");
                        try (Cursor<byte[]> cursor = redisConnection.scan(ScanOptions.scanOptions()
                                .match(generateSessionKey("*"))
                                .count(Integer.MAX_VALUE)
                                .build())) {
                            while (cursor.hasNext()) {
                                Session session = (Session) redisTemplateWithJdk.opsForValue().get(cursor.next());
                                sessionSet.add(session);
                            }
                        } catch (IOException e) {
                            log.error("shiro redis session getActiveSessions exception", e);
                        }
                        return sessionSet;
                    }
                }
        );

        return defaultWebSessionManager;
    }

    /**
     * 生成 缓存 key
     *
     * @param name
     * @param key
     * @return
     */
    private String generateCacheKey(String name, Object key) {
        return SHIRO_REDIS_CACHE_KEY_PREFIX + name + "_" + key;
    }

    /**
     * 生成 session key
     *
     * @param key
     * @return
     */
    private String generateSessionKey(Object key) {
        return SHIRO_REDIS_SESSION_KEY_PREFIX + key;
    }

    /**
     * 重写用户filter
     *
     * @author seer
     * @date 2018/6/17 22:30
     */
    class WebUserFilter extends AccessControlFilter {
        @Override
        protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
            if (isLoginRequest(request, response)) {
                response.getWriter().write("{\"response_code\":\"0000\",\"response_msg\":\"登陆成功\"}");
                return true;
            }

            Subject subject = getSubject(request, response);
            if (subject.getPrincipal() != null) {
                response.getWriter().write("{\"response_code\":\"0000\",\"response_msg\":\"登陆成功\"}");
                return true;
            }
            response.getWriter().write("{\"response_code\":\"9999\",\"response_msg\":\"登陆过期\"}");
            return false;
        }

        @Override
        protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
            /*
             * 不做重定向
             */
            return false;
        }
    }

    /**
     * 重写权限filter
     *
     * @author seer
     * @date 2018/6/17 22:41
     */
    class WebPermissionsAuthorizationFilter extends AuthorizationFilter {
        @Override
        protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
            Subject subject = getSubject(request, response);
            String[] perms = (String[]) mappedValue;

            if (ObjectUtils.isEmpty(perms)) {
                return true;
            }

            if (perms.length == 1 && !subject.isPermitted(perms[0])) {
                response.getWriter().write("{\"response_code\":\"9999\",\"response_msg\":\"权限不足\"}");
                return false;
            } else if (!subject.isPermittedAll(perms)) {
                response.getWriter().write("{\"response_code\":\"9999\",\"response_msg\":\"权限不足\"}");
                return false;
            }

            return true;
        }
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public List<String> getAnonUrlList() {
        return anonUrlList;
    }

    public void setAnonUrlList(List<String> anonUrlList) {
        this.anonUrlList = anonUrlList;
    }

    public List<String> getAuthcUrlList() {
        return authcUrlList;
    }

    public void setAuthcUrlList(List<String> authcUrlList) {
        this.authcUrlList = authcUrlList;
    }

    public String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public void setHashAlgorithm(String hashAlgorithm) {
        this.hashAlgorithm = hashAlgorithm;
    }

    public Integer getHashIterations() {
        return hashIterations;
    }

    public void setHashIterations(Integer hashIterations) {
        this.hashIterations = hashIterations;
    }
}
