package com.a243.magicbazaar.service;

import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.MenuParam;
import com.a243.magicbazaar.view.vo.MenuVo;

import java.util.List;

public interface MenuPermissionService {
    /**
     * 顶部菜单
     *
     * @return
     */
    Result<List<MenuVo>> parent();

    /**
     * 主菜单
     *
     * @param menuParam
     * @return
     */
    Result<List<MenuVo>> order(MenuParam menuParam);

    /**
     * 子菜单
     *
     * @param menuParam
     * @return
     */
    Result<List<MenuVo>> child(MenuParam menuParam);
}
