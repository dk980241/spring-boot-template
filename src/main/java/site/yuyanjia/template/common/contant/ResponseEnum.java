package site.yuyanjia.template.common.contant;

/**
 * 响应枚举
 *
 * @author seer
 * @date 2018/6/12 15:32
 */
public enum ResponseEnum {

    /**
     * 响应成功
     */
    SUCCESS("0000"),

    /**
     * 响应失败
     */
    FAILED("9999");

    public String alias;

    ResponseEnum(String alias) {
        this.alias = alias;
    }
}
