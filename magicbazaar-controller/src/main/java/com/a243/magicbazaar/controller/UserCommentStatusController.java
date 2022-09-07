package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.UserCommentStatusService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.UserCommentStatusParam;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserCommentStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/user/comment/status")
public class UserCommentStatusController {
    private final UserCommentStatusService userCommentStatusService;

    @Autowired //默认按照类型装配
    public UserCommentStatusController(UserCommentStatusService userCommentStatusService) {
        this.userCommentStatusService = userCommentStatusService;
    }

    @PostMapping(value = "list")
    public Result<List<UserCommentStatusVo>> list(UserCommentStatusParam commentStatusParam) {
        return userCommentStatusService.list(commentStatusParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(UserCommentStatusParam commentStatusParam) {
        userCommentStatusService.updateon(commentStatusParam);
        return userCommentStatusService.on(commentStatusParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(UserCommentStatusParam commentStatusParam) {
        return userCommentStatusService.off(commentStatusParam);
    }


    @RequestMapping("delete")
    @ResponseBody
    public <T> Result<T> selectPage(Integer id){
        return userCommentStatusService.delete(id);
    }

    @RequestMapping("update")
    @ResponseBody
    public <T> Result<T> update(UserCommentStatusParam commentStatusParam){
        return userCommentStatusService.update(commentStatusParam);
    }
}
