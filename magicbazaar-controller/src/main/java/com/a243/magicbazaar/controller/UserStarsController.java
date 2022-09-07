package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.TableData;
import com.a243.magicbazaar.service.UserStarsService;
import com.a243.magicbazaar.util.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller //标识表现层组件,定义一个 Controller 控制器
@RequestMapping("admin/user/comment/stars") //摘花路径地址
public class UserStarsController {
    @Resource  //按照名称装配
    UserStarsService userStarsService;
    @RequestMapping("list")
    @ResponseBody
    public TableData selectPage(Integer page, Integer limit ,String nickname,String order_number){
        return userStarsService.selectPages(page,limit,nickname,order_number);
    }

    @RequestMapping("delete")
    @ResponseBody
    public <T> Result<T> selectPage(Integer id){
       return userStarsService.delete(id);
    }
}
