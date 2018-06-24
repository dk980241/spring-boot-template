package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.Param;
import site.yuyanjia.template.common.model.WebRoleDO;

/**
 * 角色mapper
 *
 * @author seer
 * @date 2018/6/15 16:48
 */
public interface WebRoleMapper {

    /**
     * 根据角色名查询
     *
     * @param roleName
     * @return
     */
    WebRoleDO selectByRoleName(@Param("roleName") String roleName);

    /**
     * 更新角色名，描述
     *
     * @param webRoleDO
     * @return
     */
    int updateRoleNameAndRoleDescriptionByPrimaryKey(WebRoleDO webRoleDO);
}