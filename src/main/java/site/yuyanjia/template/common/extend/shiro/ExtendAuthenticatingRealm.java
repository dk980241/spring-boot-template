package site.yuyanjia.template.common.extend.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ExtendAuthenticatingRealm
 * <p>
 * 代替 {@link org.apache.shiro.realm.AuthenticatingRealm}
 * shiro默认的实现里有个两个final方法 {@link org.apache.shiro.realm.AuthenticatingRealm#init()} {@link org.apache.shiro.realm.AuthenticatingRealm#getAuthenticationInfo(AuthenticationToken)} 无法被springboot代理
 *
 * @author seer
 * @date 2018/7/11 10:37
 */
public abstract class ExtendAuthenticatingRealm extends CachingRealm implements Initializable {

    private static final Logger log = LoggerFactory.getLogger(ExtendAuthenticatingRealm.class);

    private static final AtomicInteger INSTANCE_COUNT = new AtomicInteger();

    private static final String DEFAULT_AUTHORIZATION_CACHE_SUFFIX = ".authenticationCache";

    private CredentialsMatcher credentialsMatcher;

    private Cache<Object, AuthenticationInfo> authenticationCache;

    private boolean authenticationCachingEnabled;

    private String authenticationCacheName;

    private Class<? extends AuthenticationToken> authenticationTokenClass;

    public ExtendAuthenticatingRealm() {
        this(null, new SimpleCredentialsMatcher());
    }

    public ExtendAuthenticatingRealm(CacheManager cacheManager) {
        this(cacheManager, new SimpleCredentialsMatcher());
    }

    public ExtendAuthenticatingRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }

    public ExtendAuthenticatingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        authenticationTokenClass = UsernamePasswordToken.class;

        this.authenticationCachingEnabled = false;

        int instanceNumber = INSTANCE_COUNT.getAndIncrement();
        this.authenticationCacheName = getClass().getName() + DEFAULT_AUTHORIZATION_CACHE_SUFFIX;
        if (instanceNumber > 0) {
            this.authenticationCacheName = this.authenticationCacheName + "." + instanceNumber;
        }

        if (cacheManager != null) {
            setCacheManager(cacheManager);
        }
        if (matcher != null) {
            setCredentialsMatcher(matcher);
        }
    }

    public CredentialsMatcher getCredentialsMatcher() {
        return credentialsMatcher;
    }

    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        this.credentialsMatcher = credentialsMatcher;
    }

    public Class getAuthenticationTokenClass() {
        return authenticationTokenClass;
    }

    public void setAuthenticationTokenClass(Class<? extends AuthenticationToken> authenticationTokenClass) {
        this.authenticationTokenClass = authenticationTokenClass;
    }

    public void setAuthenticationCache(Cache<Object, AuthenticationInfo> authenticationCache) {
        this.authenticationCache = authenticationCache;
    }

    public Cache<Object, AuthenticationInfo> getAuthenticationCache() {
        return this.authenticationCache;
    }

    public String getAuthenticationCacheName() {
        return this.authenticationCacheName;
    }

    public void setAuthenticationCacheName(String authenticationCacheName) {
        this.authenticationCacheName = authenticationCacheName;
    }

    public boolean isAuthenticationCachingEnabled() {
        return this.authenticationCachingEnabled && isCachingEnabled();
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void setAuthenticationCachingEnabled(boolean authenticationCachingEnabled) {
        this.authenticationCachingEnabled = authenticationCachingEnabled;
        if (authenticationCachingEnabled) {
            setCachingEnabled(true);
        }
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        String authcCacheName = this.authenticationCacheName;
        if (authcCacheName != null && authcCacheName.startsWith(getClass().getName())) {
            this.authenticationCacheName = name + DEFAULT_AUTHORIZATION_CACHE_SUFFIX;
        }
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && getAuthenticationTokenClass().isAssignableFrom(token.getClass());
    }

    /**
     * 代替 {@link AuthenticatingRealm#init()}
     */
    @Override
    public void init() {
        getAvailableAuthenticationCache();
        onInit();
    }

    protected void onInit() {
    }

    protected void afterCacheManagerSet() {
        getAvailableAuthenticationCache();
    }

    private Cache<Object, AuthenticationInfo> getAvailableAuthenticationCache() {
        Cache<Object, AuthenticationInfo> cache = getAuthenticationCache();
        boolean authcCachingEnabled = isAuthenticationCachingEnabled();
        if (cache == null && authcCachingEnabled) {
            cache = getAuthenticationCacheLazy();
        }
        return cache;
    }

    private Cache<Object, AuthenticationInfo> getAuthenticationCacheLazy() {

        if (this.authenticationCache == null) {

            log.trace("No authenticationCache instance set.  Checking for a cacheManager...");

            CacheManager cacheManager = getCacheManager();

            if (cacheManager != null) {
                String cacheName = getAuthenticationCacheName();
                log.debug("CacheManager [{}] configured.  Building authentication cache '{}'", cacheManager, cacheName);
                this.authenticationCache = cacheManager.getCache(cacheName);
            }
        }

        return this.authenticationCache;
    }

    private AuthenticationInfo getCachedAuthenticationInfo(AuthenticationToken token) {
        AuthenticationInfo info = null;

        Cache<Object, AuthenticationInfo> cache = getAvailableAuthenticationCache();
        if (cache != null && token != null) {
            log.trace("Attempting to retrieve the AuthenticationInfo from cache.");
            Object key = getAuthenticationCacheKey(token);
            info = cache.get(key);
            if (info == null) {
                log.trace("No AuthorizationInfo found in cache for key [{}]", key);
            } else {
                log.trace("Found cached AuthorizationInfo for key [{}]", key);
            }
        }

        return info;
    }

    private void cacheAuthenticationInfoIfPossible(AuthenticationToken token, AuthenticationInfo info) {
        if (!isAuthenticationCachingEnabled(token, info)) {
            log.debug("AuthenticationInfo caching is disabled for info [{}].  Submitted token: [{}].", info, token);
            return;
        }

        Cache<Object, AuthenticationInfo> cache = getAvailableAuthenticationCache();
        if (cache != null) {
            Object key = getAuthenticationCacheKey(token);
            cache.put(key, info);
            log.trace("Cached AuthenticationInfo for continued authentication.  key=[{}], value=[{}].", key, info);
        }
    }

    protected boolean isAuthenticationCachingEnabled(AuthenticationToken token, AuthenticationInfo info) {
        return isAuthenticationCachingEnabled();
    }

    /**
     * 代替 {@link AuthenticatingRealm#getAuthenticationInfo(AuthenticationToken)}
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        AuthenticationInfo info = getCachedAuthenticationInfo(token);
        if (info == null) {
            info = doGetAuthenticationInfo(token);
            log.debug("Looked up AuthenticationInfo [{}] from doGetAuthenticationInfo", info);
            if (token != null && info != null) {
                cacheAuthenticationInfoIfPossible(token, info);
            }
        } else {
            log.debug("Using cached authentication info [{}] to perform credentials matching.", info);
        }

        if (info != null) {
            assertCredentialsMatch(token, info);
        } else {
            log.debug("No AuthenticationInfo found for submitted AuthenticationToken [{}].  Returning null.", token);
        }

        return info;
    }

    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
        CredentialsMatcher cm = getCredentialsMatcher();
        if (cm != null) {
            if (!cm.doCredentialsMatch(token, info)) {
                //not successful - throw an exception to indicate this:
                String msg = "Submitted credentials for token [" + token + "] did not match the expected credentials.";
                throw new IncorrectCredentialsException(msg);
            }
        } else {
            throw new AuthenticationException("A CredentialsMatcher must be configured in order to verify " +
                    "credentials during authentication.  If you do not wish for credentials to be examined, you " +
                    "can configure an " + AllowAllCredentialsMatcher.class.getName() + " instance.");
        }
    }

    protected Object getAuthenticationCacheKey(AuthenticationToken token) {
        return token != null ? token.getPrincipal() : null;
    }

    protected Object getAuthenticationCacheKey(PrincipalCollection principals) {
        return getAvailablePrincipal(principals);
    }

    @Override
    protected void doClearCache(PrincipalCollection principals) {
        super.doClearCache(principals);
        clearCachedAuthenticationInfo(principals);
    }

    private static boolean isEmpty(PrincipalCollection pc) {
        return pc == null || pc.isEmpty();
    }

    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        if (!isEmpty(principals)) {
            Cache<Object, AuthenticationInfo> cache = getAvailableAuthenticationCache();
            if (cache != null) {
                Object key = getAuthenticationCacheKey(principals);
                cache.remove(key);
            }
        }
    }

    protected abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException;

}
