package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Order;
import com.a243.magicbazaar.dao.entity.OrderInfo;
import com.a243.magicbazaar.dao.entity.OrderType;
import com.a243.magicbazaar.dao.mapper.OrderInfoMapper;
import com.a243.magicbazaar.dao.mapper.OrderMapper;
import com.a243.magicbazaar.dao.mapper.OrderTypeMapper;
import com.a243.magicbazaar.service.OrderTypeService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.OrderTypeConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderTypeParam;
import com.a243.magicbazaar.view.vo.OrderTypeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderTypeServiceImpl implements OrderTypeService {
    private final OrderTypeMapper orderTypeMapper;
    private final OrderTypeConvert orderTypeConvert;
    private OrderMapper orderMapper;
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    public OrderTypeServiceImpl(OrderTypeMapper orderTypeMapper, OrderTypeConvert orderTypeConvert) {
        this.orderTypeMapper = orderTypeMapper;
        this.orderTypeConvert = orderTypeConvert;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Override
    public Result<List<OrderTypeVo>> list(OrderTypeParam orderTypeParam) {
        if (orderTypeParam.getPageParam() != null) {
            IPage<OrderType> iPage = orderTypeMapper.selectPage(new Page<>(orderTypeParam.getPageParam().getPage(), orderTypeParam.getPageParam().getPageSize()), new QueryWrapper<OrderType>()
                    .like(!StrUtil.isBlank(orderTypeParam.getName()), "name", orderTypeParam.getName())
                    .eq(!StrUtil.isBlank(orderTypeParam.getPublishStatus()), "publish_status", orderTypeParam.getPublishStatus()));

            return Result.ok(Math.toIntExact(iPage.getTotal()), orderTypeConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(orderTypeConvert.convert(orderTypeMapper.selectList(new QueryWrapper<OrderType>().in("id", 8,9,10))));
        }
    }

    @Override
    public <T> Result<T> on(OrderTypeParam orderTypeParam) {
        int i = orderTypeMapper.updateById(new OrderType().setId(Long.parseLong(orderTypeParam.getId())).setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(OrderTypeParam orderTypeParam) {
        List<Order> orderList = orderMapper.selectList(new QueryWrapper<Order>().eq("order_type_id", orderTypeParam.getId()));
        if (orderList.size() == 0) {
            List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_type_id", orderTypeParam.getId()));
            if (orderInfoList.size() == 0) {
                int i = orderTypeMapper.updateById(new OrderType().setId(Long.parseLong(orderTypeParam.getId())).setPublishStatus(0));
                if (i == 1) {
                    return Result.ok(BusinessCode.UPDATE_SUCCESS);
                } else {
                    return Result.fail(BusinessCode.UPDATE_ERROR);
                }
            } else {
                return Result.fail("该状态下还存在信息，无法禁用");
            }
        } else {
            return Result.fail("该状态下还存在信息，无法禁用");
        }
    }
}
