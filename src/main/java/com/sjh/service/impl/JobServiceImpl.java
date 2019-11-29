package com.sjh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjh.mapper.JobMapper;
import com.sjh.pojo.QuartzJob;
import com.sjh.response.PageResult;
import com.sjh.response.Result;
import com.sjh.service.JobService;
import com.sjh.utils.JobStatus;
import com.sjh.utils.QuartzUtils;
import javafx.print.PrinterJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@SuppressWarnings("all")
public class JobServiceImpl implements JobService {

    @Autowired
    private JobMapper jobMapper;
    @Resource
    private QuartzUtils quartzUtils;

    /**
     * 检查是否已应存在这个任务
     * @param quartzJob
     * @return
     */
    @Override
    public Result isExist(QuartzJob quartzJob) {
        log.info("查看是否存在这个任务————————————————————》》》》》》");
        String jobName = quartzJob.getJobName();//获取任务名称
        String jobClassName = quartzJob.getJobClassName(); //获取执行类名称

        QueryWrapper<QuartzJob> queryWrapperone = new QueryWrapper<>();
        queryWrapperone.eq("job_name",jobName);
        Integer integer = jobMapper.selectCount(queryWrapperone);
        if(integer > 0){
            return Result.error(500,"该任务名称已经存在");
        }
        QueryWrapper<QuartzJob> queryWrappertwo = new QueryWrapper<>();
        queryWrappertwo.eq("job_class_name",jobClassName);
        Integer integer1 = jobMapper.selectCount(queryWrappertwo);
        if(integer1 > 0){
            return Result.error(500,"执行类已经存在");
        }
        return Result.ok(200,"可以创建任务");
    }


    /**
     * 获取任务列表,加分页
     * @return
     */
    @Override
    public PageResult getJobList(Map<String,String> map) {
        log.info("获取任务列表————————————————————》》》》》》");
        Integer page = Integer.parseInt(map.get("page"));
        Integer size = Integer.parseInt(map.get("size"));
        String jobName = map.get("jobName");
        QueryWrapper<QuartzJob> queryWrapper = new QueryWrapper<>();
        if(jobName!=null) queryWrapper.like("job_name",jobName);
        IPage<QuartzJob> iPage = new Page<>(page,size);
        IPage<QuartzJob> iPageResult = jobMapper.selectPage(iPage, queryWrapper);
        long total = iPageResult.getTotal();
        List<QuartzJob> records = iPageResult.getRecords();
        PageResult<QuartzJob> pageResult = new PageResult<>(total,records);
        return pageResult;
    }

    /**
     * 添加任务
     * @param quartzJob
     * @return
     */
    @Override
    public Result addJob(QuartzJob quartzJob) {
        log.info("执行添加任务————————————————————》》》》》》");
        try {
            //创建任务
            quartzUtils.createJob(quartzJob);
            //添加任务列表操作
            quartzJob.setTriggerState(JobStatus.RUNNING.getStatus());
            quartzJob.setTriggerName(quartzJob.getJobName());
            quartzJob.setOldJobName(quartzJob.getJobName());
            quartzJob.setOldJobGroup(quartzJob.getJobGroup());
            jobMapper.insert(quartzJob);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500,"添加任务失败");
        }
        return Result.ok(200,"添加任务成功");
    }

    /**
     * 触发任务
     * @param quartzJob
     * @return
     */
    @Override
    public Result startJob(QuartzJob quartzJob) {
        log.info("触发任务"+quartzJob.getJobName()+"————————————————————》》》》》》");
        try {
            quartzUtils.triggerJob(quartzJob);//触发任务

            //修改数据库的状态
            QueryWrapper<QuartzJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_name",quartzJob.getJobName());
            queryWrapper.eq("job_group",quartzJob.getJobGroup());
            QuartzJob qua = jobMapper.selectOne(queryWrapper);
            qua.setTriggerState(JobStatus.RUNNING.getStatus());
            jobMapper.updateById(qua);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.ok(500,"触发任务失败");
        }
        return Result.ok(200,"触发任务成功");
    }


    /**
     * 暂停定时任务
     * @param quartzJob
     * @return
     */
    @Override
    public Result stopJob(QuartzJob quartzJob) {
        log.info("暂停任务"+quartzJob.getJobName()+"————————————————————》》》》》》");
        try {
            quartzUtils.stopJob(quartzJob);

            //修改数据库状态
            QueryWrapper<QuartzJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_name",quartzJob.getJobName());
            queryWrapper.eq("job_group",quartzJob.getJobGroup());
            QuartzJob qua = jobMapper.selectOne(queryWrapper);
            qua.setTriggerState(JobStatus.STOP.getStatus());
            jobMapper.updateById(qua);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.ok(500,"暂停任务失败");
        }
        return Result.ok(200,"暂停任务成功");
    }

    /**
     * 恢复任务
     * @param quartzJob
     * @return
     */
    @Override
    public Result regainJob(QuartzJob quartzJob) {
        log.info("恢复任务"+quartzJob.getJobName()+"————————————————————》》》》》》");
        try {
            quartzUtils.regainJob(quartzJob);

            //修改数据库状态
            QueryWrapper<QuartzJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_name",quartzJob.getJobName());
            queryWrapper.eq("job_group",quartzJob.getJobGroup());
            QuartzJob qua = jobMapper.selectOne(queryWrapper);
            qua.setTriggerState(JobStatus.RUNNING.getStatus());
            jobMapper.updateById(qua);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.ok(500,"恢复任务失败");
        }
        return Result.ok(200,"恢复任务成功");
    }

    /**
     * 移除任务
     * @param quartzJob
     * @return
     */
    @Override
    public Result removeJob(QuartzJob quartzJob) {
        try {
            quartzUtils.removeJob(quartzJob);

            QueryWrapper<QuartzJob> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("job_name",quartzJob.getJobName());
            queryWrapper.eq("job_group",quartzJob.getJobGroup());
            jobMapper.delete(queryWrapper);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Result.ok(500,"移除任务失败");
        }
        return Result.ok(200,"移除任务成功");
    }


}
