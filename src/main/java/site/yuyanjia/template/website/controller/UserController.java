package site.yuyanjia.template.website.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import site.yuyanjia.template.common.contant.ResultEnum;
import site.yuyanjia.template.common.model.WebUserDO;
import site.yuyanjia.template.common.util.ResponseUtil;
import site.yuyanjia.template.website.dto.WebUserLoginRequestDTO;
import site.yuyanjia.template.website.dto.WebUserPasswordUpdateRequestDTO;
import site.yuyanjia.template.website.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * UserController
 *
 * @author seer
 * @date 2018/6/22 10:08
 */
@RestController
@RequestMapping(value = "/website/user", method = RequestMethod.POST)
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user-test")
    public String userTest(HttpServletRequest httpServletRequest) {
        Subject subject = SecurityUtils.getSubject();
        Object obj = subject.getPrincipal();
        WebUserDO webUserDO = new WebUserDO();
        BeanUtils.copyProperties(obj, webUserDO);
        log.warn("===== " + webUserDO.toString());

        return "success";
    }

    /**
     * 用户登录
     *
     * @param userLoginRequestDTO
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/user-login")
    public Object userLogin(@RequestBody @Valid WebUserLoginRequestDTO userLoginRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errMsg = bindingResult.getFieldError().getDefaultMessage();
            log.error("用户登录，请求参数校验失败，{}，请求数据 {}", errMsg, userLoginRequestDTO);
            return ResponseUtil.resultFailed(ResultEnum.请求参数不完整);
        }

        Object responseStr = userService.userLogin(userLoginRequestDTO.getUsername(), userLoginRequestDTO.getPassword());
        log.info("用户登录，返回数据 {}", responseStr);
        return responseStr;
    }

    /**
     * 用户密码修改
     *
     * @param userPasswordUpdateRequestDTO
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/user-password-update", method = RequestMethod.POST)
    public Object userPasswordUpdate(@RequestBody @Valid WebUserPasswordUpdateRequestDTO userPasswordUpdateRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errMsg = bindingResult.getFieldError().getDefaultMessage();
            log.error("用户密码修改，请求参数校验失败，{}，请求数据 {}", errMsg, userPasswordUpdateRequestDTO);
            return ResponseUtil.resultFailed(ResultEnum.请求参数不完整);
        }
        Object responseStr = userService.userPasswordUpdate(userPasswordUpdateRequestDTO.getOldPassword(), userPasswordUpdateRequestDTO.getNewPassword());
        log.error("用户密码修改，返回数据 {}", responseStr);
        return responseStr;
    }
}
