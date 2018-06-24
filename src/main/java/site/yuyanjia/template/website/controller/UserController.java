package site.yuyanjia.template.website.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.yuyanjia.template.common.model.WebUserDO;
import site.yuyanjia.template.website.service.UserService;

/**
 * UserController
 *
 * @author seer
 * @date 2018/6/22 10:08
 */
@RestController
@RequestMapping("/website/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user-test")
    public String userTest() {
        Subject subject = SecurityUtils.getSubject();
        WebUserDO webUserDO = (WebUserDO) subject.getPreviousPrincipals();
        log.warn("===== "+webUserDO.toString());

        return "success";
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/user-login", method = RequestMethod.POST)
    public String userLogin(String username, String password) {
        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            return "{\"response_code\":\"9999\",\"response_msg\":\"请求参数不完整\"}";
        }

        String responseStr = userService.userLogin(username, password);
        log.info("用户登录，返回数据 {}", responseStr);
        return responseStr;
    }

    /**
     * 用户登出
     *
     * @return
     */
    @RequestMapping(value = "/user-logout", method = RequestMethod.POST)
    public String userLogout() {
        return "{\"response_code\":\"0000\",\"response_msg\":\"SUCCESS\"}";
    }

    /**
     * 用户密码修改
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/user-password-update", method = RequestMethod.POST)
    public String userPasswordUpdate(@RequestParam("old_password") String oldPassword, @RequestParam("new_password") String newPassword) {
        log.error(oldPassword + " " + newPassword);
        if (ObjectUtils.isEmpty(oldPassword) || ObjectUtils.isEmpty(newPassword)) {
            return "{\"response_code\":\"9999\",\"response_msg\":\"请求参数不完整\"}";
        }
        String responseStr = userService.userPasswordUpdate(oldPassword, newPassword);
        log.error("用户密码修改，返回数据 {}", responseStr);
        return responseStr;
    }
}
