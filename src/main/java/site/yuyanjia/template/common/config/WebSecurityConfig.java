package site.yuyanjia.template.common.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import site.yuyanjia.template.common.security.WebUserDetail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;

/**
 * 安全配置
 *
 * @author seer
 * @date 2018/12/5 10:30
 */
// @Configuration
// @EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    /**
     * 成功
     */
    private static final String SUCCESS = "{\"result_code\": \"00000\", \"result_msg\": \"处理成功\"}";

    /**
     * 失败
     */
    private static final String FAILED = "{\"result_code\": \"99999\", \"result_msg\": \"处理失败\"}";

    /**
     * 权限限制
     */
    private static final String ROLE_LIMIT = "{\"result_code\": \"50002\", \"result_msg\": \"权限不足\"}";

    /**
     * 登录过期
     */
    private static final String LOGIN_EXPIRE = "{\"result_code\": \"50001\", \"result_msg\": \"用户登陆过期\"}";

    @Autowired
    private UserDetailsService webUserDetailsService;

    /**
     * http安全配置
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 跨域资源共享限制.无效
                .cors().disable()
                // 跨域伪造请求限制.无效
                .csrf().disable()
                /*
                异常处理
                默认 权限不足  返回403，可以在这里自定义返回内容
                 */
                .exceptionHandling().accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
            log.warn("权限不足");
            httpServletResponse.getWriter().write(ROLE_LIMIT);
        })
                .authenticationEntryPoint((httpServletRequest, httpServletResponse, e) -> {
                    log.warn("登录过期");
                    httpServletResponse.getWriter().write(LOGIN_EXPIRE);
                }).and()
                // 开启授权认证
                .authorizeRequests()
                // 这里可以用来设置权限验证处理
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {

                        /*
                           设置权限原数据
                           这里为，请求URL归属哪个角色，最终要用对角色做比较
                         */
                        object.setSecurityMetadataSource(new FilterInvocationSecurityMetadataSource() {
                            @Override
                            public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
                                String url = ((FilterInvocation) o).getRequestUrl();

                                // TODO seer 2018/12/6 14:10 数据库中查询url对应的角色信息
                                String[] roleIds = {"adm1in"};
                                return SecurityConfig.createList(roleIds);
                            }

                            @Override
                            public Collection<ConfigAttribute> getAllConfigAttributes() {
                                return null;
                            }

                            @Override
                            public boolean supports(Class<?> aClass) {
                                return true;
                            }
                        });

                        /*
                        设置权限决策者
                        是否有访问权限在这里确定的
                         */
                        object.setAccessDecisionManager(new AccessDecisionManager() {
                            /**
                             * 判定
                             *
                             * @param authentication 登录用户的信息
                             * @param o
                             * @param collection   请求地址拥有的角色集合
                             * @throws AccessDeniedException
                             * @throws InsufficientAuthenticationException
                             */
                            @Override
                            public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
                                Iterator<ConfigAttribute> iterator = collection.iterator();
                                while (iterator.hasNext()) {
                                    ConfigAttribute attribute = iterator.next();
                                    Collection<? extends GrantedAuthority> authenticationAuthorities = authentication.getAuthorities();
                                    for (GrantedAuthority authenticationAuthority : authenticationAuthorities) {
                                        if (authenticationAuthority.getAuthority().equalsIgnoreCase(attribute.getAttribute())) {
                                            return;
                                        }
                                    }
                                }
                                throw new AccessDeniedException("权限不足");
                            }

                            @Override
                            public boolean supports(ConfigAttribute configAttribute) {
                                return true;
                            }

                            @Override
                            public boolean supports(Class<?> aClass) {
                                return true;
                            }
                        });
                        return object;
                    }
                })
                // 可以直接访问的url(不授权，不权限)
                .antMatchers("/**", "/home").permitAll()
                // 剩下的授权之后可以直接访问
                .anyRequest().authenticated()
                .and()
                /*
                设置form表单登录
                必须先设置，后边会替换form表单登录的filter
                 */
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                // reqeust，session缓存，自行实现 org.springframework.security.web.savedrequest.RequestCache
                .requestCache().requestCache(new HttpSessionRequestCache());

        /*
         * 实现json格式登录
         */
        AbstractAuthenticationProcessingFilter filter = new UsernamePasswordAuthenticationFilter() {
            /**
             *  获取json数据格式的用户名和密码
             * @param request
             * @param response
             * @return
             * @throws AuthenticationException
             */
            @Override
            public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
                if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType())
                        || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(request.getContentType())) {
                    // 解析request内容
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    dbf.setExpandEntityReferences(false);
                    StringBuffer sb = new StringBuffer();
                    try (InputStream inputStream = request.getInputStream(); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            sb.append(str);
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException("获取请求内容异常", ex);
                    }

                    JSONObject jsonObject = JSON.parseObject(sb.toString());
                    String username = jsonObject.getString("username");
                    String password = jsonObject.getString("password");
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
                    return this.getAuthenticationManager().authenticate(authenticationToken);
                } else {
                    return super.attemptAuthentication(request, response);
                }
            }
        };

        // 登录成功后 返回 {"result_code": "00000", "result_msg": "处理成功"}
        filter.setAuthenticationSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> httpServletResponse.getWriter().write(SUCCESS));
        //登录失败后 返回 {"result_code": "99999", "result_msg": "99999"}
        filter.setAuthenticationFailureHandler((httpServletRequest, httpServletResponse, e) -> {
            log.warn("用户登录失败", e);
            httpServletResponse.getWriter().write(FAILED);
        });
        // 作用在登录的URL
        filter.setFilterProcessesUrl("/login");
        // 设置验证manager
        filter.setAuthenticationManager(super.authenticationManagerBean());

        // 替换默认的filter
        http.addFilterAt(filter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置登录验证
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 用户信息获取service
        auth.userDetailsService(webUserDetailsService);
        // 自定义的密码验证
        auth.authenticationProvider(new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String loginUsername = authentication.getName();
                String loginPassword = (String) authentication.getCredentials();
                log.trace("用户登录，登录信息，用户名 {}，密码 {}", loginUsername, loginPassword);

                WebUserDetail webUserDetail = (WebUserDetail) webUserDetailsService.loadUserByUsername(loginUsername);
                // TODO seer 2018/12/6 14:04  这个地方就可以实现自己的密码校验规则了，加盐处理等，随意了
                if (!loginPassword.equals(webUserDetail.getPassword())) {
                    throw new DisabledException("用户登录，密码错误");
                }

                return new UsernamePasswordAuthenticationToken(webUserDetail, webUserDetail.getPassword(), webUserDetail.getAuthorities());
            }

            /**
             * 支持使用此方法验证
             *
             * @param aClass
             * @return 没有特殊处理，返回true，否则不会用这个配置进行验证
             */
            @Override
            public boolean supports(Class<?> aClass) {
                return true;
            }
        });
    }
}
