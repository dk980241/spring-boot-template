package site.yuyanjia.template.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class WebRoleDO implements Serializable {
    private static final long serialVersionUID = 3534370363087269547L;

    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDescription;

    /**
     * 是否可用：0-不可用，1-可用
     */
    private Boolean available;

    private Date gmtCreate;

    private Date gmtModified;
}