package online.yuyanjia.template.mobile.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * shiro配置
 *
 * @author seer
 * @date 2018/2/1 15:41
 */
@Configuration
@AutoConfigureAfter(RedisConfig.class)
public class ShiroConfig {
    private static Logger LOGGER = LogManager.getLogger(ShiroConfig.class);

    /**
     * shiro过滤器
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 配置拦截路径，顺寻判断
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
}
