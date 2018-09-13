package site.yuyanjia.template.common.service;

/**
 * 验证码
 *
 * @author seer
 * @date 2018/9/13 20:29
 */
public interface VerifyCodeService {

    /**
     * 是否申请获取验证码
     *
     * @param mobile
     * @return 是-true，否-false
     */
    boolean isRepeatApply(String mobile);

    /**
     * 生成验证码
     *
     * @param mobile 手机号
     * @param expire 有效时间(分钟)
     * @return
     */
    String generateVerifyCode(String mobile, Long expire);

    /**
     * 校验验证码
     *
     * @param mobile
     * @param verifyCode
     * @return 有效-true，无效-false
     */
    boolean checkVerifyCode(String mobile, String verifyCode);
}
