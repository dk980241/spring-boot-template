package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.Param;
import site.yuyanjia.template.common.model.WebUserDO;

import java.util.List;

/**
 * 用户mapper
 *
 * @author seer
 * @date 2018/6/15 16:49
 */
public interface WebUserMapper {

    /**
     * 根据用户名查询
     *
     * @param username
     * @return
     */
    WebUserDO selectByUsername(String username);

    /**
     * 根据手机号查询
     *
     * @param mobile
     * @return
     */
    WebUserDO selectByMobile(String mobile);

    /**
     * 根据条件模糊查询
     *
     * @param username
     * @param mobile
     * @param realName
     * @param locked
     * @return
     */
    List<WebUserDO> selectWithLike(@Param("username") String username, @Param("mobile") String mobile, @Param("realName") String realName, @Param("locked") Boolean locked);

    /**
     * 更新手机号和真实姓名
     *
     * @param webUserDO
     * @return
     */
    int updateMobileAndRealNameByPrimaryKey(WebUserDO webUserDO);

    /**
     * 更新锁定状态
     *
     * @param webUserDO
     * @return
     */
    int updateLockedByPrimaryKey(WebUserDO webUserDO);

    /**
     * 更新密码
     *
     * @param webUserDO
     * @return
     */
    int updatePasswordByPrimaryKey(WebUserDO webUserDO);
}