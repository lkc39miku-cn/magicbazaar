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
import com.a243.magicbazaar.service.OrderService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.OrderConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.OrderParam;
import com.a243.magicbazaar.view.vo.OrderVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderConvert orderConvert;
    private UserMapper userMapper;
    private OrderInfoMapper orderInfoMapper;
    private CommodityMapper commodityMapper;

    @Autowired
    public OrderServiceImpl(OrderMapper orderMapper, OrderConvert orderConvert) {
        this.orderMapper = orderMapper;
        this.orderConvert = orderConvert;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
    }

    @Override
    public Result<List<OrderVo>> list(OrderParam orderParam) {
        if (orderParam.getPageParam() != null) {
            List<Long> userId = new ArrayList<>();
            if (!StrUtil.isBlank(orderParam.getNickname())) {
                userId = userMapper.selectList(new QueryWrapper<User>().like("nickname", orderParam.getNickname())).stream().map(User::getId).collect(Collectors.toList());
            }

            IPage<Order> iPage = orderMapper.selectPage(new Page<>(orderParam.getPageParam().getPage(), orderParam.getPageParam().getPageSize()), new QueryWrapper<Order>()
                    .like(!StrUtil.isBlank(orderParam.getOrderNumber()), "order_number", orderParam.getOrderNumber())
                    .in(userId.size() != 0, "user_id", userId)
                    .eq(!StrUtil.isBlank(orderParam.getPaypalId()), "paypal_id", orderParam.getPaypalId())
                    .eq(!StrUtil.isBlank(orderParam.getOrderTypeId()), "order_type_id", orderParam.getOrderTypeId())
                    .eq(!StrUtil.isBlank(orderParam.getPublishStatus()), "publish_status", orderParam.getPublishStatus())
                    .eq(!StrUtil.isBlank(orderParam.getDeleteStatus()), "delete_status", orderParam.getDeleteStatus()));

            if (!StrUtil.isBlank(orderParam.getNickname()) && userId.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), orderConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(orderConvert.convert(orderMapper.selectList(new QueryWrapper<>())));
        }
    }


    @Override
    public <T> Result<T> on(OrderParam orderParam) {
        int i = orderMapper.updateById(new Order().setId(Long.parseLong(orderParam.getId())).setPublishStatus(1));
        if (i == 1) {
            List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", orderParam.getId()));
            i = 0;
            for (OrderInfo orderInfo : orderInfoList) {
                i += orderInfoMapper.updateById(orderInfo.setPublishStatus(1));
            }
            if (orderInfoList.size() == i) {
                return Result.ok(BusinessCode.UPDATE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(OrderParam orderParam) {
        int i = orderMapper.updateById(new Order().setId(Long.parseLong(orderParam.getId())).setPublishStatus(0));
        if (i == 1) {
            List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", orderParam.getId()));
            i = 0;
            for (OrderInfo orderInfo : orderInfoList) {
                i += orderInfoMapper.updateById(orderInfo.setPublishStatus(0));
            }
            if (orderInfoList.size() == i) {
                return Result.ok(BusinessCode.UPDATE_SUCCESS);
            } else {
                return Result.fail(BusinessCode.UPDATE_ERROR);
            }
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(OrderParam orderParam) {
        Long userId = orderMapper.selectById(orderParam.getId()).getUserId();
        User user = userMapper.selectById(userId);
        if (user != null) {
            return Result.fail("该订单下还存在用户信息，无法删除");
        } else {
            int i = orderMapper.updateById(new Order().setId(Long.parseLong(orderParam.getId())).setDeleteStatus(1));
            if (i == 1) {
                // 其下子订单删除
                List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", orderParam.getId()));
                i = 0;
                for (OrderInfo orderInfo : orderInfoList) {
                    i += orderInfoMapper.updateById(orderInfo.setDeleteStatus(1));
                }
                if (orderInfoList.size() == i) {
                    return Result.ok(BusinessCode.DELETE_SUCCESS);
                } else {
                    return Result.fail(BusinessCode.DELETE_ERROR);
                }
            } else {
                return Result.ok(BusinessCode.DELETE_ERROR);
            }
        }
    }

    @Override
    public <T> Result<T> fire(OrderParam orderParam) {
        // 待发货订单
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", orderParam.getId()).eq("order_type_id", 2));

        // 如果没有则返回
        if (orderInfoList.size() == 0) {
            return Result.fail("该订单中没有可以发货的订单，请查看详情");
        }

        List<Commodity> commodityList = commodityMapper.selectList(new QueryWrapper<Commodity>().in("id", orderInfoList.stream().map(OrderInfo::getCommodityId).collect(Collectors.toList())));

        // 判断发货商品数量是否足够
        boolean check = true;
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 2) {
                for (Commodity commodity : commodityList) {
                    if (orderInfo.getCommodityId().equals(commodity.getId())) {
                        if (commodity.getCommodityNumber() - orderInfo.getNumber() <= 0) {
                            check = false;
                        }
                        break;
                    }
                }
            }
            if (!check) {
                break;
            }
        }

        if (!check) {
            return Result.fail("该订单中存在订单货物不足，发货失败。如有需要请进行单独发货");
        }

        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 2) {
                Commodity commodity = commodityMapper.selectById(orderInfo.getCommodityId());
                commodityMapper.updateById(commodity.setCommodityNumber(commodity.getCommodityNumber() - orderInfo.getNumber()));
                orderInfoMapper.updateById(orderInfo.setOrderTypeId(3L));
            }
        }
        return Result.ok(BusinessCode.UPDATE_SUCCESS);
    }

    @Override
    public Result<String> orderCounts() {
        Long toDayRegisterCount = orderMapper.selectCount(new QueryWrapper<Order>().eq("Date(create_time)", LocalDate.now()));
        Long lastDayRegister = orderMapper.selectCount(new QueryWrapper<Order>().eq("Date(create_time)", LocalDate.now().plusDays(-1)));
        Long selectCount = orderMapper.selectCount(new QueryWrapper<Order>().between("Date(create_time)", LocalDate.now().plusDays(-7), LocalDate.now()));

        return Result.ok(toDayRegisterCount + "," + (toDayRegisterCount - lastDayRegister) + "," + (toDayRegisterCount - selectCount));
    }

    @Override
    public Result<String> moneyCounts() {
        BigDecimal toDayRegisterCount = new BigDecimal(0);
        List<Order> orders = orderMapper.selectList(new QueryWrapper<Order>().eq("Date(create_time)", LocalDate.now()).in("order_type_id", 9, 10));
        if (orders.size() != 0) {
            for (Order order : orders) {
                List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));
                if (orderInfoList.size() != 0) {
                    for (OrderInfo orderInfo : orderInfoList) {
                        if (orderInfo.getOrderTypeId() == 4 || orderInfo.getOrderTypeId() == 5) {
                            toDayRegisterCount = toDayRegisterCount.add(order.getMoney());
                        }
                    }
                }
            }
        }
        BigDecimal lastDayRegister = new BigDecimal(0);
        List<Order> orders1 = orderMapper.selectList(new QueryWrapper<Order>().eq("Date(create_time)", LocalDate.now().plusDays(-1)).in("order_type_id", 9, 10));
        if (orders1.size() != 0) {
            for (Order order : orders1) {
                List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));
                if (orderInfoList.size() != 0) {
                    for (OrderInfo orderInfo : orderInfoList) {
                        if (orderInfo.getOrderTypeId() == 4 || orderInfo.getOrderTypeId() == 5) {
                            lastDayRegister = lastDayRegister.add(order.getMoney());
                        }
                    }
                }
            }
        }
        BigDecimal selectCount = new BigDecimal(0);
        List<Order> orders2 = orderMapper.selectList(new QueryWrapper<Order>().between("Date(create_time)", LocalDate.now().plusDays(-7), LocalDate.now()).in("order_type_id", 9, 10));
        if (orders2.size() != 0) {
            for (Order order : orders2) {
                List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));
                if (orderInfoList.size() != 0) {
                    for (OrderInfo orderInfo : orderInfoList) {
                        if (orderInfo.getOrderTypeId() == 4 || orderInfo.getOrderTypeId() == 5) {
                            selectCount = selectCount.add(order.getMoney());
                        }
                    }
                }
            }
        }

        return Result.ok(toDayRegisterCount + "," + (toDayRegisterCount.subtract(lastDayRegister)) + "," + (toDayRegisterCount.subtract(selectCount)));
    }
}
