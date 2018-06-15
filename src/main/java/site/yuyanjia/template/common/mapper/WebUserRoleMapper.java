package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import site.yuyanjia.template.common.config.MybatisRedisCache;
import site.yuyanjia.template.common.model.WebUserRoleDO;
import site.yuyanjia.template.common.util.BaseMapper;

/**
 * 用户角色mapper
 *
 * @author seer
 * @date 2018/6/15 16:49
 */
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface WebUserRoleMapper extends BaseMapper<WebUserRoleDO> {
}