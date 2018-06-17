package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import site.yuyanjia.template.common.config.MybatisRedisCache;
import site.yuyanjia.template.common.model.WebRolePermissionDO;
import site.yuyanjia.template.common.util.BaseMapper;

import java.util.List;

/**
 * 角色权限mapper
 *
 * @author seer
 * @date 2018/6/15
 */
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface WebRolePermissionMapper extends BaseMapper<WebRolePermissionDO> {

    /**
     * 根据角色id查询
     *
     * @param roleId
     * @return
     */
    List<WebRolePermissionDO> selectByRoleId(Long roleId);
}