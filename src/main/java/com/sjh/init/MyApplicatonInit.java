package com.sjh.init;

import com.sjh.mapper.JobMapper;
import com.sjh.pojo.QuartzJob;
import com.sjh.utils.JobStatus;
import com.sjh.utils.QuartzUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Spring Boot应用程序在启动后，会遍历CommandLineRunner接口的实例并运行它们的run方法。
 * 也可以利用@Order注解（或者实现Order接口）来规定所有CommandLineRunner实例的运行顺序。
 */
@Component
@Slf4j
//@Order(value = 1)
public class MyApplicatonInit implements CommandLineRunner {


    @Resource
    private JobMapper jobMapper;
    @Resource
    private QuartzUtils quartzUtils;

    @Override
    public void run(String... args) throws Exception {
        log.info("程序重新启动，尝试启动任务--------------------------》》》》");
        //获得所有任务
        List<QuartzJob> quartzJobs = jobMapper.selectList(null);
        quartzJobs.forEach(items->{
            if(items.getTriggerState().equals(JobStatus.RUNNING.getStatus())){
                try {
                    quartzUtils.triggerJob(items);
                    log.info("启动任务成功--------------------------》》》》");
                } catch (SchedulerException e) {
                    log.info("启动任务失败--------------------------》》》》");
                    e.printStackTrace();
                }
            }
        });
    }
}
