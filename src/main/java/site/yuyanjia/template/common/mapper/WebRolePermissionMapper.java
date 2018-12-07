package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.Param;
import site.yuyanjia.template.common.model.WebRolePermissionDO;

import java.util.List;


/**
 * 角色权限mapper
 *
 * @author seer
 * @date 2018/6/15
 */
public interface WebRolePermissionMapper {

    /**
     * 根据角色id查询
     *
     * @param roleId
     * @return
     */
    List<WebRolePermissionDO> selectByRoleId(Long roleId);

    /**
     * 根据角色Id删除
     *
     * @param roleId
     * @return
     */
    int deleteByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据权限Id删除
     *
     * @param permissionId
     * @return
     */
    int deleteByPermissionId(@Param("permissionId") Long permissionId);

    /**
     * 根据权限id查询
     *
     * @param permissionId
     * @return
     */
    List<WebRolePermissionDO> selectByPermissionId(@Param("permissionId") Long permissionId);
}