package site.yuyanjia.template.common.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * WebAuthorizationFilter
 *
 * @author seer
 * @date 2018/6/14 13:50
 */
@Slf4j
public class WebAuthorizationFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        //
        // // TODO seer 2018/6/14 14:15 角色表获取所有的角色
        //
        // // uuyana
        // //
        // //
        // //
        // List<String> roles = new ArrayList<>();
        // //
        // //
        // //
        // Set<String> roles = CollectionUtils.asSet(rolesArray);
        //
        // subject.hasAllRoles(roles);

        String[] perms = (String[]) mappedValue;

        // TODO seer 2018/6/14 14:14 这个地方的权限
        perms = new String[0];
        boolean isPermitted = true;
        if (perms != null && perms.length > 0) {
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else {
                if (subject.isPermittedAll(perms)) {
                    isPermitted = true;
                }
            }
        }




        return false;
    }
}

