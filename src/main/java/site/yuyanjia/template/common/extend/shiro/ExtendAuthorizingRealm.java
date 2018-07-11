package site.yuyanjia.template.common.extend.shiro;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.PermissionResolverAware;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolverAware;
import org.apache.shiro.authz.permission.WildcardPermissionResolver;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ExtendAuthorizingRealm
 * <p>
 * 代替 {@link org.apache.shiro.realm.AuthorizingRealm}
 *
 * @author seer
 * @date 2018/7/11 10:45
 */
public abstract class ExtendAuthorizingRealm extends ExtendAuthenticatingRealm
        implements Authorizer, Initializable, PermissionResolverAware, RolePermissionResolverAware {
    private static final Logger log = LoggerFactory.getLogger(ExtendAuthorizingRealm.class);

    private static final String DEFAULT_AUTHORIZATION_CACHE_SUFFIX = ".authorizationCache";

    private static final AtomicInteger INSTANCE_COUNT = new AtomicInteger();

    private boolean authorizationCachingEnabled;

    private Cache<Object, AuthorizationInfo> authorizationCache;

    private String authorizationCacheName;

    private PermissionResolver permissionResolver;

    private RolePermissionResolver permissionRoleResolver;

    public ExtendAuthorizingRealm() {
        this(null, null);
    }

    public ExtendAuthorizingRealm(CacheManager cacheManager) {
        this(cacheManager, null);
    }

    public ExtendAuthorizingRealm(CredentialsMatcher matcher) {
        this(null, matcher);
    }

    public ExtendAuthorizingRealm(CacheManager cacheManager, CredentialsMatcher matcher) {
        super();
        if (cacheManager != null) setCacheManager(cacheManager);
        if (matcher != null) setCredentialsMatcher(matcher);

        this.authorizationCachingEnabled = true;
        this.permissionResolver = new WildcardPermissionResolver();

        int instanceNumber = INSTANCE_COUNT.getAndIncrement();
        this.authorizationCacheName = getClass().getName() + DEFAULT_AUTHORIZATION_CACHE_SUFFIX;
        if (instanceNumber > 0) {
            this.authorizationCacheName = this.authorizationCacheName + "." + instanceNumber;
        }
    }

    public void setName(String name) {
        super.setName(name);
        String authzCacheName = this.authorizationCacheName;
        if (authzCacheName != null && authzCacheName.startsWith(getClass().getName())) {
            this.authorizationCacheName = name + DEFAULT_AUTHORIZATION_CACHE_SUFFIX;
        }
    }

    public void setAuthorizationCache(Cache<Object, AuthorizationInfo> authorizationCache) {
        this.authorizationCache = authorizationCache;
    }

    public Cache<Object, AuthorizationInfo> getAuthorizationCache() {
        return this.authorizationCache;
    }

    public String getAuthorizationCacheName() {
        return authorizationCacheName;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void setAuthorizationCacheName(String authorizationCacheName) {
        this.authorizationCacheName = authorizationCacheName;
    }

    public boolean isAuthorizationCachingEnabled() {
        return isCachingEnabled() && authorizationCachingEnabled;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public void setAuthorizationCachingEnabled(boolean authenticationCachingEnabled) {
        this.authorizationCachingEnabled = authenticationCachingEnabled;
        if (authenticationCachingEnabled) {
            setCachingEnabled(true);
        }
    }

    public PermissionResolver getPermissionResolver() {
        return permissionResolver;
    }

    public void setPermissionResolver(PermissionResolver permissionResolver) {
        if (permissionResolver == null) throw new IllegalArgumentException("Null PermissionResolver is not allowed");
        this.permissionResolver = permissionResolver;
    }

    public RolePermissionResolver getRolePermissionResolver() {
        return permissionRoleResolver;
    }

    public void setRolePermissionResolver(RolePermissionResolver permissionRoleResolver) {
        this.permissionRoleResolver = permissionRoleResolver;
    }

    protected void onInit() {
        super.onInit();
        getAvailableAuthorizationCache();
    }

    protected void afterCacheManagerSet() {
        super.afterCacheManagerSet();
        getAvailableAuthorizationCache();
    }

    private Cache<Object, AuthorizationInfo> getAuthorizationCacheLazy() {

        if (this.authorizationCache == null) {

            if (log.isDebugEnabled()) {
                log.debug("No authorizationCache instance set.  Checking for a cacheManager...");
            }

            CacheManager cacheManager = getCacheManager();

            if (cacheManager != null) {
                String cacheName = getAuthorizationCacheName();
                if (log.isDebugEnabled()) {
                    log.debug("CacheManager [" + cacheManager + "] has been configured.  Building " +
                            "authorization cache named [" + cacheName + "]");
                }
                this.authorizationCache = cacheManager.getCache(cacheName);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No cache or cacheManager properties have been set.  Authorization cache cannot " +
                            "be obtained.");
                }
            }
        }

        return this.authorizationCache;
    }

    private Cache<Object, AuthorizationInfo> getAvailableAuthorizationCache() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache == null && isAuthorizationCachingEnabled()) {
            cache = getAuthorizationCacheLazy();
        }
        return cache;
    }

    protected AuthorizationInfo getAuthorizationInfo(PrincipalCollection principals) {

        if (principals == null) {
            return null;
        }

        AuthorizationInfo info = null;

        if (log.isTraceEnabled()) {
            log.trace("Retrieving AuthorizationInfo for principals [" + principals + "]");
        }

        Cache<Object, AuthorizationInfo> cache = getAvailableAuthorizationCache();
        if (cache != null) {
            if (log.isTraceEnabled()) {
                log.trace("Attempting to retrieve the AuthorizationInfo from cache.");
            }
            Object key = getAuthorizationCacheKey(principals);
            info = cache.get(key);
            if (log.isTraceEnabled()) {
                if (info == null) {
                    log.trace("No AuthorizationInfo found in cache for principals [" + principals + "]");
                } else {
                    log.trace("AuthorizationInfo found in cache for principals [" + principals + "]");
                }
            }
        }


        if (info == null) {
            info = doGetAuthorizationInfo(principals);
            if (info != null && cache != null) {
                if (log.isTraceEnabled()) {
                    log.trace("Caching authorization info for principals: [" + principals + "].");
                }
                Object key = getAuthorizationCacheKey(principals);
                cache.put(key, info);
            }
        }

        return info;
    }

    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        return principals;
    }

    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            return;
        }

        Cache<Object, AuthorizationInfo> cache = getAvailableAuthorizationCache();
        if (cache != null) {
            Object key = getAuthorizationCacheKey(principals);
            cache.remove(key);
        }
    }

    protected abstract AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals);

    protected Collection<Permission> getPermissions(AuthorizationInfo info) {
        Set<Permission> permissions = new HashSet<Permission>();

        if (info != null) {
            Collection<Permission> perms = info.getObjectPermissions();
            if (!CollectionUtils.isEmpty(perms)) {
                permissions.addAll(perms);
            }
            perms = resolvePermissions(info.getStringPermissions());
            if (!CollectionUtils.isEmpty(perms)) {
                permissions.addAll(perms);
            }

            perms = resolveRolePermissions(info.getRoles());
            if (!CollectionUtils.isEmpty(perms)) {
                permissions.addAll(perms);
            }
        }

        if (permissions.isEmpty()) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableSet(permissions);
        }
    }

    private Collection<Permission> resolvePermissions(Collection<String> stringPerms) {
        Collection<Permission> perms = Collections.emptySet();
        PermissionResolver resolver = getPermissionResolver();
        if (resolver != null && !CollectionUtils.isEmpty(stringPerms)) {
            perms = new LinkedHashSet<Permission>(stringPerms.size());
            for (String strPermission : stringPerms) {
                Permission permission = resolver.resolvePermission(strPermission);
                perms.add(permission);
            }
        }
        return perms;
    }

    private Collection<Permission> resolveRolePermissions(Collection<String> roleNames) {
        Collection<Permission> perms = Collections.emptySet();
        RolePermissionResolver resolver = getRolePermissionResolver();
        if (resolver != null && !CollectionUtils.isEmpty(roleNames)) {
            perms = new LinkedHashSet<Permission>(roleNames.size());
            for (String roleName : roleNames) {
                Collection<Permission> resolved = resolver.resolvePermissionsInRole(roleName);
                if (!CollectionUtils.isEmpty(resolved)) {
                    perms.addAll(resolved);
                }
            }
        }
        return perms;
    }

    public boolean isPermitted(PrincipalCollection principals, String permission) {
        Permission p = getPermissionResolver().resolvePermission(permission);
        return isPermitted(principals, p);
    }

    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        AuthorizationInfo info = getAuthorizationInfo(principals);
        return isPermitted(permission, info);
    }

    //visibility changed from private to protected per SHIRO-332
    protected boolean isPermitted(Permission permission, AuthorizationInfo info) {
        Collection<Permission> perms = getPermissions(info);
        if (perms != null && !perms.isEmpty()) {
            for (Permission perm : perms) {
                if (perm.implies(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean[] isPermitted(PrincipalCollection subjectIdentifier, String... permissions) {
        List<Permission> perms = new ArrayList<Permission>(permissions.length);
        for (String permString : permissions) {
            perms.add(getPermissionResolver().resolvePermission(permString));
        }
        return isPermitted(subjectIdentifier, perms);
    }

    public boolean[] isPermitted(PrincipalCollection principals, List<Permission> permissions) {
        AuthorizationInfo info = getAuthorizationInfo(principals);
        return isPermitted(permissions, info);
    }

    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        boolean[] result;
        if (permissions != null && !permissions.isEmpty()) {
            int size = permissions.size();
            result = new boolean[size];
            int i = 0;
            for (Permission p : permissions) {
                result[i++] = isPermitted(p, info);
            }
        } else {
            result = new boolean[0];
        }
        return result;
    }

    public boolean isPermittedAll(PrincipalCollection subjectIdentifier, String... permissions) {
        if (permissions != null && permissions.length > 0) {
            Collection<Permission> perms = new ArrayList<Permission>(permissions.length);
            for (String permString : permissions) {
                perms.add(getPermissionResolver().resolvePermission(permString));
            }
            return isPermittedAll(subjectIdentifier, perms);
        }
        return false;
    }

    public boolean isPermittedAll(PrincipalCollection principal, Collection<Permission> permissions) {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        return info != null && isPermittedAll(permissions, info);
    }

    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission p : permissions) {
                if (!isPermitted(p, info)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkPermission(PrincipalCollection subjectIdentifier, String permission) throws AuthorizationException {
        Permission p = getPermissionResolver().resolvePermission(permission);
        checkPermission(subjectIdentifier, p);
    }

    public void checkPermission(PrincipalCollection principal, Permission permission) throws AuthorizationException {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        checkPermission(permission, info);
    }

    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        if (!isPermitted(permission, info)) {
            String msg = "User is not permitted [" + permission + "]";
            throw new UnauthorizedException(msg);
        }
    }

    public void checkPermissions(PrincipalCollection subjectIdentifier, String... permissions) throws AuthorizationException {
        if (permissions != null) {
            for (String permString : permissions) {
                checkPermission(subjectIdentifier, permString);
            }
        }
    }

    public void checkPermissions(PrincipalCollection principal, Collection<Permission> permissions) throws AuthorizationException {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        checkPermissions(permissions, info);
    }

    protected void checkPermissions(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission p : permissions) {
                checkPermission(p, info);
            }
        }
    }

    public boolean hasRole(PrincipalCollection principal, String roleIdentifier) {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        return hasRole(roleIdentifier, info);
    }

    protected boolean hasRole(String roleIdentifier, AuthorizationInfo info) {
        return info != null && info.getRoles() != null && info.getRoles().contains(roleIdentifier);
    }

    public boolean[] hasRoles(PrincipalCollection principal, List<String> roleIdentifiers) {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        boolean[] result = new boolean[roleIdentifiers != null ? roleIdentifiers.size() : 0];
        if (info != null) {
            result = hasRoles(roleIdentifiers, info);
        }
        return result;
    }

    protected boolean[] hasRoles(List<String> roleIdentifiers, AuthorizationInfo info) {
        boolean[] result;
        if (roleIdentifiers != null && !roleIdentifiers.isEmpty()) {
            int size = roleIdentifiers.size();
            result = new boolean[size];
            int i = 0;
            for (String roleName : roleIdentifiers) {
                result[i++] = hasRole(roleName, info);
            }
        } else {
            result = new boolean[0];
        }
        return result;
    }

    public boolean hasAllRoles(PrincipalCollection principal, Collection<String> roleIdentifiers) {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        return info != null && hasAllRoles(roleIdentifiers, info);
    }

    private boolean hasAllRoles(Collection<String> roleIdentifiers, AuthorizationInfo info) {
        if (roleIdentifiers != null && !roleIdentifiers.isEmpty()) {
            for (String roleName : roleIdentifiers) {
                if (!hasRole(roleName, info)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkRole(PrincipalCollection principal, String role) throws AuthorizationException {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        checkRole(role, info);
    }

    protected void checkRole(String role, AuthorizationInfo info) {
        if (!hasRole(role, info)) {
            String msg = "User does not have role [" + role + "]";
            throw new UnauthorizedException(msg);
        }
    }

    public void checkRoles(PrincipalCollection principal, Collection<String> roles) throws AuthorizationException {
        AuthorizationInfo info = getAuthorizationInfo(principal);
        checkRoles(roles, info);
    }

    public void checkRoles(PrincipalCollection principal, String... roles) throws AuthorizationException {
        checkRoles(principal, Arrays.asList(roles));
    }

    protected void checkRoles(Collection<String> roles, AuthorizationInfo info) {
        if (roles != null && !roles.isEmpty()) {
            for (String roleName : roles) {
                checkRole(roleName, info);
            }
        }
    }

    @Override
    protected void doClearCache(PrincipalCollection principals) {
        super.doClearCache(principals);
        clearCachedAuthorizationInfo(principals);
    }

}
