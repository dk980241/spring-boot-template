package online.yuyanjia.template.mobile.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author seer
 * @date 2018/1/25 16:40
 */
@RestController
@RequestMapping("/test")
public class TestController {
    private static Logger LOGGER = LogManager.getLogger(TestController.class);
    @RequestMapping("/log")
    public Object test() {
        LOGGER.error("yuyanjia");
        return "yuyanjia";
    }
}
