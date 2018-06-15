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
 * 权限
 *
 * @author seer
 * @date 2018/6/15 16:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "web_permission")
public class WebPermissionDO implements Serializable {
    private static final long serialVersionUID = -4474359554927389197L;

    @Id
    private Long id;

    /**
     * 权限名称
     */
    @Column(name = "permission_name")
    private String permissionName;

    /**
     * 权限值
     */
    @Column(name = "permission_value")
    private String permissionValue;

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