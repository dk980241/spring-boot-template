package site.yuyanjia.template.common.demo;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 定时任务控制demo
 *
 * @author seer
 * @date 2018/10/29 21:33
 */
public class QuartzControlDemo {
    @Autowired
    private Scheduler scheduler;

    /**
     * 获取任务信息
     */
    public void getAllJob() throws Exception {
        /*
        返回数据格式

            [
              {
                "group_name": [
                  {
                    "job_name": {
                      "job_key": "jobKey",
                      "job_description": "任务描述",
                      "job_class": "任务类",
                      "job_cron_trigger": [
                        {
                          "trigger_key": "triggerKey",
                          "trigger_state": "NORMAL",
                          "trigger_description": "啥时候",
                          "trigger_start_time": "开始时间",
                          "trigger_end_time": "结束时间",
                          "trigger_next_fire_time": "下次触发时间",
                          "trigger_previous_fire_time": "上次触发时间",
                          "trigger_final_fire_time": "最后触发时间",
                          "cron_expression": "* * * * * ?"
                        }
                      ],
                      "job_simple_trigger": [
                        {
                          "trigger_key": "triggerKey",
                          "trigger_state": "PAUSED",
                          "trigger_description": "啥时候",
                          "trigger_start_time": "开始时间",
                          "trigger_end_time": "结束时间",
                          "trigger_next_fire_time": "下次触发时间",
                          "trigger_previous_fire_time": "上次触发时间",
                          "trigger_final_fire_time": "最后触发时间",
                          "repeat_count": "重复次数",
                          "repeat_interval": "间隔时间",
                          "triggered_times": "总触发次数"
                        }
                      ]
                    }
                  }
                ]
              }
            ]

         */

        // 获取有所的组
        List<String> jobGroupNameList = scheduler.getJobGroupNames();
        for (String jobGroupName : jobGroupNameList) {
            GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupEquals(jobGroupName);
            Set<JobKey> jobKeySet = scheduler.getJobKeys(jobKeyGroupMatcher);

            for (JobKey jobKey : jobKeySet) {
                String jobName = jobKey.getName();

                // 获取 job 信息
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                String jobDescription = jobDetail.getDescription();
                String jobClass = jobDetail.getClass().getName();

                List<Trigger> triggerList = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggerList) {
                    // 获取 trigger 信息
                    String triggerState = scheduler.getTriggerState(trigger.getKey()).toString();
                    String triggerDescription = trigger.getDescription();
                    String triggerStartTime = trigger.getStartTime().toString();
                    String triggerEndTime = trigger.getEndTime().toString();
                    String triggerNextFireTime = trigger.getNextFireTime().toString();
                    String triggerPreviousFireTime = trigger.getPreviousFireTime().toString();
                    String triggerFinalFireTime = trigger.getFinalFireTime().toString();

                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        continue;
                    }

                    if (trigger instanceof SimpleTrigger) {
                        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                        int repeatCount = simpleTrigger.getRepeatCount();
                        long repeatInterval = simpleTrigger.getRepeatInterval();
                        int triggered_times = simpleTrigger.getTimesTriggered();
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 增加任务
     */
    public void addJob() throws SchedulerException {
        Class<? extends Job> jobClass = null;
        String jobDescription = null;
        String jobName = null;
        String jobGroup = null;
        List<Map<String, String>> jobCronTriggerList = null;
        List<Map<String, String>> jobSimpleTriggerList = null;

        JobKey jobKey = new JobKey(jobName, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // TODO seer 2018/10/26 16:16 任务存在
        }

        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobKey)
                .withDescription(jobDescription)
                .build();

        for (Map<String, String> cronTriggerMap : jobCronTriggerList) {
            TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
            if (scheduler.checkExists(triggerKey)) {
                // TODO seer 2018/10/26 16:17 触发器存在
            }

            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronTriggerMap.get("cronExpression")).withMisfireHandlingInstructionDoNothing();

            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withDescription(cronTriggerMap.get("triggerDescription"))
                    .withSchedule(cronScheduleBuilder)
                    .startAt(new Date(cronTriggerMap.get("triggerStartTime")))
                    .endAt(new Date(cronTriggerMap.get("triggerEndTime")))
                    .build();

            scheduler.scheduleJob(jobDetail, cronTrigger);
        }

        for (Map<String, String> jobSimpleTriggerMap : jobSimpleTriggerList) {
            TriggerKey triggerKey = new TriggerKey(jobName, jobGroup);
            if (scheduler.checkExists(triggerKey)) {
                // TODO seer 2018/10/26 16:17 触发器存在
            }

            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
                    .repeatMinutelyForTotalCount(Integer.valueOf(jobSimpleTriggerMap.get("repeatCount")), Integer.valueOf(jobSimpleTriggerMap.get("repeatInterval")));

            SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withDescription(jobSimpleTriggerMap.get("triggerDescription"))
                    .withSchedule(simpleScheduleBuilder)
                    .startAt(new Date(jobSimpleTriggerMap.get("triggerStartTime")))
                    .endAt(new Date(jobSimpleTriggerMap.get("triggerEndTime")))
                    .build();

            scheduler.scheduleJob(jobDetail, simpleTrigger);
        }
    }

    /**
     * 删除任务
     */
    public void deleteJob() throws SchedulerException {
        JobKey jobKey = null;
        scheduler.deleteJob(jobKey);

        TriggerKey triggerKey = null;
        scheduler.unscheduleJob(triggerKey);
    }

    /**
     * 暂停任务
     */
    public void pauseJob() throws SchedulerException {
        JobKey jobKey = null;
        scheduler.pauseJob(jobKey);

        String groupName = null;
        GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupEquals(groupName);
        scheduler.pauseJobs(jobKeyGroupMatcher);


        TriggerKey triggerKey = null;
        scheduler.pauseTrigger(triggerKey);

        GroupMatcher<TriggerKey> triggerKeyGroupMatcher = GroupMatcher.triggerGroupEquals(groupName);
        scheduler.pauseTriggers(triggerKeyGroupMatcher);
    }

    /**
     * 恢复任务
     */
    public void resumeJob() throws SchedulerException {
        JobKey jobKey = null;
        scheduler.resumeJob(jobKey);

        String groupName = null;
        GroupMatcher<JobKey> jobKeyGroupMatcher = GroupMatcher.jobGroupEquals(groupName);
        scheduler.resumeJobs(jobKeyGroupMatcher);


        TriggerKey triggerKey = null;
        scheduler.resumeTrigger(triggerKey);

        GroupMatcher<TriggerKey> triggerKeyGroupMatcher = GroupMatcher.triggerGroupEquals(groupName);
        scheduler.resumeTriggers(triggerKeyGroupMatcher);
    }

}
