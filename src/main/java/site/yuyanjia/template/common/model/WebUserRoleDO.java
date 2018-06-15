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
 * 用户角色
 *
 * @author seer
 * @date 2018/6/15 16:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "web_user_role")
public class WebUserRoleDO implements Serializable {
    private static final long serialVersionUID = 8444477092635406831L;

    @Id
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;
}