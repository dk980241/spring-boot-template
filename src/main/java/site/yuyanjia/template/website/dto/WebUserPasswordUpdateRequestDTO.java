package site.yuyanjia.template.website.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * UserPasswordUpdateRequestDTO
 *
 * @author seer
 * @date 2018/6/27 15:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebUserPasswordUpdateRequestDTO implements Serializable {
    private static final long serialVersionUID = -6447330599401624351L;

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}
