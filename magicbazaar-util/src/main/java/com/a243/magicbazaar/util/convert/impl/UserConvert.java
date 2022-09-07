package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.dao.entity.UserCommentStatus;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.dao.mapper.UserCommentStatusMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class UserConvert implements Convert<User, UserVo> {
    private StaffMapper staffMapper;
    private UserCommentStatusMapper userCommentStatusMapper;

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    @Autowired
    public void setUserCommentStatusMapper(UserCommentStatusMapper userCommentStatusMapper) {
        this.userCommentStatusMapper = userCommentStatusMapper;
    }

    public abstract UserVo convert(User user);

    public abstract List<UserVo> convert(List<User> userList);

    @AfterMapping
    public void convert(User user, @MappingTarget UserVo userVo) {
        if (user.getStaffId() != null) {
            userVo.setStaffName(staffMapper.selectById(user.getStaffId()).getUsername());
        }
        userVo.setCommentStatus(userCommentStatusMapper.selectOne(new QueryWrapper<UserCommentStatus>().eq("user_id", user.getId())).getCommentStatus());
    }

    @AfterMapping
    public void convert(List<User> userList, @MappingTarget List<UserVo> userVoList) {
        for (int i = 0; i < userList.size(); i++) {
            convert(userList.get(i), userVoList.get(i));
        }
    }
}
