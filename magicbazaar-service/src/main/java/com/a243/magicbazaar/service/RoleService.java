package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.dto.ChoosePermission;
import com.a243.magicbazaar.dao.entity.Role;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.RoleParam;
import com.a243.magicbazaar.view.vo.RoleVo;

import java.util.List;

public interface RoleService {
    /**
     * 角色信息
     *
     * @param roleParam 参数
     * @return 结果集
     */
    Result<List<RoleVo>> list(RoleParam roleParam);

    /**
     * 启用
     *
     * @param roleParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(RoleParam roleParam);

    /**
     * 禁用
     *
     * @param roleParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(RoleParam roleParam);

    /**
     * 删除
     *
     * @param roleParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(RoleParam roleParam);

    /**
     * 添加
     *
     * @param role
     * @param <T>
     * @return
     */
    <T> Result<T> add(Role role);

    /**
     * 修改
     *
     * @param role
     * @param <T>
     * @return
     */
    <T> Result<T> update(Role role);

    /**
     * 得到符合要求的权限
     *
     * @param roleParam
     * @return
     */
    Result<List<ChoosePermission>> getPermission(RoleParam roleParam);

    /**
     * 更新权限
     *
     * @param roleValue
     * @param choosePermissionList
     * @param <T>
     * @return
     */
    <T> Result<T> updateRole(Long roleValue, List<ChoosePermission> choosePermissionList);
}
