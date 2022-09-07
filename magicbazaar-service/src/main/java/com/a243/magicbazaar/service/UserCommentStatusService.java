package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.StoreHouse;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.UserCommentStatusParam;
import com.a243.magicbazaar.view.param.UserParam;
import com.a243.magicbazaar.view.vo.UserCommentStatusVo;

import java.util.List;

public interface UserCommentStatusService {
    /**
     * 查询
     *
     * @param commentStatusParam
     * @return
     */
    Result<List<UserCommentStatusVo>> list(UserCommentStatusParam commentStatusParam);

    /**
     * 删除
     *
     * @param id
     * @param <T>
     * @return
     */
    <T> Result<T> delete(Integer id);

    /**
     * 修改
     *
     * @param commentStatusParam
     * @param <T>
     * @return
     */
    <T> Result<T> update(UserCommentStatusParam commentStatusParam);
    <T> Result<T> updateon(UserCommentStatusParam commentStatusParam);


    /**
     * 启用
     *
     * @param commentStatusParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(UserCommentStatusParam commentStatusParam);

    /**
     * 禁用
     *
     * @param commentStatusParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(UserCommentStatusParam commentStatusParam);
}
