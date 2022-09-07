package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.Commodity;
import com.a243.magicbazaar.dao.entity.Order;
import com.a243.magicbazaar.dao.entity.OrderInfo;
import com.a243.magicbazaar.dao.entity.User;
import com.a243.magicbazaar.dao.mapper.CommodityMapper;
import com.a243.magicbazaar.dao.mapper.OrderInfoMapper;
import com.a243.magicbazaar.dao.mapper.OrderMapper;
import com.a243.magicbazaar.dao.mapper.UserMapper;
import com.a243.magicbazaar.service.OrderInfoService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.OrderInfoConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderInfoParam;
import com.a243.magicbazaar.view.vo.OrderInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderInfoServiceImpl implements OrderInfoService {
    private final OrderInfoMapper orderInfoMapper;
    private final OrderInfoConvert orderInfoConvert;
    private OrderMapper orderMapper;
    private UserMapper userMapper;
    private CommodityMapper commodityMapper;

    @Autowired
    public OrderInfoServiceImpl(OrderInfoMapper orderInfoMapper, OrderInfoConvert orderInfoConvert) {
        this.orderInfoMapper = orderInfoMapper;
        this.orderInfoConvert = orderInfoConvert;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Override
    public Result<List<OrderInfoVo>> list(OrderInfoParam orderInfoParam) {
        if (orderInfoParam.getPageParam() != null) {
            IPage<OrderInfo> iPage = orderInfoMapper.selectPage(new Page<>(orderInfoParam.getPageParam().getPage(), orderInfoParam.getPageParam().getPageSize()), new QueryWrapper<OrderInfo>()
                    .eq(!StrUtil.isBlank(orderInfoParam.getOrderId()), "order_id", orderInfoParam.getOrderId()));

            return Result.ok(Math.toIntExact(iPage.getTotal()), orderInfoConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(orderInfoConvert.convert(orderInfoMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> on(OrderInfoParam orderInfoParam) {
        int i = orderInfoMapper.updateById(new OrderInfo().setId(Long.parseLong(orderInfoParam.getId())).setPublishStatus(1));
        if (i == 1) {
            // 如果子订单可见 则父级订单一定可见
            i = orderMapper.updateById(new Order().setId(orderInfoMapper.selectById(orderInfoParam.getId()).getOrderId()).setPublishStatus(1));
            if (i == 1) {
                return Result.ok(BusinessCode.UPDATE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(OrderInfoParam orderInfoParam) {
        int i = orderInfoMapper.updateById(new OrderInfo().setId(Long.parseLong(orderInfoParam.getId())).setPublishStatus(0));
        if (i == 1) {
            // 如果所有子订单被隐藏 则父级订单也隐藏
            List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", orderInfoMapper.selectById(orderInfoParam.getId()).getOrderId()));
            boolean check = true;
            for (OrderInfo orderInfo : orderInfoList) {
                if (orderInfo.getPublishStatus() == 1) {
                    check = false;
                    break;
                }
            }
            if (check) {
                // 隐藏父级订单
                i = orderMapper.updateById(new Order().setId(orderInfoMapper.selectById(orderInfoParam.getId()).getOrderId()).setPublishStatus(0));
                if (i == 1) {
                    return Result.ok(BusinessCode.UPDATE_SUCCESS);
                } else {
                    return Result.fail(BusinessCode.UPDATE_ERROR);
                }
            }
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(OrderInfoParam orderInfoParam) {
        Long orderId = orderInfoMapper.selectById(orderInfoParam.getId()).getOrderId();
        Long userId = orderMapper.selectById(orderId).getUserId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            int i = orderInfoMapper.updateById(orderInfoMapper.selectById(orderInfoParam.getId()).setDeleteStatus(1));
            if (i == 1) {
                // 如果所有子订单被删除 则父级订单也同时删除
                List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", orderId));

                boolean check = true;
                for (OrderInfo orderInfo : orderInfoList) {
                    if (orderInfo.getDeleteStatus() == 0) {
                        check = false;
                        break;
                    }
                }

                if (check) {
                    // 删除父订单
                    i = orderMapper.updateById(orderMapper.selectById(orderId).setDeleteStatus(1));
                    if (i == 1) {
                        return Result.ok(BusinessCode.DELETE_SUCCESS);
                    } else {
                        return Result.fail(BusinessCode.DELETE_ERROR);
                    }
                }
                return Result.ok(BusinessCode.DELETE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.DELETE_ERROR);
            }
        } else {
            return Result.fail("该记录下还存在用户信息，无法删除");
        }
    }

    @Override
    public <T> Result<T> fire(OrderInfoParam orderInfoParam) {
        OrderInfo orderInfoOne = orderInfoMapper.selectById(orderInfoParam.getId());
        Commodity commodity = commodityMapper.selectById(orderInfoOne.getCommodityId());
        // 判断商品数量是否足够
        if (commodity.getCommodityNumber() - orderInfoOne.getNumber() >= 0) {

            // 商品数量减少
            commodityMapper.updateById(commodity.setCommodityNumber(commodity.getCommodityNumber() - orderInfoOne.getNumber()));

            // 修改订单状态
            int i = orderInfoMapper.updateById(new OrderInfo().setId(Long.parseLong(orderInfoParam.getId())).setOrderTypeId(3L));

            if (i == 1) {
                return Result.ok(BusinessCode.UPDATE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            return Result.fail("当前货物不足，请补充货物后再次发货");
        }
    }
}
