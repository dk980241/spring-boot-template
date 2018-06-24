package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.Param;
import site.yuyanjia.template.common.model.WebPermissionDO;

/**
 * 权限mapper
 *
 * @author seer
 * @date 2018/6/15 16:48
 */
public interface WebPermissionMapper{

    /**
     * 根据权限名查询
     *
     * @param permissionName
     * @return
     */
    WebPermissionDO selectByPermissonName(@Param("permissionName") String permissionName);

    /**
     * 修改权限名和值
     *
     * @param webPermissionDO
     * @return
     */
    int updatePermissionNameAndPermissionValueByPrimaryKey(WebPermissionDO webPermissionDO);

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    WebPermissionDO selectByPrimaryKey(Long id);
}