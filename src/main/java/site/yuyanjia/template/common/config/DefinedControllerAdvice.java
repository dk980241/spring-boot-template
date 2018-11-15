package site.yuyanjia.template.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.yuyanjia.template.common.util.ResponseUtil;


/**
 * controller 补充处理
 *
 * @author seer
 * @date 2018/11/15 11:39
 */
@ControllerAdvice
@Slf4j
public class DefinedControllerAdvice {

    /**
     * 异常补充处理
     * <p>
     * 处理controller及其调用层的异常，
     * 自定义返回信息
     *
     * @param ex 异常
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object exceptionAdvice(Exception ex) {
        log.error("system error", ex);
        return ResponseUtil.responseFailed();
    }
}
