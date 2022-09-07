package com.a243.magicbazaar.service;

import com.a243.magicbazaar.dao.entity.Menu;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.MenuParam;
import com.a243.magicbazaar.view.vo.MenuVo;

import java.util.List;

public interface MenuService {
    /**
     * 查询菜单列表
     *
     * @param menuVo
     * @return
     */
    List<Menu> findMenuList(MenuVo menuVo);

    /**
     * 添加
     *
     * @param menuParam
     * @param <T>
     * @return
     */
    <T> Result<T> add(MenuParam menuParam);

    /**
     * 修改
     *
     * @param menuParam
     * @param <T>
     * @return
     */
    <T> Result<T> delete(MenuParam menuParam);

    /**
     * 启用
     *
     * @param menuParam
     * @param <T>
     * @return
     */
    <T> Result<T> on(MenuParam menuParam);

    /**
     * 禁用
     *
     * @param menuParam
     * @param <T>
     * @return
     */
    <T> Result<T> off(MenuParam menuParam);

    /**
     * 修改
     *
     * @param menuParam
     * @param <T>
     * @return
     */
    <T> Result<T> update(MenuParam menuParam);

    /**
     * 父级值
     * @param id
     * @return
     */
    Result<String> getParentMenuId(Long id);

    /**
     * 接口父级id
     * @param id
     * @return
     */
    Result<String> getIInterFaceMenuId(Long id);
}
