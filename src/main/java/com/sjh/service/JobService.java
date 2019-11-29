package com.sjh.service;

import com.sjh.pojo.QuartzJob;
import com.sjh.response.PageResult;
import com.sjh.response.Result;

import java.util.Map;

public interface JobService {
    Result isExist(QuartzJob quartzJob);

    PageResult getJobList(Map<String,String> map);

    Result addJob(QuartzJob quartzJob);

    Result startJob(QuartzJob quartzJob);

    Result stopJob(QuartzJob quartzJob);

    Result regainJob(QuartzJob quartzJob);

    Result removeJob(QuartzJob quartzJob);
}
