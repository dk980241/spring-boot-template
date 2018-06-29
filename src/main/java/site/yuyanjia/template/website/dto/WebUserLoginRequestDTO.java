package site.yuyanjia.template.website.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * UserLoginRequestDTO
 *
 * @author seer
 * @date 2018/6/27 15:26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUserLoginRequestDTO implements Serializable {
    private static final long serialVersionUID = 7078307588699210115L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}
