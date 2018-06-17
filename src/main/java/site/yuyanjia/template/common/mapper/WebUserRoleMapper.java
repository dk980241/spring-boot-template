package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import site.yuyanjia.template.common.config.MybatisRedisCache;
import site.yuyanjia.template.common.model.WebUserRoleDO;
import site.yuyanjia.template.common.util.BaseMapper;

import java.util.List;

/**
 * 用户角色mapper
 *
 * @author seer
 * @date 2018/6/15 16:49
 */
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface WebUserRoleMapper extends BaseMapper<WebUserRoleDO> {

    /**
     * 根据用户名查询
     *
     * @param userId
     * @return
     */
    List<WebUserRoleDO> selectByUserId(Long userId);
}