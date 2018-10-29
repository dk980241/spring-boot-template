
/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package site.yuyanjia.template.common.quartz.job;

import org.quartz.Calendar;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.ListenerManager;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerListener;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.TriggerListener;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.quartz.utils.Key;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the main interface of a Quartz Scheduler.
 * <p>
 * <p>
 * A <code>Scheduler</code> maintains a registry of <code>{@link JobDetail}</code>s
 * and <code>{@link Trigger}</code>s. Once registered, the <code>Scheduler</code>
 * is responsible for executing <code>Job</code> s when their associated
 * <code>Trigger</code> s fire (when their scheduled time arrives).
 * </p>
 * <p>
 * <p>
 * <code>Scheduler</code> instances are produced by a <code>{@link SchedulerFactory}</code>.
 * A scheduler that has already been created/initialized can be found and used
 * through the same factory that produced it. After a <code>Scheduler</code>
 * has been created, it is in "stand-by" mode, and must have its
 * <code>start()</code> method called before it will fire any <code>Job</code>s.
 * </p>
 * <p>
 * <p>
 * <code>Job</code> s are to be created by the 'client program', by defining
 * a class that implements the <code>{@link Job}</code>
 * interface. <code>{@link JobDetail}</code> objects are then created (also
 * by the client) to define a individual instances of the <code>Job</code>.
 * <code>JobDetail</code> instances can then be registered with the <code>Scheduler</code>
 * via the <code>scheduleJob(JobDetail, Trigger)</code> or <code>addJob(JobDetail, boolean)</code>
 * method.
 * </p>
 * <p>
 * <p>
 * <code>Trigger</code> s can then be defined to fire individual <code>Job</code>
 * instances based on given schedules. <code>SimpleTrigger</code> s are most
 * useful for one-time firings, or firing at an exact moment in time, with N
 * repeats with a given delay between them. <code>CronTrigger</code> s allow
 * scheduling based on time of day, day of week, day of month, and month of
 * year.
 * </p>
 * <p>
 * <p>
 * <code>Job</code> s and <code>Trigger</code> s have a name and group
 * associated with them, which should uniquely identify them within a single
 * <code>{@link Scheduler}</code>. The 'group' feature may be useful for
 * creating logical groupings or categorizations of <code>Jobs</code> s and
 * <code>Triggers</code>s. If you don't have need for assigning a group to a
 * given <code>Jobs</code> of <code>Triggers</code>, then you can use the
 * <code>DEFAULT_GROUP</code> constant defined on this interface.
 * </p>
 * <p>
 * <p>
 * Stored <code>Job</code> s can also be 'manually' triggered through the use
 * of the <code>triggerJob(String jobName, String jobGroup)</code> function.
 * </p>
 * <p>
 * <p>
 * Client programs may also be interested in the 'listener' interfaces that are
 * available from Quartz. The <code>{@link JobListener}</code> interface
 * provides notifications of <code>Job</code> executions. The <code>{@link TriggerListener}</code>
 * interface provides notifications of <code>Trigger</code> firings. The
 * <code>{@link SchedulerListener}</code> interface provides notifications of
 * <code>Scheduler</code> events and errors.  Listeners can be associated with
 * local schedulers through the {@link ListenerManager} interface.
 * </p>
 * <p>
 * <p>
 * The setup/configuration of a <code>Scheduler</code> instance is very
 * customizable. Please consult the documentation distributed with Quartz.
 * </p>
 *
 * @author James House
 * @author Sharada Jambula
 * @see Job
 * @see JobDetail
 * @see JobBuilder
 * @see Trigger
 * @see TriggerBuilder
 * @see JobListener
 * @see TriggerListener
 * @see SchedulerListener
 */
public interface Scheduler {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constants.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * A (possibly) useful constant that can be used for specifying the group
     * that <code>Job</code> and <code>Trigger</code> instances belong to.
     */
    String DEFAULT_GROUP = Key.DEFAULT_GROUP;

    /**
     * A constant <code>Trigger</code> group name used internally by the
     * scheduler - clients should not use the value of this constant
     * ("RECOVERING_JOBS") for the name of a <code>Trigger</code>'s group.
     *
     * @see JobDetail#requestsRecovery()
     */
    String DEFAULT_RECOVERY_GROUP = "RECOVERING_JOBS";

    /**
     * A constant <code>Trigger</code> group name used internally by the
     * scheduler - clients should not use the value of this constant
     * ("FAILED_OVER_JOBS") for the name of a <code>Trigger</code>'s group.
     *
     * @see JobDetail#requestsRecovery()
     */
    String DEFAULT_FAIL_OVER_GROUP = "FAILED_OVER_JOBS";


