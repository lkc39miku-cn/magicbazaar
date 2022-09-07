package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.service.UserService;
import com.a243.magicbazaar.util.FileUploadUtil;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "admin/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "list")
    public Result<List<UserVo>> list(UserParam userParam) {
        return userService.list(userParam);
    }

    @PostMapping(value = "delete")
    public <T> Result<T> delete(UserParam userParam) {
        return userService.delete(userParam);
    }

    @PostMapping(value = "on")
    public <T> Result<T> on(UserParam userParam) {
        return userService.on(userParam);
    }

    @PostMapping(value = "off")
    public <T> Result<T> off(UserParam userParam) {
        return userService.off(userParam);
    }

    @RequestMapping(value = "upload")
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        map.put("savePath", FileUploadUtil.upload(file));
        return map;
    }

    @PostMapping(value = "update")
    public <T> Result<T> update(User user) {
        return userService.update(user);
    }

    @PostMapping(value = "counts")
    public Result<String> userCounts() {
        return userService.userCounts();
    }

}
