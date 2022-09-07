package com.a243.magicbazaar.service.impl;

import com.a243.magicbazaar.dao.entity.StaffRole;
import com.a243.magicbazaar.dao.mapper.StaffRoleMapper;
import com.a243.magicbazaar.service.StaffRoleService;
import com.a243.magicbazaar.util.convert.impl.StaffRoleConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StaffRoleParam;
import com.a243.magicbazaar.view.vo.StaffRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StaffRoleServiceImpl implements StaffRoleService {
    private final StaffRoleMapper staffRoleMapper;
    private final StaffRoleConvert staffRoleConvert;

    @Autowired
    public StaffRoleServiceImpl(StaffRoleMapper staffRoleMapper, StaffRoleConvert staffRoleConvert) {
        this.staffRoleMapper = staffRoleMapper;
        this.staffRoleConvert = staffRoleConvert;
    }

    @Override
    public Result<List<StaffRoleVo>> use(StaffRoleParam staffRoleParam) {
        List<StaffRole> staffRoleList = staffRoleMapper.selectList(new QueryWrapper<StaffRole>().eq("staff_id", staffRoleParam.getId()));
        return Result.ok(staffRoleConvert.convert(staffRoleList));
    }
}
