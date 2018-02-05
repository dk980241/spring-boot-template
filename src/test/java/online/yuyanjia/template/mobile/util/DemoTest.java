package online.yuyanjia.template.mobile.util;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import online.yuyanjia.template.mobile.mapper.UserInfoDOMapper;
import online.yuyanjia.template.mobile.model.DO.UserInfoDO;
import online.yuyanjia.template.mobile.service.UserInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
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

    /**
     * shiro校验
     */
    public void use_shiro_authen() {
        /**
         * 跑起服务来用。junit跑不起来，可能是我的姿势不对
         */
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            LOGGER.error("已经通过身份验证了");
            return;
        }
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("1231231", "123456");
        try {
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException e) {
            LOGGER.error("帐号不存在");
        } catch (IncorrectCredentialsException e) {
            LOGGER.error("密码错误");
        }
    }

    /**
     * shiro生成密码
     */
    @Test
    public void use_shiro_generate_pwd() {
        String password = "980241";
        String username = "yuyanjia";
        String salt = "zheshiyanhousile";

        SimpleHash simpleHash = new SimpleHash("MD5",
                password
                , username + salt, 2);
        LOGGER.error("==== simple hash {} ====", simpleHash.toHex());

        Md5Hash md5Hash = new Md5Hash(password, username + salt, 2);
        LOGGER.warn("==== md5 hash {} ====", md5Hash.toHex());

        // d1ffe510ca0fc7446739f40ce0d163e0
    }
}


