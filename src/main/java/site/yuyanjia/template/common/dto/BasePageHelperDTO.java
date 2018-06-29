package site.yuyanjia.template.common.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 基础分页
 *
 * @author seer
 * @date 2018/6/20 15:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BasePageHelperDTO implements Serializable {
    private static final long serialVersionUID = 8210698657852953013L;

    /**
     * 页码，从1开始
     */
    @NotNull(message = "页码不能为空")
    private Integer pageNum;

    /**
     * 每页显示数量
     */
    @NotNull(message = "每页显示数量不能为空")
    private Integer pageSize;
}
