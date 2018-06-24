package site.yuyanjia.template;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 *
 * @author seer
 * @date 2018/6/15 15:17
 */
@SpringBootApplication
@MapperScan("site.yuyanjia.template.common.mapper")
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.warn("application start done");
    }
}
