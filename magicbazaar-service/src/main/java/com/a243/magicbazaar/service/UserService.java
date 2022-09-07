package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserVo;

import java.util.List;

public interface UserService {
    /**
     * 查询用户信息
     *
     * @param userParam 信息参数
     * @return 结果集
     */
    Result<List<UserVo>> list(UserParam userParam);

    /**
     * 删除
     *
     * @param userParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(UserParam userParam);

    /**
     * 启用
     *
     * @param userParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(UserParam userParam);

    /**
     * 禁用
     *
     * @param userParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(UserParam userParam);

    /**
     * 更新
     * @param user
     * @param <T>
     * @return
     */
    <T> Result<T> update(User user);

    /**
     * 统计注册人数
     * @return
     */
    Result<String> userCounts();
}
