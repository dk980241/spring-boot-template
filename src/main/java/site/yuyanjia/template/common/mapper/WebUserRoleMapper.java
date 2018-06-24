package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.Param;
import site.yuyanjia.template.common.model.WebUserRoleDO;

import java.util.List;

/**
 * 用户角色mapper
 *
 * @author seer
 * @date 2018/6/15 16:49
 */
public interface WebUserRoleMapper  {

    /**
     * 根据用户Id查询
     *
     * @param userId
     * @return
     */
    List<WebUserRoleDO> selectByUserId(Long userId);

    /**
     * 根据用户Id删除
     *
     * @param userId
     * @return
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID删除
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(@Param("roleId") Long roleId);
}