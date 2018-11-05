package site.yuyanjia.template.common.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;

/**
 * redis分布式锁demo
 *
 * @author seer
 * @date 2018/11/5 15:57
 */
public class RedisLockDemo {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加锁
     */
    public void lock() {
        List<String> keyList = new ArrayList<>();
        keyList.add("seer");
        keyList.add("yuyanjia");
        keyList.add("600");

        // 加载脚本文件
        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("redis_lock.lua"));
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptSource(scriptSource);
        // 设置脚本返回类型
        defaultRedisScript.setResultType(Long.class);

        // 只用了KEYS[i] 传值，但是 args 不能为空，忘了哪个方法里注释的了，随便给个值，反正用不到
        Long result = (Long) redisTemplate.execute(defaultRedisScript, keyList, keyList);

        /*
            1   成功
            0   失败
         */
        System.out.println(result);

    }

    /**
     * 解锁
     */
    public void unlock() {
        List<String> keyList = new ArrayList<>();
        keyList.add("seer");
        keyList.add("yuyanjia");

        ScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource("redis_unlock.lua"));
        DefaultRedisScript defaultRedisScript = new DefaultRedisScript();
        defaultRedisScript.setScriptSource(scriptSource);
        defaultRedisScript.setResultType(Long.class);

        Long result = (Long) redisTemplate.execute(defaultRedisScript, keyList, keyList);
        System.out.println(result);
    }

}
