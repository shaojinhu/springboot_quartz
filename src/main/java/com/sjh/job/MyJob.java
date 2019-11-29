package com.sjh.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建一个简单的任务类
 */
public class MyJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:ss;mm").format(new Date());
        //要执行的任务
        System.out.println("我是一个简单的定时任务==================>>>>>>>>>"+format);
    }

}
