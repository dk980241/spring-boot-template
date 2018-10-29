package site.yuyanjia.template.common.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * QuartzDemoJob
 *
 * @author seer
 * @date 2018/10/23 14:47
 */
@Slf4j
public class QuartzDemoJob extends QuartzJobBean {
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("这里写定时任务业务逻辑。。。");
    }
}
