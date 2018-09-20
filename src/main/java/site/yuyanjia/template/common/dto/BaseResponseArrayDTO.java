package site.yuyanjia.template.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 响应数组数据
 *
 * @author seer
 * @date 2018/6/12 15:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseResponseArrayDTO extends BaseResponseDTO implements Serializable {
    private static final long serialVersionUID = -4123938961030522956L;

    /**
     * 总数
     */
    private Integer amount;

    /**
     * 响应数据
     */
    private List<?> data;
}