    /**
     * A constant <code>JobDataMap</code> key that can be used to retrieve the
     * name of the original <code>Trigger</code> from a recovery trigger's
     * data map in the case of a job recovering after a failed scheduler
     * instance.
     *
     * @see JobDetail#requestsRecovery()
     */
    String FAILED_JOB_ORIGINAL_TRIGGER_NAME = "QRTZ_FAILED_JOB_ORIG_TRIGGER_NAME";

    /**
     * A constant <code>JobDataMap</code> key that can be used to retrieve the
     * group of the original <code>Trigger</code> from a recovery trigger's
     * data map in the case of a job recovering after a failed scheduler
     * instance.
     *
     * @see JobDetail#requestsRecovery()
     */
    String FAILED_JOB_ORIGINAL_TRIGGER_GROUP = "QRTZ_FAILED_JOB_ORIG_TRIGGER_GROUP";

    /**
     * A constant <code>JobDataMap</code> key that can be used to retrieve the
     * fire time of the original <code>Trigger</code> from a recovery
     * trigger's data map in the case of a job recovering after a failed scheduler
     * instance.
     * <p>
     * <p>Note that this is the time the original firing actually occurred,
     * which may be different from the scheduled fire time - as a trigger doesn't
     * always fire exactly on time.</p>
     *
     * @see JobDetail#requestsRecovery()
     */
    String FAILED_JOB_ORIGINAL_TRIGGER_FIRETIME_IN_MILLISECONDS = "QRTZ_FAILED_JOB_ORIG_TRIGGER_FIRETIME_IN_MILLISECONDS_AS_STRING";

    /**
     * A constant <code>JobDataMap</code> key that can be used to retrieve the
     * scheduled fire time of the original <code>Trigger</code> from a recovery
     * trigger's data map in the case of a job recovering after a failed scheduler
     * instance.
     * <p>
     * <p>Note that this is the time the original firing was scheduled for,
     * which may be different from the actual firing time - as a trigger doesn't
     * always fire exactly on time.</p>
     *
     * @see JobDetail#requestsRecovery()
     */
    String FAILED_JOB_ORIGINAL_TRIGGER_SCHEDULED_FIRETIME_IN_MILLISECONDS = "QRTZ_FAILED_JOB_ORIG_TRIGGER_SCHEDULED_FIRETIME_IN_MILLISECONDS_AS_STRING";

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * 获取 Scheduler 名称
     *
     * @return
     * @throws SchedulerException
     */
    String getSchedulerName() throws SchedulerException;

    /**
     * 获取 Scheduler 的实例Id
     *
     * @return
     * @throws SchedulerException
     */
    String getSchedulerInstanceId() throws SchedulerException;

    /**
     * 获取 Scheduler 的 SchedulerContext
     *
     * @return
     * @throws SchedulerException
     */
    SchedulerContext getContext() throws SchedulerException;

    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Scheduler State Management Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 启动 Scheduler
     *
     * @throws SchedulerException
     */
    void start() throws SchedulerException;

    /**
     * 延迟启动 Scheduler
     *
     * @param seconds
     * @throws SchedulerException
     */
    void startDelayed(int seconds) throws SchedulerException;

    /**
     * Scheduler 是否启动
     *
     * @return
     * @throws SchedulerException
     */
    boolean isStarted() throws SchedulerException;

    /**
     * 暂停 Scheduler
     *
     * @throws SchedulerException
     */
    void standby() throws SchedulerException;

    /**
     * Scheduler 是否暂停
     *
     * @return
     * @throws SchedulerException
     */
    boolean isInStandbyMode() throws SchedulerException;

    /**
     * Scheduler 关闭
     *
     * @throws SchedulerException
     */
    void shutdown() throws SchedulerException;

    /**
     * 等所有的任务完成后 Scheduler 关闭
     *
     * @param waitForJobsToComplete
     * @throws SchedulerException
     */
    void shutdown(boolean waitForJobsToComplete)
            throws SchedulerException;

    /**
     * Scheduler 是否关闭
     *
     * @return
     * @throws SchedulerException
     */
    boolean isShutdown() throws SchedulerException;

    /**
     * 获取 SchedulerMetaData 对象
     *
     * @return
     * @throws SchedulerException
     */
    SchedulerMetaData getMetaData() throws SchedulerException;

    /**
     * 获取所有正在执行的 Job
     *
     * @return
     * @throws SchedulerException
     */
    List<JobExecutionContext> getCurrentlyExecutingJobs() throws SchedulerException;

    /**
     * 设置 JobFactory
     *
     * @param factory
     * @throws SchedulerException
     */
    void setJobFactory(JobFactory factory) throws SchedulerException;

