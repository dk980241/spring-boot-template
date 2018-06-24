package site.yuyanjia.template.website.service;

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
    String userPasswordUpdate(String oldPassword, String newPassword);

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return
     */
    String userLogin(String username, String password);
}
