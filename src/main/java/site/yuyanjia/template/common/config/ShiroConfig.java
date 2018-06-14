package site.yuyanjia.template.common.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
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
    private String loginUrl;

    /**
     * 登出路径
     */
    private String logoutUrl;

    /**
     * 登录成功跳转路径
     */
    private String successUrl;

    /**
     * 未授权路径
     */
    private String unauthorizedUrl;

    /**
     * 匿名路径
     */
    private String[] anonUrls;

    /**
     * 权限路径
     */
    private String[] authcUrls;

    /**
     * 散列算法
     */
    private String hashAlgorithm;

    /**
     * 散列迭代次数
     */
    private Integer hashIterations;

    /**
     * 权限枚举
     */
    private enum AuthorizationEunm {
        /**
         * 匿名
         */
        anon,

        /**
         * 登录
         */
        authc,

        /**
         * 登出
         */
        logout
    }


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
        // anon 不校验权限
        filterChainDefinitionMap.put("/static/**", "anon");
        // logout 退出
        // filterChainDefinitionMap.put("/logout", "logout"); // TODO seer 2018/2/2 10:37 web退出
        // 一般将/**放在最为下边
        filterChainDefinitionMap.put("/**", "anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);


        // 登录页面
        // shiroFilterFactoryBean.setLoginUrl(null); // TODO seer 2018/2/2 10:39 web登录
        // 生成页面
        // shiroFilterFactoryBean.setSuccessUrl(null); // TODO seer 2018/2/2 10:40 web登录成功跳转
        // 未授权错误页面
        // shiroFilterFactoryBean.setUnauthorizedUrl(null); // TODO seer 2018/2/2 10:40 web未授权页面
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
        // 散列算法，MD5
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        // 散列次数，2次
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    /**
     * 用户Realm
     *
     * @return
     */
    @Bean
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return new UserRealm();
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

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String[] getAnonUrls() {
        return anonUrls;
    }

    public void setAnonUrls(String[] anonUrls) {
        this.anonUrls = anonUrls;
    }

    public String[] getAuthcUrls() {
        return authcUrls;
    }

    public void setAuthcUrls(String[] authcUrls) {
        this.authcUrls = authcUrls;
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
