package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Menu;
import com.a243.magicbazaar.dao.mapper.MenuMapper;
import com.a243.magicbazaar.service.MenuPermissionService;
import com.a243.magicbazaar.util.convert.impl.MenuConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.MenuParam;
import com.a243.magicbazaar.view.vo.MenuVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MenuPermissionServiceImpl implements MenuPermissionService {
    private final MenuMapper menuMapper;
    private final MenuConvert menuConvert;

    @Autowired
    public MenuPermissionServiceImpl(MenuMapper menuMapper, MenuConvert menuConvert) {
        this.menuMapper = menuMapper;
        this.menuConvert = menuConvert;
    }

    @Override
    public Result<List<MenuVo>> parent() {
        List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", 0));
        return Result.ok(menuConvert.convert(menuList));
    }

    @Override
    public Result<List<MenuVo>> order(MenuParam menuParam) {
        if (!StrUtil.isBlank(menuParam.getParentId())) {
            List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", menuParam.getParentId()));
            return Result.ok(menuConvert.convert(menuList));
        }
        List<Menu> menuList = new ArrayList<>();
        return Result.ok(menuConvert.convert(menuList));
    }

    @Override
    public Result<List<MenuVo>> child(MenuParam menuParam) {
        if (!StrUtil.isBlank(menuParam.getParentId())) {
            List<Menu> menuList = menuMapper.selectList(new QueryWrapper<Menu>().eq("parent_id", menuParam.getParentId()));
            return Result.ok(menuConvert.convert(menuList));
        }
        List<Menu> menuList = new ArrayList<>();
        return Result.ok(menuConvert.convert(menuList));
    }
}
