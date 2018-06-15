package site.yuyanjia.template.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "web_user")
public class WebUserDO implements Serializable {
    private static final long serialVersionUID = 7312528454014665283L;

    @Id
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
     * 是否锁定：0-锁定，1-正常
     */
    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;
}