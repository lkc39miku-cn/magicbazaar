package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.dao.entity.UserCommentStatus;
import com.a243.magicbazaar.dao.mapper.IUserCommentStatusDao;
import com.a243.magicbazaar.dao.mapper.StaffMapper;
import com.a243.magicbazaar.dao.mapper.UserCommentStatusMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.service.UserCommentStatusService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.UserCommentStatusConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.UserCommentStatusParam;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserCommentStatusVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserCommentStatusServiceImpl implements UserCommentStatusService {
    private final UserCommentStatusMapper userCommentStatusMapper;
    private final UserCommentStatusConvert userCommentStatusConvert;

    @Resource
    IUserCommentStatusDao userCommentStatusDao;

    @Autowired
    public UserCommentStatusServiceImpl(UserCommentStatusMapper userCommentStatusMapper, UserCommentStatusConvert userCommentStatusConvert) {
        this.userCommentStatusMapper = userCommentStatusMapper;
        this.userCommentStatusConvert = userCommentStatusConvert;
    }

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

    @Override
    public Result<List<UserCommentStatusVo>> list(UserCommentStatusParam userCommentStatusParam) {
        if (userCommentStatusParam.getPageParam() != null) {
            List<Long> userId = new ArrayList<>();
            List<Long> staffId = new ArrayList<>();

            if (!StrUtil.isBlank(userCommentStatusParam.getUsername())) {
                userId = userMapper.selectList(new QueryWrapper<User>().like("nickname", userCommentStatusParam.getUsername())).stream().map(User::getId).collect(Collectors.toList());
            }

            if (!StrUtil.isBlank(userCommentStatusParam.getStaffName())) {
                staffId = staffMapper.selectList(new QueryWrapper<Staff>().like("username", userCommentStatusParam.getStaffName())).stream().map(Staff::getId).collect(Collectors.toList());
            }

            if (!StrUtil.isBlank(userCommentStatusParam.getUsername()) && userId.size() == 0 || !StrUtil.isBlank(userCommentStatusParam.getStaffName()) && staffId.size() == 0) {
                return Result.ok(0, null);
            }

            IPage<UserCommentStatus> iPage = userCommentStatusMapper.selectPage(new Page<>(userCommentStatusParam.getPageParam().getPage(), userCommentStatusParam.getPageParam().getPageSize()), new QueryWrapper<UserCommentStatus>()
                    .in(userId.size() != 0, "user_id", userId)
                    .in(staffId.size() != 0, "staff_id", staffId)
                    .eq(!StrUtil.isBlank(userCommentStatusParam.getCommentStatus()), "comment_status", userCommentStatusParam.getCommentStatus()));

            return Result.ok(Math.toIntExact(iPage.getTotal()), userCommentStatusConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(userCommentStatusConvert.convert(userCommentStatusMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> delete(Integer id) {
        int delete = userCommentStatusDao.delete(id);
        if (delete > 0) {
            return Result.ok(BusinessCode.DELETE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.DELETE_ERROR);
        }
    }

    @Override
    public <T> Result<T> update(UserCommentStatusParam commentStatusParam) {
        int i = userCommentStatusDao.update(commentStatusParam);
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> updateon(UserCommentStatusParam commentStatusParam) {
        int i = userCommentStatusDao.updateon(commentStatusParam);
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> on(UserCommentStatusParam commentStatusParam) {
        int i = userCommentStatusMapper.updateById(new UserCommentStatus().setId(Long.parseLong(commentStatusParam.getId())).setCommentStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(UserCommentStatusParam commentStatusParam) {
        int i = userCommentStatusMapper.updateById(new UserCommentStatus().setId(Long.parseLong(commentStatusParam.getId())).setCommentStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }
}
