package site.yuyanjia.template.common.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Set;

/**
 * WebAuthorizationFilter
 *
 * @author seer
 * @date 2018/6/17 21:43
 */
public class WebAuthorizationFilter extends AccessControlFilter {


    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        if (isLoginRequest(request, response)) {
            response.getWriter().write("{\"response_code\":\"9999\",\"response_msg\":\"登陆过期\"}");
            return true;
        }
        
        Subject subject = getSubject(request, response);
        if (subject.getPrincipal() != null) {
            response.getWriter().write("{\"response_code\":\"0000\",\"response_msg\":\"登陆成功\"}");
            return true;
        }

        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return true;
        }

        Set<String> roles = CollectionUtils.asSet(rolesArray);
        return subject.hasAllRoles(roles);

    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        saveRequestAndRedirectToLogin(request, response);
        return false;
    }
}
