package site.yuyanjia.template.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class WebRolePermissionDO implements Serializable {
    private static final long serialVersionUID = -2859558477710689415L;

    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;

    private Date gmtCreate;

    private Date gmtModified;
}