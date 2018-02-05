package online.yuyanjia.template.mobile.configuration;

import online.yuyanjia.template.mobile.model.DO.UserInfoDO;
import online.yuyanjia.template.mobile.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户Realm
 *
 * @author seer
 * @date 2018/2/1 16:59
 */
public class UserRealm extends AuthorizingRealm {
    private static Logger LOGGER = LogManager.getLogger(UserRealm.class);

    @Autowired
    UserInfoService userInfoService;

    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        // TODO seer 2018/2/1 16:54 获取用户权限
        authenticationInfo.addRole(null);
        authenticationInfo.addStringPermission(null);
        return authenticationInfo;
    }

    /**
     * 获取验证信息
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String mobile = (String) authenticationToken.getPrincipal();
        LOGGER.debug("---- 用户登录验证 - {} ----", mobile);
        UserInfoDO userInfoDO = userInfoService.findUserInfoByMobile(mobile);
        if (null == userInfoDO) {
            LOGGER.debug(">>>> 用户登录验证，用户信息查询失败，查询手机号{} <<<<", mobile);
            throw new UnknownAccountException();
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfoDO.getMobile(),
                userInfoDO.getPassword(),
                getName()
        );
        ByteSource salt = ByteSource.Util.bytes(userInfoDO.getMobile() + userInfoDO.getSalt());
        authenticationInfo.setCredentialsSalt(salt);
        return authenticationInfo;
    }
}
