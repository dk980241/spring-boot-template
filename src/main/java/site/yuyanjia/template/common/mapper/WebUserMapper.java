package site.yuyanjia.template.common.mapper;

import org.apache.ibatis.annotations.CacheNamespace;
import site.yuyanjia.template.common.config.MybatisRedisCache;
import site.yuyanjia.template.common.model.WebUserDO;
import site.yuyanjia.template.common.util.BaseMapper;

/**
 * 用户mapper
 *
 * @author seer
 * @date 2018/6/15 16:49
 */
@CacheNamespace(implementation = MybatisRedisCache.class)
public interface WebUserMapper extends BaseMapper<WebUserDO> {
}