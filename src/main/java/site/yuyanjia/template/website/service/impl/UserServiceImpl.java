package site.yuyanjia.template.website.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import site.yuyanjia.template.common.mapper.WebUserMapper;
import site.yuyanjia.template.common.model.WebUserDO;
import site.yuyanjia.template.website.service.UserService;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * UserServiceImpl
 *
 * @author seer
 * @date 2018/6/22 11:15
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private WebUserMapper webUserMapper;

    /**
     * 用户密码修改
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @Override
    public String userPasswordUpdate(String oldPassword, String newPassword) {
        Subject subject = SecurityUtils.getSubject();
        WebUserDO webUserDO = (WebUserDO) subject.getPreviousPrincipals();
        if (ObjectUtils.isEmpty(webUserDO)) {
            log.error("用户密码修改，用户信息为空");
            return "{\"response_code\":\"9999\",\"response_msg\":\"信息不存在\"}";
        }

        UsernamePasswordToken token = new UsernamePasswordToken(webUserDO.getUsername(), oldPassword);
        try {
            subject.login(token);
        } catch (AuthenticationException ex) {
            log.error("用户密码修改，旧密码校验失败，用户信息 {}", webUserDO);
            return "{\"response_code\":\"9999\",\"response_msg\":\"旧密码校验失败\"}";
        }

        String salt = UUID.randomUUID().toString().trim().replaceAll("-", "").toUpperCase();
        String password = "";

        webUserDO.setSalt(salt);
        webUserDO.setPassword(password);
        webUserDO.setGmtModified(Date.from(Instant.now()));
        int count = webUserMapper.updatePasswordByPrimaryKey(webUserDO);
        if (count != 1) {
            log.error("用户密码修改，更新用户数据失败，修改数据 {}", webUserDO);
            return "{\"response_code\":\"9999\",\"response_msg\":\"系统错误\"}";
        }
        return "{\"response_code\":\"0000\",\"response_msg\":\"SUCCESS\"}";
    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public String userLogin(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "{\"response_code\":\"0000\",\"response_msg\":\"SUCCESS\"}";
        }

        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
        } catch (ShiroException ex) {
            log.trace("用户登录失败", ex.getMessage());
            return "{\"response_code\":\"9999\",\"response_msg\":\"登陆失败\"}";
        }
        return "{\"response_code\":\"0000\",\"response_msg\":\"SUCCESS\"}";
    }
}
