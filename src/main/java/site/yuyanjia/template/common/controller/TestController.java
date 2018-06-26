package site.yuyanjia.template.common.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * @author seer
 * @date 2018/2/2 11:01
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private static Logger LOGGER = LogManager.getLogger(TestController.class);

    @RequestMapping("/log")
    public void useShiroAuthen() {
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

    public static void main(String[] args) {
        String str = "2018-06-25 15:03:13";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(str, dateTimeFormatter);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        System.out.println(date);
    }
}
