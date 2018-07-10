package site.yuyanjia.template.website.realm;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import site.yuyanjia.template.common.config.SerializableSimpleByteSource;
import site.yuyanjia.template.common.mapper.WebPermissionMapper;
import site.yuyanjia.template.common.mapper.WebRolePermissionMapper;
import site.yuyanjia.template.common.mapper.WebUserMapper;
import site.yuyanjia.template.common.mapper.WebUserRoleMapper;
import site.yuyanjia.template.common.model.WebPermissionDO;
import site.yuyanjia.template.common.model.WebRolePermissionDO;
import site.yuyanjia.template.common.model.WebUserDO;
import site.yuyanjia.template.common.model.WebUserRoleDO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 用户Realm
 *
 * @author seer
 * @date 2018/2/1 16:59
 */
public class WebUserRealm extends AuthorizingRealm {

    @Autowired
    private WebUserMapper webUserMapper;

    @Autowired
    private WebUserRoleMapper webUserRoleMapper;

    @Autowired
    private WebRolePermissionMapper webRolePermissionMapper;

    @Autowired
    private WebPermissionMapper webPermissionMapper;

    /**
     * 获取授权信息
     * <p>
     * 权限的值是前端ajax请求的路径，角色的存在是为了方便给用户批量赋值权限的。
     * 项目的最终实现是针对用户和权限的关系，不对角色作校验
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /**
         * 这个地方涉及到类加载的问题，看起来是同一个对象，但是不是同一个类加载器，强制类型转换会失败，所以只好使用拷贝了
         */
        Object obj = principalCollection.getPrimaryPrincipal();
        WebUserDO webUserDO = new WebUserDO();
        BeanUtils.copyProperties(obj, webUserDO);

        if (ObjectUtils.isEmpty(obj) || ObjectUtils.isEmpty(webUserDO.getId())) {
            throw new AccountException("用户信息查询为空");
        }

        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        List<WebUserRoleDO> webUserRoleDOList = webUserRoleMapper.selectByUserId(webUserDO.getId());
        if (CollectionUtils.isEmpty(webUserRoleDOList)) {
            return authenticationInfo;
        }

        List<WebRolePermissionDO> webRolePermissionDOList = new ArrayList<>();
        webUserRoleDOList.forEach(
                webUserRoleDO -> webRolePermissionDOList.addAll(webRolePermissionMapper.selectByRoleId(webUserRoleDO.getRoleId()))
        );
        if (CollectionUtils.isEmpty(webRolePermissionDOList)) {
            return authenticationInfo;
        }

        Set<String> permissonSet = webRolePermissionDOList.stream()
                .map(webRolePermissionDO ->
                {
                    WebPermissionDO webPermissionDO = webPermissionMapper.selectByPrimaryKey(webRolePermissionDO.getPermissionId());
                    return webPermissionDO.getPermissionValue();
                })
                .collect(Collectors.toSet());
        authenticationInfo.addStringPermissions(permissonSet);
        return authenticationInfo;
    }

    /**
     * 获取验证信息
     * <p>
     * 将用户实体作为principal方便后续直接使用
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        WebUserDO webUserDO = webUserMapper.selectByUsername(username);
        if (ObjectUtils.isEmpty(webUserDO)) {
            throw new UnknownAccountException("用户 " + username + " 信息查询失败");
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                webUserDO,
                webUserDO.getPassword(),
                getName()
        );
        authenticationInfo.setCredentialsSalt(new SerializableSimpleByteSource(webUserDO.getSalt()));
        return authenticationInfo;
    }

    /**
     * 删除缓存
     *
     * @param principals
     */
    @Override
    protected void doClearCache(PrincipalCollection principals) {
        super.doClearCache(principals);
    }
}
