package site.yuyanjia.template.website.service;

import com.alibaba.fastjson.JSONObject;

/**
 * UserService
 *
 * @author seer
 * @date 2018/6/22 11:14
 */
public interface UserService {
    /**
     * 用户密码修改
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    Object userPasswordUpdate(String oldPassword, String newPassword);

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    Object userLogin(String username, String password);
}
