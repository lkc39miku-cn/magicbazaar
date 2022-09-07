package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.dao.entity.UserCommentStatus;
import com.a243.magicbazaar.dao.mapper.UserCommentStatusMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.service.UserService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.UserConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.StaffThreadLocal;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserConvert userConvert;
    private UserCommentStatusMapper userCommentStatusMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserConvert userConvert) {
        this.userMapper = userMapper;
        this.userConvert = userConvert;
    }

    @Autowired
    public void setUserCommentStatusMapper(UserCommentStatusMapper userCommentStatusMapper) {
        this.userCommentStatusMapper = userCommentStatusMapper;
    }

    @Override
    public Result<List<UserVo>> list(UserParam userParam) {
        if (userParam.getPageParam() != null) {
            List<Long> commentId = new ArrayList<>();
            if (!StrUtil.isBlank(userParam.getCommentStatus())) {
                commentId = userCommentStatusMapper.selectList(new QueryWrapper<UserCommentStatus>().eq("comment_status", userParam.getCommentStatus())).stream().map(UserCommentStatus::getUserId).collect(Collectors.toList());
            }

            IPage<User> iPage = userMapper.selectPage(new Page<>(userParam.getPageParam().getPage(), userParam.getPageParam().getPageSize()), new QueryWrapper<User>()
                    .like(!StrUtil.isBlank(userParam.getUsername()), "username", userParam.getUsername())
                    .like(!StrUtil.isBlank(userParam.getNickname()), "nickname", userParam.getNickname())
                    .eq(!StrUtil.isBlank(userParam.getGender()), "gender", userParam.getGender())
                    .like(!StrUtil.isBlank(userParam.getPhone()), "phone", userParam.getPhone())
                    .like(!StrUtil.isBlank(userParam.getEmail()), "email", userParam.getEmail())
                    .eq(!StrUtil.isBlank(userParam.getPublishStatus()), "publish_status", userParam.getPublishStatus())
                    .eq(!StrUtil.isBlank(userParam.getDeleteStatus()), "delete_status", userParam.getDeleteStatus())
                    .in(commentId.size() != 0, "id", commentId));

            if (!StrUtil.isBlank(userParam.getCommentStatus()) && commentId.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), userConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(userConvert.convert(userMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> delete(UserParam userParam) {
        int i = userMapper.updateById(new User().setId(Long.parseLong(userParam.getId())).setDeleteStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> on(UserParam userParam) {
        int i = userMapper.updateById(new User().setId(Long.parseLong(userParam.getId())).setPublishStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(UserParam userParam) {
        int i = userMapper.updateById(new User().setId(Long.parseLong(userParam.getId())).setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> update(User user) {
        int i = userMapper.updateById(user.setStaffId(StaffThreadLocal.get().getId()));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public Result<String> userCounts() {
        Long toDayRegisterCount = userMapper.selectCount(new QueryWrapper<User>().eq("Date(create_time)", LocalDate.now()));
        Long lastDayRegister = userMapper.selectCount(new QueryWrapper<User>().eq("Date(create_time)", LocalDate.now().plusDays(-1)));
        Long selectCount = userMapper.selectCount(new QueryWrapper<User>().between("Date(create_time)", LocalDate.now().plusDays(-7), LocalDate.now()));

        return Result.ok(toDayRegisterCount + "," + (toDayRegisterCount - lastDayRegister) + "," + (toDayRegisterCount - selectCount));
    }
}
