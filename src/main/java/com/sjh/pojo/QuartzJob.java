package com.sjh.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 任务调度实体类
 */
@Data
@TableName("quartz_job")
@ToString
public class QuartzJob implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("job_name")
    private String jobName;     //任务名称
    @TableField("job_group")
    private String jobGroup;    //任务分组
    private String description; //任务描述

    @TableField("job_class_name")
    private String jobClassName; //执行类
    @TableField("cron_expression")
    private String cronExpression; //执行时间
    @TableField("trigger_name")
    private String triggerName; //触发器名称
    @TableField("trigger_state")
    private String triggerState; //触发器状态状态
    @TableField("old_job_name")
    private String oldJobName; //任务名称 用于修改
    @TableField("old_job_group")
    private String oldJobGroup; //任务分组 用于修改

    public QuartzJob() {
        super();
    }
    public QuartzJob(String jobName, String jobGroup, String description, String jobClassName, String cronExpression, String triggerName) {
        super();
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.description = description;
        this.jobClassName = jobClassName;
        this.cronExpression = cronExpression;
        this.triggerName = triggerName;
    }
}