package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.UserCommentStatus;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.UserCommentStatusVo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class UserCommentStatusConvert implements Convert<UserCommentStatus, UserCommentStatusVo> {
    private UserMapper userMapper;
    private StaffMapper staffMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setStaffMapper(StaffMapper staffMapper) {
        this.staffMapper = staffMapper;
    }

    public abstract UserCommentStatusVo convert(UserCommentStatus userCommentStatus);

    public abstract List<UserCommentStatusVo> convert(List<UserCommentStatus> userCommentStatusList);

    @AfterMapping
    public void convert(UserCommentStatus userCommentStatus, @MappingTarget UserCommentStatusVo userCommentStatusVo) {
        userCommentStatusVo.setUserName(userMapper.selectById(userCommentStatus.getUserId()).getNickname());
        if (userCommentStatus.getStaffId() != null) {
            userCommentStatusVo.setStaffName(staffMapper.selectById(userCommentStatus.getStaffId()).getUsername());
        }
    }

    @AfterMapping
    public void convert(List<UserCommentStatus> userCommentStatusList, @MappingTarget List<UserCommentStatusVo> userCommentStatusVoList) {
        for (int i = 0; i < userCommentStatusList.size(); i++) {
            convert(userCommentStatusList.get(i), userCommentStatusVoList.get(i));
        }
    }
}
