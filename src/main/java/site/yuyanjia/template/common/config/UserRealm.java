package site.yuyanjia.template.common.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * 用户Realm
 *
 * @author seer
 * @date 2018/2/1 16:59
 */
public class UserRealm extends AuthorizingRealm {
    /**
     * 获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        String userName = (String) principalCollection.getPrimaryPrincipal();
        // String[] roles = TmpShiro.getRoles(userName);
        //
        // authenticationInfo.addRoles(Arrays.asList(roles));
        // List<String> permissionList = new ArrayList<>();
        // for (String role : roles) {
        //     String[] permissions = TmpShiro.getPermissions(role);
        //     permissionList.addAll(Arrays.asList(permissions));
        // }
        // authenticationInfo.addStringPermissions(permissionList);
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
        // UserInfoDO userInfoDO = userInfoService.findUserInfoByMobile(mobile);
        // if (null == userInfoDO) {
        //     LOGGER.debug(">>>> 用户登录验证，用户信息查询失败，查询手机号{} <<<<", mobile);
        //     throw new UnknownAccountException();
        // }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                null,
                null,
                getName()
        );
        ByteSource salt = ByteSource.Util.bytes("");
        authenticationInfo.setCredentialsSalt(salt);
        return authenticationInfo;
    }
}
