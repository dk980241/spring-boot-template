package online.yuyanjia.template.mobile.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * shiro测试
 *
 * @author seer
 * @date 2018/3/23 21:55
 */
@RestController
@RequestMapping("shirotest")
public class ShiroTestController {
    private static Logger LOGGER = LogManager.getLogger(ShiroTestController.class);

    @RequestMapping("/login")
    public Object login(String name, String pwd) {
        Subject subject = SecurityUtils.getSubject();

        if (subject.isAuthenticated()) {
            LOGGER.info("=== {} 已经通过shiro验证", name);
            return true;
        }

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(name, pwd);
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            LOGGER.error("帐号不存在");
        } catch (IncorrectCredentialsException e) {
            LOGGER.error("密码错误");
        }

        System.out.println("SESSION ID = " + SecurityUtils.getSubject().getSession().getId());
        System.out.println("用户名：" + SecurityUtils.getSubject().getPrincipal());
        System.out.println("HOST：" + SecurityUtils.getSubject().getSession().getHost());
        System.out.println("TIMEOUT ：" + SecurityUtils.getSubject().getSession().getTimeout());
        System.out.println("START：" + SecurityUtils.getSubject().getSession().getStartTimestamp());
        System.out.println("LAST：" + SecurityUtils.getSubject().getSession().getLastAccessTime());
        return true;
    }

}
