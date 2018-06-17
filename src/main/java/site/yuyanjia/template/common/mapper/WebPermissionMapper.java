package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import site.yuyanjia.template.common.config.MybatisRedisCache;
import site.yuyanjia.template.common.model.WebPermissionDO;
import site.yuyanjia.template.common.util.BaseMapper;

import java.util.List;

/**
 * 权限mapper
 *
 * @author seer
 * @date 2018/6/15 16:48
 */
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface WebPermissionMapper extends BaseMapper<WebPermissionDO> {

}