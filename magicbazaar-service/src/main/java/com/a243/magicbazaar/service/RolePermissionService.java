package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.dto.MenuPermission;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.RolePermissionParam;

import java.util.List;

public interface RolePermissionService {
    /**
     * tree
     *
     * @param rolePermissionParam
     * @return
     */
    Result<List<MenuPermission>> list(RolePermissionParam rolePermissionParam);
}
