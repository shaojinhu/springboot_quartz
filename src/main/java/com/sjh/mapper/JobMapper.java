package com.sjh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjh.pojo.QuartzJob;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobMapper extends BaseMapper<QuartzJob> {
}
