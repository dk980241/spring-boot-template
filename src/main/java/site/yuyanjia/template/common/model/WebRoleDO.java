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
 * 角色
 *
 * @author seer
 * @date 2018/6/15 16:44
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "web_role")
public class WebRoleDO implements Serializable {
    private static final long serialVersionUID = 3534370363087269547L;

    @Id
    private Long id;

    /**
     * 角色名称
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "role_description")
    private String roleDescription;

    /**
     * 是否可用：0-不可用，1-可用
     */
    @Column(name = "is_available")
    private Boolean available;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;
}