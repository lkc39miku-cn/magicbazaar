package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StaffRoleParam;
import com.a243.magicbazaar.view.vo.StaffRoleVo;

import java.util.List;

public interface StaffRoleService {
    /**
     * 拥有角色
     *
     * @param staffRoleParam
     * @return
     */
    Result<List<StaffRoleVo>> use(StaffRoleParam staffRoleParam);
}
