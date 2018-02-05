package online.yuyanjia.template.mobile.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author seer
 * @date 2018/2/2 11:01
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private static Logger LOGGER = LogManager.getLogger(TestController.class);

    @RequestMapping("/log")
    public void use_shiro_authen() {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("1231231", "123456");
            try {
                subject.login(usernamePasswordToken);
            } catch (UnknownAccountException e) {
                LOGGER.error("帐号不存在");
                return;
            } catch (IncorrectCredentialsException e) {
                LOGGER.error("密码错误");
                return;
            } catch (LockedAccountException e) {
                LOGGER.error("帐号锁定");
                return;
            }
        } else {
            LOGGER.error("已经通过身份验证了");
        }
    }
}
