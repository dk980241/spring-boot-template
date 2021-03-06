package site.yuyanjia.template.common.contant;

/**
 * ajax结果枚举
 *
 * @author seer
 * @date 2018/6/12 15:44
 */
public enum ResultEnum {

    /**
     * 处理成功
     */
    处理成功("00000"),

    /**
     * 请求参数不完整
     */
    请求参数不完整("00001"),

    /**
     * 信息已存在
     */
    信息已存在("00002"),

    /**
     * 请求参数错误
     */
    请求参数错误("00003"),

    /**
     * 信息不存在
     */
    信息不存在("00004"),

    /**
     * 信息不允许修改
     */
    信息不允许修改("00005"),

    /**
     * 用户已存在
     */
    用户已存在("10001"),

    /**
     * 用户登录失败
     */
    用户登录失败("50000"),

    /**
     * 用户登陆过期
     */
    用户登陆过期("50001"),

    /**
     * 权限不足
     */
    权限不足("50002"),

    /**
     * 旧密码校验失败
     */
    旧密码校验失败("50003");

    public String alias;

    ResultEnum(String alias) {
        this.alias = alias;
    }
}
