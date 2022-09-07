package com.a243.magicbazaar.util.convert.impl;

import com.a243.magicbazaar.dao.entity.Menu;
import com.a243.magicbazaar.util.convert.Convert;
import com.a243.magicbazaar.view.vo.MenuVo;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public abstract class MenuConvert implements Convert<Menu, MenuVo> {
    public abstract MenuVo convert(Menu menu);

    public abstract List<MenuVo> convert(List<Menu> menuList);
}
