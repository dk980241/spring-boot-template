package site.yuyanjia.template.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @author seer
 * @date 2018/6/15 16:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebUserDO implements Serializable {
    private static final long serialVersionUID = 7312528454014665283L;

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 是否锁定：0-锁定，1-正常
     */
    private Boolean locked;

    private Date gmtCreate;

    private Date gmtModified;
}