package site.yuyanjia.template.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限
 *
 * @author seer
 * @date 2018/6/15 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebPermissionDO implements Serializable {
    private static final long serialVersionUID = -4474359554927389197L;

    private Long id;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限值
     */
    private String permissionValue;

    /**
     * 是否可用：0-不可用，1-可用
     */
    private Boolean available;

    private Date gmtCreate;

    private Date gmtModified;
}