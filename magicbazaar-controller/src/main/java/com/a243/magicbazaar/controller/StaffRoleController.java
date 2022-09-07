package com.a243.magicbazaar.controller;

import com.a243.magicbazaar.service.StaffRoleService;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StaffRoleParam;
import com.a243.magicbazaar.view.vo.StaffRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "admin/staff/role")
public class StaffRoleController {
    private final StaffRoleService staffRoleService;

    @Autowired
    public StaffRoleController(StaffRoleService staffRoleService) {
        this.staffRoleService = staffRoleService;
    }

    @PostMapping(value = "use")
    public Result<List<StaffRoleVo>> use(StaffRoleParam staffRoleParam) {
        return staffRoleService.use(staffRoleParam);
    }
}
