package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.Staff;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.StaffParam;
import com.a243.magicbazaar.view.vo.StaffVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface StaffService {
    /**
     * 根据员工的账号和密码寻找员工
     *
     * @param <T>      泛型
     * @param staff    员工
     * @param request  客户端请求
     * @param response 服务端响应
     * @return 是否成功登录
     */
    <T> Result<T> findStaffByNameAndPassword(Staff staff, String autoLogin, HttpServletRequest request, HttpServletResponse response);

    /**
     * 检查token是否正确
     *
     * @param token token
     * @return staff对象
     */
    Staff checkStaff(String token);

    /**
     * 注销账号
     *
     * @param <T>     泛型
     * @param request 客户端请求
     * @return 是否成功退出
     */
    <T> Result<T> logout(HttpServletRequest request);

    /**
     * 员工信息
     *
     * @param staffParam 参数
     * @return 结果集
     */
    Result<List<StaffVo>> list(StaffParam staffParam);

    /**
     * 手机验证
     *
     * @param response
     * @param phone
     */
    void phoneCode(HttpServletResponse response, String phone);

    /**
     * 邮箱验证
     *
     * @param response
     * @param email
     */
    void emailCode(HttpServletResponse response, String email);

    /**
     * 注册
     *
     * @param request
     * @param response
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> add(HttpServletRequest request, HttpServletResponse response, StaffParam staffParam);

    /**
     * 修改
     *
     * @param request
     * @param response
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> update(HttpServletRequest request, HttpServletResponse response, StaffParam staffParam);

    /**
     * 启用
     *
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(StaffParam staffParam);

    /**
     * 禁用
     *
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(StaffParam staffParam);

    /**
     * 删除
     *
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(StaffParam staffParam);

    /**
     * 修改角色
     *
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> role(StaffParam staffParam);

    /**
     * 用户信息
     * @return
     */
    Result<StaffVo> info();

    /**
     * 重名
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> checkName(StaffParam staffParam);

    /**
     * 邮箱验证
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> emailCheck(StaffParam staffParam);

    /**
     * 检查密码
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> checkPassword(StaffParam staffParam);

    /**
     * 改变密码
     * @param staffParam
     * @param <T>
     * @return
     */
    <T> Result<T> changePassword(StaffParam staffParam);
}
