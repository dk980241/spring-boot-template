package site.yuyanjia.template.common.config;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import site.yuyanjia.template.common.realm.WebUserRealm;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * shiro配置
 *
 * @author seer
 * @date 2018/2/1 15:41
 */
@Configuration
@AutoConfigureAfter(RedisConfig.class)
@ConfigurationProperties(prefix = ShiroConfig.PREFIX)
public class ShiroConfig {
    public static final String PREFIX = "shiro";

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
        filterChainDefinitionMap.put(loginUrl, DefaultFilter.authc.toString());
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
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
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
        return hashedCredentialsMatcher;
    }

    /**
     * 用户Realm
     *
     * @return
     */
    @Bean
    public WebUserRealm userRealm() {
        WebUserRealm userRealm = new WebUserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return new WebUserRealm();
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
            saveRequestAndRedirectToLogin(request, response);
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
                response.getWriter().write("{\"response_code\":\"0000\",\"response_msg\":\"权限校验通过\"}");
                return true;
            }

            if (perms.length == 1 && !subject.isPermitted(perms[0])) {
                response.getWriter().write("{\"response_code\":\"9999\",\"response_msg\":\"权限校验未通过\"}");
                return false;
            } else if (!subject.isPermittedAll(perms)) {
                response.getWriter().write("{\"response_code\":\"9999\",\"response_msg\":\"权限校验未通过\"}");
                return false;
            }

            response.getWriter().write("{\"response_code\":\"0000\",\"response_msg\":\"权限校验通过\"}");
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
