package site.yuyanjia.template.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class WebUserRoleDO implements Serializable {
    private static final long serialVersionUID = 8444477092635406831L;

    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    private Date gmtCreate;

    private Date gmtModified;
}