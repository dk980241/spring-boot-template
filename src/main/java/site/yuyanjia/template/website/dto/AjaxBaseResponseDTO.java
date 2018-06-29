package site.yuyanjia.template.website.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import site.yuyanjia.template.common.dto.BaseResponseDTO;

import java.io.Serializable;

/**
 * Ajax基础响应
 *
 * @author seer
 * @date 2018/6/12 15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AjaxBaseResponseDTO extends BaseResponseDTO implements Serializable {
    private static final long serialVersionUID = -4123938961030522956L;

    /**
     * 总数
     */
    private Integer amount;

    /**
     * 响应数据
     */
    private Object data;
}
