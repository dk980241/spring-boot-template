package online.yuyanjia.template.mobile.service;

import online.yuyanjia.template.mobile.model.DO.UserInfoDO;

/**
 * 用户操作接口
 *
 * @author seer
 * @date 2018/1/29 16:15
 */
public interface UserInfoService {

    /**
     * 注册
     */
    void register();

    /**
     * 登录
     */
    void login();

    /**
     * 登出
     */
    void logout();

    UserInfoDO test(UserInfoDO userInfoDO);
}
