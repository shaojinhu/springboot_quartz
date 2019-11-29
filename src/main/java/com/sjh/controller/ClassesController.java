package com.sjh.controller;

import com.sjh.response.Result;
import com.sjh.utils.GetClassesUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("class")
@CrossOrigin
public class ClassesController {

    /**
     * 获得全限定包名的任务类,也就是该前台展示，供用户选择要执行的定时任务类
     * @return
     */
    @PostMapping("getClassList")
    public List<String> getClassesList(){
        //是工具类获取全限定类名
        List<String> classes = GetClassesUtils.getClasses("com.sjh.job");
        return classes;
    }

}
