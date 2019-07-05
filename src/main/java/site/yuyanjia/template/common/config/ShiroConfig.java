package site.yuyanjia.template.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;
import site.yuyanjia.template.common.contant.RedisKeyContant;
import site.yuyanjia.template.common.contant.ResultEnum;
import site.yuyanjia.template.common.util.ResponseUtil;
import site.yuyanjia.template.website.realm.WebUserRealm;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * shiro配置
 *
 * @author seer
 * @date 2018/2/1 15:41
 */
// @Configuration
// @AutoConfigureAfter(RedisConfig.class)
@ConfigurationProperties(prefix = ShiroConfig.PREFIX)
@Slf4j
public class ShiroConfig {

    public static final String PREFIX = "yuyanjia.shiro";

    /**
     * Url和Filter匹配关系
     */
    private List<String> urlFilterList = new ArrayList<>();

    /**
     * 散列算法
     */
    private String hashAlgorithm = "MD5";

    /**
     * 散列迭代次数
     */
    private Integer hashIterations = 2;

    /**
     * Filter 工厂
     * <p>
     * 通过自定义 Filter 实现校验逻辑的重写和返回值的定义 {@link ShiroFilterFactoryBean#setFilters(java.util.Map)
     * 对一个 URL 要进行多个 Filter 的校验。通过 {@link ShiroFilterFactoryBean#setFilterChainDefinitions(java.lang.String)} 实现
     * 通过 {@link ShiroFilterFactoryBean#setFilterChainDefinitionMap(java.util.Map)} 实现的拦截不方便实现实现多 Filter 校验，所以这里没有使用
     * <p>
     * 权限的名称可以随便指定的，和 URL 配置的 Filter 有关，这里使用 {@link DefaultFilter} 默认的的权限定义，覆盖了原权限拦截器
     * 授权Filter {@link WebUserFilter}
     * 权限Filter {@link WebPermissionsAuthorizationFilter}
     * 登出Filter {@link WebLogoutFilter}
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put(DefaultFilter.authc.toString(), new WebUserFilter());
        filterMap.put(DefaultFilter.perms.toString(), new WebPermissionsAuthorizationFilter());
        filterMap.put(DefaultFilter.logout.toString(), new WebLogoutFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        StringBuilder stringBuilder = new StringBuilder();
        urlFilterList.forEach(s -> stringBuilder.append(s).append("\n"));
        shiroFilterFactoryBean.setFilterChainDefinitions(stringBuilder.toString());

        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     *
     * @param userRealm                自定义 realm {@link #userRealm(CacheManager, HashedCredentialsMatcher)}
     * @param shiroRedisSessionManager 自定义 session 管理器 {@link #shiroRedisSessionManager(RedisTemplate)}
     * @return @link org.apache.shiro.mgt.SecurityManager}
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
     * shiro默认缓存这里还有点坑需要填
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
     * @param redisTemplateWithJdk shiro的对象总是有这样那样的问题，所以 redisTemplate 使用 {@link org.springframework.data.redis.serializer.JdkSerializationRedisSerializer} 序列化值
     * @return
     */
    @Bean
    public CacheManager shiroRedisCacheManager(RedisTemplate redisTemplateWithJdk) {
        return new CacheManager() {
            @Override
            public <K, V> Cache<K, V> getCache(String s) throws CacheException {
                log.trace("shiro redis cache manager get cache. name={} ", s);

                return new Cache<K, V>() {
                    @Override
                    public V get(K k) throws CacheException {
                        log.trace("shiro redis cache get.{} K={}", s, k);
                        return ((V) redisTemplateWithJdk.opsForValue().get(generateCacheKey(s, k)));
                    }

                    @Override
                    public V put(K k, V v) throws CacheException {
                        log.trace("shiro redis cache put.{} K={} V={}", s, k, v);
                        V result = (V) redisTemplateWithJdk.opsForValue().get(generateCacheKey(s, k));

                        redisTemplateWithJdk.opsForValue().set(generateCacheKey(s, k), v);
                        return result;
                    }

                    @Override
                    public V remove(K k) throws CacheException {
                        log.trace("shiro redis cache remove.{} K={}", s, k);
                        V result = (V) redisTemplateWithJdk.opsForValue().get(generateCacheKey(s, k));

                        redisTemplateWithJdk.delete(generateCacheKey(s, k));
                        return result;
                    }

                    /**
                     * clear
                     * <p>
                     *     redis keys 命令会造成堵塞
                     *     redis scan 命令不会造成堵塞
                     *
                     * @throws CacheException
                     */
                    @Override
                    public void clear() throws CacheException {
                        log.trace("shiro redis cache clear.{}", s);
                        RedisConnection redisConnection = redisTemplateWithJdk.getConnectionFactory().getConnection();
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
                        RedisConnection redisConnection = redisTemplateWithJdk.getConnectionFactory().getConnection();
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
                        RedisConnection redisConnection = redisTemplateWithJdk.getConnectionFactory().getConnection();
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
     *
     * @param redisTemplateWithJdk shiro的对象总是有这样那样的问题，所以 redisTemplate 使用 {@link org.springframework.data.redis.serializer.JdkSerializationRedisSerializer} 序列化值
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
        return RedisKeyContant.SHIRO_CACHE_PREFIX + name + ":" + key;
    }

    /**
     * 生成 session key
     *
     * @param key
     * @return
     */
    private String generateSessionKey(Object key) {
        return RedisKeyContant.SHIRO_SESSION_PREFIX + key;
    }

    /**
     * 重写用户filter
     * <p>
     * shiro 默认 {@link org.apache.shiro.web.filter.authc.UserFilter}
     *
     * @author seer
     * @date 2018/6/17 22:30
     */
    class WebUserFilter extends AccessControlFilter {
        @Override
        protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
            response.setContentType("application/json");
            if (isLoginRequest(request, response)) {
                return true;
            }

            Subject subject = getSubject(request, response);
            if (subject.getPrincipal() != null) {
                return true;
            }
            response.getWriter().write(ResponseUtil.resultFailed(ResultEnum.用户登陆过期));
            return false;
        }

        /**
         * 访问拒绝时，不错任何处理
         *
         * @param request
         * @param response
         * @return
         * @throws Exception
         */
        @Override
        protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
            return false;
        }
    }


    /**
     * 重写权限filter
     * <p>
     * shiro 默认 {@link org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter}
     * <p>
     * 前后端分离项目，直接获取url进行匹配，后台配置的权限的值就是请求路径 {@link WebUserRealm#doGetAuthorizationInfo(PrincipalCollection)}
     *
     * @author seer
     * @date 2018/6/17 22:41
     */
    class WebPermissionsAuthorizationFilter extends AuthorizationFilter {
        @Override
        protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
            Subject subject = getSubject(request, response);
            HttpServletRequest httpServletRequest = ((HttpServletRequest) request);
            String url = httpServletRequest.getServletPath();
            if (subject.isPermitted(url)) {
                return true;
            }
            response.getWriter().write(ResponseUtil.resultFailed(ResultEnum.权限不足));
            return false;
        }

        /**
         * 访问拒绝时不作任何处理
         *
         * @param request
         * @param response
         * @return
         * @throws IOException
         */
        @Override
        protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
            return false;
        }
    }

    /**
     * 重写登出filter
     * shiro 默认 {@link LogoutFilter}
     *
     * @author seer
     * @date 2018/6/26 2:09
     */
    class WebLogoutFilter extends LogoutFilter {
        @Override
        protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
            response.setContentType("application/json");
            response.getWriter().write(ResponseUtil.resultSuccess());
            Subject subject = getSubject(request, response);

            if (isPostOnlyLogout()) {
                if (!WebUtils.toHttp(request).getMethod().toUpperCase(Locale.ENGLISH).equals(HttpMethod.POST.toString())) {
                    return onLogoutRequestNotAPost(request, response);
                }
            }
            try {
                subject.logout();
            } catch (SessionException ise) {
                log.trace("Encountered session exception during logout.  This can generally safely be ignored.", ise);
            }
            return false;
        }
    }

    public List<String> getUrlFilterList() {
        return urlFilterList;
    }

    public void setUrlFilterList(List<String> urlFilterList) {
        this.urlFilterList = urlFilterList;
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
