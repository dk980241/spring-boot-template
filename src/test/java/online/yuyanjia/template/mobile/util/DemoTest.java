package online.yuyanjia.template.mobile.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import online.yuyanjia.template.mobile.mapper.UserInfoDOMapper;
import online.yuyanjia.template.mobile.model.DO.UserInfoDO;
import online.yuyanjia.template.mobile.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;

/**
 * 测试demo
 *
 * @author seer
 * @date 2018/1/31 14:35
 */
@CacheConfig
public class DemoTest extends BaseJunitTest {
    private static Logger LOGGER = LogManager.getLogger(DemoTest.class);

    @Autowired
    private UserInfoDOMapper userInfoDOMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DemoTestSub demoTestSub;

    /**
     * 使用mapper
     */
    @Test
    public void use_mapper() {
        // List<UserInfoDO> list = userInfoDOMapper.selectAll();
        // list.forEach(LOGGER::error);

        UserInfoDO userInfoDO = userInfoDOMapper.selectByPrimaryKey(1);
        LOGGER.error(userInfoDO);
    }

    /**
     * 使用分页
     */
    @Test
    public void use_pagehelper() {
        PageHelper.startPage(2, 2);
        List<UserInfoDO> list = userInfoDOMapper.selectAll();
        PageInfo<UserInfoDO> pageInfo = new PageInfo<UserInfoDO>(list);
        pageInfo.getList().forEach(LOGGER::error);
    }

    /**
     * redis保存
     *
     * @throws Exception
     */
    @Test
    public void use_redis_save() throws Exception {
        UserInfoDO userInfoDO = UserInfoDO.builder().uid(110).build();
        ValueOperations<String, UserInfoDO> operations = redisTemplate.opsForValue();
        operations.set("user", userInfoDO);
        LOGGER.error("保存成功");
    }

    /**
     * redis取值
     */
    @Test
    public void use_redis_select() {
        ValueOperations operations = redisTemplate.opsForValue();
        UserInfoDO userInfoDO = ((UserInfoDO) operations.get("user"));
        LOGGER.error(userInfoDO);
    }

    /**
     * redis删除
     */
    @Test
    public void use_redis_delete() {
        redisTemplate.delete("user");
    }

    /**
     * redis注解操作
     */
    @Test
    public void use_redis_save_anno() {
        demoTestSub.use_redis_save_anno(UserInfoDO.builder().uid(100).build());
    }
}


