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
 * 角色权限
 *
 * @author seer
 * @date 2018/6/15 16:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "web_role_permission")
public class WebRolePermissionDO implements Serializable {
    private static final long serialVersionUID = -2859558477710689415L;

    @Id
    private Long id;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "gmt_create")
    private Date gmtCreate;

    @Column(name = "gmt_modified")
    private Date gmtModified;
}