    /**
     * 获取 ListenerManager
     *
     * @return
     * @throws SchedulerException
     */
    ListenerManager getListenerManager() throws SchedulerException;

    ///////////////////////////////////////////////////////////////////////////
    ///
    /// Scheduling-related Methods
    ///
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 添加指定的 JobDetail 和 Trigger
     *
     * @param jobDetail
     * @param trigger
     * @return
     * @throws SchedulerException
     */
    Date scheduleJob(JobDetail jobDetail, Trigger trigger)
            throws SchedulerException;

    /**
     * 添加指定的 Trigger
     *
     * @param trigger
     * @return
     * @throws SchedulerException
     */
    Date scheduleJob(Trigger trigger) throws SchedulerException;

    /**
     * 批量添加 JobDetail 和 Trigger
     *
     * @param triggersAndJobs
     * @param replace
     * @throws SchedulerException
     */
    void scheduleJobs(Map<JobDetail, Set<? extends Trigger>> triggersAndJobs, boolean replace) throws SchedulerException;

    /**
     * 添加有多个 Trigger 的 JobDetail
     *
     * @param jobDetail
     * @param triggersForJob
     * @param replace
     * @throws SchedulerException
     */
    void scheduleJob(JobDetail jobDetail, Set<? extends Trigger> triggersForJob, boolean replace) throws SchedulerException;

    /**
     * 删除指定的 Trigger
     *
     * @param triggerKey
     * @return
     * @throws SchedulerException
     */
    boolean unscheduleJob(TriggerKey triggerKey)
            throws SchedulerException;

    /**
     * 批量删除 Trigger
     *
     * @param triggerKeys
     * @return
     * @throws SchedulerException
     */
    boolean unscheduleJobs(List<TriggerKey> triggerKeys)
            throws SchedulerException;

    /**
     * 删除并设置新的 Trigger
     *
     * @param triggerKey
     * @param newTrigger
     * @return
     * @throws SchedulerException
     */
    Date rescheduleJob(TriggerKey triggerKey, Trigger newTrigger)
            throws SchedulerException;

    /**
     * 添加 JobDetail
     *
     * @param jobDetail
     * @param replace
     * @throws SchedulerException
     */
    void addJob(JobDetail jobDetail, boolean replace)
            throws SchedulerException;

    /**
     * 添加持久化 JobDetail
     *
     * @param jobDetail
     * @param replace
     * @param storeNonDurableWhileAwaitingScheduling
     * @throws SchedulerException
     */
    void addJob(JobDetail jobDetail, boolean replace, boolean storeNonDurableWhileAwaitingScheduling)
            throws SchedulerException;

    /**
     * 删除指定的 JobDetail
     *
     * @param jobKey
     * @return
     * @throws SchedulerException
     */
    boolean deleteJob(JobKey jobKey)
            throws SchedulerException;

    /**
     * 批量删除 JobDetail
     *
     * @param jobKeys
     * @return
     * @throws SchedulerException
     */
    boolean deleteJobs(List<JobKey> jobKeys)
            throws SchedulerException;

    /**
     * 立即触发指定的 JobDetail
     *
     * @param jobKey
     * @throws SchedulerException
     */
    void triggerJob(JobKey jobKey)
            throws SchedulerException;

    /**
     * 立即触发指定的 JobDetail
     *
     * @param jobKey
     * @param data
     * @throws SchedulerException
     */
    void triggerJob(JobKey jobKey, JobDataMap data)
            throws SchedulerException;

    /**
     * 暂停指定的 JobDetail
     *
     * @param jobKey
     * @throws SchedulerException
     */
    void pauseJob(JobKey jobKey)
            throws SchedulerException;

    /**
     * 暂停 Group 中所有 JobDetail
     *
     * @param matcher
     * @throws SchedulerException
     */
    void pauseJobs(GroupMatcher<JobKey> matcher) throws SchedulerException;

    /**
     * 暂停指定的 TriggerKey
     *
     * @param triggerKey
     * @throws SchedulerException
     */
    void pauseTrigger(TriggerKey triggerKey)
            throws SchedulerException;

    /**
     * 暂停 Group 中所有 Trigger
     *
     * @param matcher
     * @throws SchedulerException
     */
    void pauseTriggers(GroupMatcher<TriggerKey> matcher) throws SchedulerException;

    /**
     * 恢复指定的 JobDetail
     *
     * @param jobKey
     * @throws SchedulerException
     */
    void resumeJob(JobKey jobKey)
            throws SchedulerException;

    /**
     * 恢复 Group 中所有 JobDetail
     *
     * @param matcher
     * @throws SchedulerException
     */
    void resumeJobs(GroupMatcher<JobKey> matcher) throws SchedulerException;

