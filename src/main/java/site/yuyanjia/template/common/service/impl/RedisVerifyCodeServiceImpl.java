package site.yuyanjia.template.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import site.yuyanjia.template.common.service.VerifyCodeService;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * redis实现验证码
 *
 * @author seer
 * @date 2018/9/13 20:35
 */
@Service("redisVerifyCodeService")
@Slf4j
public class RedisVerifyCodeServiceImpl implements VerifyCodeService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 是否申请获取验证码
     *
     * @param mobile
     * @return 是-true，否-false
     */
    @Override
    public boolean isRepeatApply(String mobile) {
        Object obj = redisTemplate.opsForValue().get(this.getClass().getName() + "_" + mobile);
        return !ObjectUtils.isEmpty(obj);
    }

    /**
     * 生成验证码
     *
     * @param mobile 手机号
     * @param expire 有效时间(分钟)
     * @return
     */
    @Override
    public String generateVerifyCode(String mobile, Long expire) {
        int codeLength = 6;
        String sources = "0123456789";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int j = 0; j < codeLength; j++) {
            stringBuffer.append(sources.charAt(random.nextInt(9)));
        }
        log.trace("手机号 {}，获取验证码 {}", mobile, stringBuffer.toString());
        redisTemplate.opsForValue().set(this.getClass().getName() + "_" + mobile, stringBuffer.toString(), expire, TimeUnit.MINUTES);
        return stringBuffer.toString();
    }

    /**
     * 校验验证码
     *
     * @param mobile
     * @param verifyCode
     * @return 有效-true，无效-false
     */
    @Override
    public boolean checkVerifyCode(String mobile, String verifyCode) {
        String original = ((String) redisTemplate.opsForValue().get(this.getClass().getName() + "_" + mobile));
        if (StringUtils.equals(verifyCode, original)) {
            redisTemplate.delete(this.getClass().getName() + "_" + mobile);
            return true;
        }
        return false;
    }
}
