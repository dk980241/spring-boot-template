package site.yuyanjia.template.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import site.yuyanjia.template.common.model.WebUserDO;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户信息获取service
 *
 * @author seer
 * @date 2018/12/3 14:46
 */
@Service("webUserDetailsService")
public class WebUserDetailsServiceImpl implements UserDetailsService {

    /**
     * 根据用户名登录
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO seer 2018/12/6 11:41   数据库中获取用户密码，角色等信息
        WebUserDO webUserDO = new WebUserDO();
        if (ObjectUtils.isEmpty(webUserDO)) {
            throw new UsernameNotFoundException("用户登录，用户信息查询失败");
        }
        Set<String> roleSet = new HashSet<>();

        /**
         封装为框架使用的 userDetail {@link UserDetails}
         */
        WebUserDetail webUserDetail = new WebUserDetail();
        webUserDetail.setSalt(webUserDO.getSalt());
        webUserDetail.setPassword(webUserDO.getPassword());
        webUserDetail.setUsername(webUserDO.getUsername());
        webUserDetail.setRoleSet(roleSet);

        webUserDetail.setPassword("seer");
        webUserDetail.setUsername("yuyanjia");
        roleSet.add("admin");
        webUserDetail.setRoleSet(roleSet);
        return webUserDetail;
    }
}