    /**
     * 恢复指定的 TriggerKey
     *
     * @param triggerKey
     * @throws SchedulerException
     */
    void resumeTrigger(TriggerKey triggerKey)
            throws SchedulerException;

    /**
     * 恢复 Group 中所有 Trigger
     *
     * @param matcher
     * @throws SchedulerException
     */
    void resumeTriggers(GroupMatcher<TriggerKey> matcher) throws SchedulerException;

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    void pauseAll() throws SchedulerException;

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    void resumeAll() throws SchedulerException;

    /**
     * 获取所有 JobGroup 名字
     *
     * @return
     * @throws SchedulerException
     */
    List<String> getJobGroupNames() throws SchedulerException;

    /**
     * 获取 JobGroup 中所有 JobKey
     *
     * @param matcher
     * @return
     * @throws SchedulerException
     */
    Set<JobKey> getJobKeys(GroupMatcher<JobKey> matcher) throws SchedulerException;

    /**
     * 获取 JobDetail 的 Trigger
     *
     * @param jobKey
     * @return
     * @throws SchedulerException
     */
    List<? extends Trigger> getTriggersOfJob(JobKey jobKey)
            throws SchedulerException;

    /**
     * 获取所有 TriggerGroup
     *
     * @return
     * @throws SchedulerException
     */
    List<String> getTriggerGroupNames() throws SchedulerException;

    /**
     * 获取 TriggerGroup 中所有 Trigger
     *
     * @param matcher
     * @return
     * @throws SchedulerException
     */
    Set<TriggerKey> getTriggerKeys(GroupMatcher<TriggerKey> matcher) throws SchedulerException;

    /**
     * 获取所有暂停的 TriggerGroup
     *
     * @return
     * @throws SchedulerException
     */
    Set<String> getPausedTriggerGroups() throws SchedulerException;

    /**
     * 获取指定的 JobDetail
     *
     * @param jobKey
     * @return
     * @throws SchedulerException
     */
    JobDetail getJobDetail(JobKey jobKey)
            throws SchedulerException;

    /**
     * 获取指定的 Trigger
     *
     * @param triggerKey
     * @return
     * @throws SchedulerException
     */
    Trigger getTrigger(TriggerKey triggerKey)
            throws SchedulerException;

    /**
     * 获取指定的 Trigger 的状态
     *
     * @param triggerKey
     * @return
     * @throws SchedulerException
     */
    TriggerState getTriggerState(TriggerKey triggerKey)
            throws SchedulerException;

    /**
     * 重新识别指定的 Trigger 的状态
     *
     * @param triggerKey
     * @throws SchedulerException
     */
    void resetTriggerFromErrorState(TriggerKey triggerKey)
            throws SchedulerException;

    /**
     * 给 Scheduler 添加一个指定的 Calendar
     *
     * @param calName
     * @param calendar
     * @param replace
     * @param updateTriggers
     * @throws SchedulerException
     */
    void addCalendar(String calName, Calendar calendar, boolean replace, boolean updateTriggers)
            throws SchedulerException;

    /**
     * 从 Scheduler 删除 Calendar
     *
     * @param calName
     * @return
     * @throws SchedulerException
     */
    boolean deleteCalendar(String calName) throws SchedulerException;

    /**
     * 根据名称获取 Calendar
     *
     * @param calName
     * @return
     * @throws SchedulerException
     */
    Calendar getCalendar(String calName) throws SchedulerException;

    /**
     * 获取所有的 Calendar
     *
     * @return
     * @throws SchedulerException
     */
    List<String> getCalendarNames() throws SchedulerException;


    /**
     * 中断指定的 JobDetail
     *
     * @param jobKey
     * @return
     * @throws UnableToInterruptJobException
     */
    boolean interrupt(JobKey jobKey) throws UnableToInterruptJobException;

    /**
     * 中断指定的 JobDetail
     *
     * @param fireInstanceId
     * @return
     * @throws UnableToInterruptJobException
     */
    boolean interrupt(String fireInstanceId) throws UnableToInterruptJobException;

    /**
     * 检查 JobDetail 是否存在
     *
     * @param jobKey
     * @return
     * @throws SchedulerException
     */
    boolean checkExists(JobKey jobKey) throws SchedulerException;

    /**
     * 检查 Trigger 是否存在
     *
     * @param triggerKey
     * @return
     * @throws SchedulerException
     */
    boolean checkExists(TriggerKey triggerKey) throws SchedulerException;

    /**
     * 清除所有任务
     *
     * @throws SchedulerException
     */
    void clear() throws SchedulerException;


}
