package com.sjh.utils;

import com.sjh.pojo.QuartzJob;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时任务工具类
 */
@Component
public class QuartzUtils {

    @Resource
    private Scheduler scheduler;


    /**
     * 创建定时任务
     * @param Job  定时任务实体类
     */
    public void createJob(QuartzJob Job) throws SchedulerException, ClassNotFoundException {
            //根据全限定类型获取字节码文件
            Class cls = Class.forName(Job.getJobClassName());
            //创建Job并检验Job是否存在
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(Job.getJobName(),Job.getJobGroup())
                    .withDescription(Job.getDescription()).build();
            //任务触发的时间
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(Job.getCronExpression().trim());
            //触发器
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(Job.getJobName(),Job.getJobGroup())
                   // .startNow()  //创建之后立即执行
                    //.startAt()   //创建任务开始时间
                    //.endAt()      //创建任务结束时间
                    .withSchedule(cronScheduleBuilder).build();
            //交给scheduler执行器安排触发任务
            scheduler.scheduleJob(jobDetail,trigger);
    }

    /**
     * 触发定时任务
     * @param job
     * @throws SchedulerException
     */
    public void triggerJob(QuartzJob job) throws SchedulerException {
        JobKey jobKey = new JobKey(job.getJobName(),job.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 暂停定时任务
     */
    public void stopJob(QuartzJob quartzJob) throws SchedulerException {
        JobKey jobKey = new JobKey(quartzJob.getJobName(),quartzJob.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复任务
     */
    public void regainJob(QuartzJob quartzJob) throws SchedulerException {
        JobKey jobKey = new JobKey(quartzJob.getJobName(),quartzJob.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 移除任务
     */
    public void removeJob(QuartzJob quartzJob) throws SchedulerException {
        JobKey jobKey = new JobKey(quartzJob.getJobName(),quartzJob.getJobGroup());
        TriggerKey triggerKey = TriggerKey.triggerKey(quartzJob.getJobName(), quartzJob.getJobGroup());
        scheduler.pauseTrigger(triggerKey);//停止触发器
        scheduler.unscheduleJob(triggerKey);//移除触发器
        scheduler.deleteJob(jobKey);//删除任务
    }

}
