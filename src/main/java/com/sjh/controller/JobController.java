package com.sjh.controller;

import com.sjh.pojo.QuartzJob;
import com.sjh.response.PageResult;
import com.sjh.response.Result;
import com.sjh.service.JobService;
import com.sjh.utils.QuartzUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("job")
@CrossOrigin
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 获取任务列表
     * @param map
     * @return
     */
    @PostMapping("getJobList")
    public PageResult getJobList(@RequestBody Map<String,String> map){
        return jobService.getJobList(map);
    }

    /**
     * 添加任务时，用于查看该任务是否已经存在，已经存在则不让进行重复添加
     * @return
     */
    @PostMapping("checkJob")
    public Result checkJob(@RequestBody QuartzJob quartzJob){
        return jobService.isExist(quartzJob);
    }

    /**
     * 添加任务
     * @param quartzJob
     * @return
     */
    @PostMapping("addJob")
    public Result addJob(@RequestBody QuartzJob quartzJob){
        return jobService.addJob(quartzJob);
    }


    /**
     * 触发任务
     * @param quartzJob
     * @return
     */
    @PostMapping("startJob")
    public Result startJob(@RequestBody QuartzJob quartzJob){
        return jobService.startJob(quartzJob);
    }


    /**
     * 暂停定时任务
     * @param quartzJob
     * @return
     */
    @PostMapping("stopJob")
    public Result stopJob(@RequestBody QuartzJob quartzJob){
        return jobService.stopJob(quartzJob);
    }

    /**
     * 恢复任务
     * @param quartzJob
     * @return
     */
    @PostMapping("regainJob")
    public  Result regainJob(@RequestBody QuartzJob quartzJob){
        return jobService.regainJob(quartzJob);
    }

    /**
     * 移除任务
     */
    @PostMapping("removeJob")
    public Result removeJob(@RequestBody QuartzJob quartzJob){
        return jobService.removeJob(quartzJob);
    }
}
