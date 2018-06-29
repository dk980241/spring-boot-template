package site.yuyanjia.template.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 基础响应
 *
 * @author seer
 * @date 2018/6/12 15:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDTO implements Serializable {

    private static final long serialVersionUID = 8801985488178486968L;

    /**
     * 响应结果码
     */
    private String responseCode;

    /**
     * 响应结果描述
     */
    private String responseMsg;

    /**
     * 业务处理吗
     */
    private String resultCode;

    /**
     * 业务处理描述
     */
    private String resultMsg;
}
