package com.a243.magicbazaar.service.impl.reception;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.reception.ReOrderService;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.CommodityDisputeCommentConvert;
import com.a243.magicbazaar.util.convert.impl.OrderConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.util.thread.UserThreadLocal;
import com.a243.magicbazaar.view.param.OrderParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeCommentVo;
import com.a243.magicbazaar.view.vo.OrderVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class ReOrderServiceImpl implements ReOrderService {
    private final OrderMapper orderMapper;
    private final OrderConvert orderConvert;

    @Autowired
    public ReOrderServiceImpl(OrderMapper orderMapper, OrderConvert orderConvert) {
        this.orderMapper = orderMapper;
        this.orderConvert = orderConvert;
    }

    private OrderInfoMapper orderInfoMapper;
    private CommodityDisputeMapper commodityDisputeMapper;
    private CommodityDisputeCommentMapper commodityDisputeCommentMapper;
    private CommodityDisputeCommentConvert commodityDisputeCommentConvert;
    private UserStarsMapper userStarsMapper;
    private CommodityDisputeInfoMapper commodityDisputeInfoMapper;
    private UserMapper userMapper;

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Autowired
    public void setCommodityDisputeMapper(CommodityDisputeMapper commodityDisputeMapper) {
        this.commodityDisputeMapper = commodityDisputeMapper;
    }

    @Autowired
    public void setCommodityDisputeCommentMapper(CommodityDisputeCommentMapper commodityDisputeCommentMapper) {
        this.commodityDisputeCommentMapper = commodityDisputeCommentMapper;
    }

    @Autowired
    public void setCommodityDisputeCommentConvert(CommodityDisputeCommentConvert commodityDisputeCommentConvert) {
        this.commodityDisputeCommentConvert = commodityDisputeCommentConvert;
    }

    @Autowired
    public void setUserStarsMapper(UserStarsMapper userStarsMapper) {
        this.userStarsMapper = userStarsMapper;
    }

    @Autowired
    public void setCommodityDisputeInfoMapper(CommodityDisputeInfoMapper commodityDisputeInfoMapper) {
        this.commodityDisputeInfoMapper = commodityDisputeInfoMapper;
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }



    @Override
    public Result<List<OrderVo>> list(OrderParam orderParam) {
        IPage<Order> iPage = orderMapper.selectPage(new Page<>(orderParam.getPage(), orderParam.getPageSize()), new QueryWrapper<Order>()
        .like(!StrUtil.isBlank(orderParam.getOrderNumber()), "order_number", orderParam.getOrderNumber())
        .eq("user_id", UserThreadLocal.get().getId())
        .eq(!StrUtil.isBlank(orderParam.getType()), "order_type_id", orderParam.getType())
        .eq("publish_status", 1)
        .orderByDesc("update_time"));

        System.out.println(iPage.getRecords());
        return Result.ok(Math.toIntExact(iPage.getPages()), orderConvert.convert(iPage.getRecords()));
    }

    @Override
    public <T> Result<T> changeAll(OrderParam orderParam) {

        Order order = orderMapper.selectById(orderParam.getId());
        // ??????????????? ??????????????????????????????????????????????????????????????????????????????
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

        // ???????????????????????????????????????
        boolean check = false;
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 4 || orderInfo.getOrderTypeId() == 5 || orderInfo.getOrderTypeId() == 14) {
                check = true;
                break;
            }
        }

        if (!check) {
            return Result.fail("???????????????????????????????????????");
        }

        // ??????????????????
        if (order.getOrderTypeId() != 8) {
            orderMapper.updateById(order.setOrderTypeId(8L));
        }


        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 4 || orderInfo.getOrderTypeId() == 5 || orderInfo.getOrderTypeId() == 14) {
                changeOne(new OrderParam().setId(String.valueOf(orderInfo.getId())));
            }

        }
        return Result.ok(BusinessCode.UPDATE_SUCCESS);
    }

    @Override
    public Result<List<CommodityDisputeCommentVo>> disputeInfo(OrderParam orderParam) {
        Long order_info_id = commodityDisputeInfoMapper.selectOne(new QueryWrapper<CommodityDisputeInfo>().eq("order_info_id", orderParam.getCommodityDisputeInfoId())).getId();
        List<CommodityDisputeComment> commodityDisputeCommentList = commodityDisputeCommentMapper.selectList(new QueryWrapper<CommodityDisputeComment>()
                .eq(!StrUtil.isBlank(String.valueOf(order_info_id)), "commodity_dispute_info_id", order_info_id));

        return Result.ok(commodityDisputeCommentConvert.convert(commodityDisputeCommentList));
    }

    @Override
    public <T> Result<T> exitAll(OrderParam orderParam) {
        // ??????????????????
        Order order = orderMapper.selectById(orderParam.getId());


        // ??????????????? ??????????????????????????????????????????????????????????????????????????????
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

        // ???????????????????????????????????????
        boolean check = false;
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 2 || orderInfo.getOrderTypeId() == 3 || orderInfo.getOrderTypeId() == 4 || orderInfo.getOrderTypeId() == 5 || orderInfo.getOrderTypeId() == 13) {
                check = true;
                break;
            }
        }

        if (!check) {
            return Result.fail("???????????????????????????????????????");
        }


        if (order.getOrderTypeId() != 8) {
            orderMapper.updateById(order.setOrderTypeId(8L));
        }

        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 2 || orderInfo.getOrderTypeId() == 3 || orderInfo.getOrderTypeId() == 4 || orderInfo.getOrderTypeId() == 5 || orderInfo.getOrderTypeId() == 13) {
                exitOne(new OrderParam().setId(String.valueOf(orderInfo.getId())));
            }

        }
        return Result.ok(BusinessCode.UPDATE_SUCCESS);
    }

    @Override
    public <T> Result<T> exitOne(OrderParam orderParam) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderParam.getId());

        // ??????????????????
        orderInfoMapper.updateById(orderInfo.setOrderTypeId(7L));

        // ???????????????
        Order order = orderMapper.selectById(orderInfo.getOrderId());

        // ?????????????????????
        if (order.getOrderTypeId() != 8) {
            orderMapper.updateById(order.setOrderTypeId(8L));
        }

        // ?????????????????????????????????
        CommodityDispute commodityDispute = commodityDisputeMapper.selectOne(new QueryWrapper<CommodityDispute>().eq("order_id", order.getId()));

        // ?????????????????????????????? ??????????????????
        if (commodityDispute == null) {
            // ????????????
            Long outTradeNo = System.currentTimeMillis();
            CommodityDispute commodityDisputeParent = new CommodityDispute();
            commodityDisputeMapper.insert(commodityDisputeParent
                    .setDisputeNumber(String.valueOf(outTradeNo))
                    .setOrderId(order.getId())
                    .setDisputeStatus(0)
                    .setPublishStatus(1)
                    .setDeleteStatus(0));

            // ???????????????
            CommodityDisputeInfo commodityDisputeInfo = new CommodityDisputeInfo();
            commodityDisputeInfoMapper.insert(commodityDisputeInfo
                    .setCommodityDisputeId(commodityDisputeParent.getId())
                    .setOrderInfoId(orderInfo.getId())
                    .setDisputeStatus(0)
                    .setPublishStatus(1)
                    .setDeleteStatus(0));

            // ???????????????
            CommodityDisputeComment commodityDisputeComment = new CommodityDisputeComment();
            commodityDisputeCommentMapper.insert(commodityDisputeComment
                    .setCommodityDisputeInfoId(commodityDisputeInfo.getId())
                    .setUserId(UserThreadLocal.get().getId())
                    .setContext("??????"));
        } else {
            // ????????????????????? ???????????????????????????

            // ?????????????????????
            commodityDisputeMapper.updateById(commodityDispute.setDisputeStatus(0));

            // ???????????????
            CommodityDisputeInfo commodityDisputeInfo = commodityDisputeInfoMapper.selectOne(new QueryWrapper<CommodityDisputeInfo>().eq("order_info_id", orderInfo.getId()));

            // ??????????????? ???????????????
            if (commodityDisputeInfo != null) {

                commodityDisputeInfoMapper.updateById(commodityDisputeInfo.setDisputeStatus(0));

                // ????????????????????????
                commodityDisputeCommentMapper.delete(new QueryWrapper<CommodityDisputeComment>().eq("commodity_dispute_info_id", commodityDisputeInfo.getId()));

                // ???????????????
                CommodityDisputeComment commodityDisputeComment = new CommodityDisputeComment();
                commodityDisputeCommentMapper.insert(commodityDisputeComment
                        .setCommodityDisputeInfoId(commodityDisputeInfo.getId())
                        .setUserId(UserThreadLocal.get().getId())
                        .setContext("??????"));

            } else {
                // ?????????????????? ??????????????? ?????????????????????

                CommodityDisputeInfo commodityDisputeInfoChild = new CommodityDisputeInfo();

                commodityDisputeInfoMapper.insert(commodityDisputeInfoChild
                        .setCommodityDisputeId(commodityDispute.getId())
                        .setOrderInfoId(orderInfo.getId())
                        .setDisputeStatus(0)
                        .setPublishStatus(1)
                        .setDeleteStatus(0));

                // ???????????????
                CommodityDisputeComment commodityDisputeComment = new CommodityDisputeComment();
                commodityDisputeCommentMapper.insert(commodityDisputeComment
                        .setCommodityDisputeInfoId(commodityDisputeInfoChild.getId())
                        .setUserId(UserThreadLocal.get().getId())
                        .setContext("??????"));
            }

        }
        return Result.ok(BusinessCode.UPDATE_SUCCESS);
    }

    @Override
    public <T> Result<T> haveOne(OrderParam orderParam) {
        // ????????????
        OrderInfo orderInfo = orderInfoMapper.selectById(orderParam.getId());

        // ???????????????????????????????????? ????????? ??????????????????????????????
        UserStars userStars = userStarsMapper.selectOne(new QueryWrapper<UserStars>().eq("order_info_id", orderInfo.getId()));
        if (userStars == null) {
            orderInfoMapper.updateById(orderInfo.setOrderTypeId(4L));
        } else {
            orderInfoMapper.updateById(orderInfo.setOrderTypeId(5L));
        }

        // ??????????????????
        Order order = orderMapper.selectById(orderInfo.getOrderId());

        if (order.getOrderTypeId() != 8) {
            // ???????????????????????????????????????

            // ?????????????????????
            List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

            boolean check = true;
            for (OrderInfo orderInfoChild : orderInfoList) {
                if (orderInfoChild.getOrderTypeId() != 4 && orderInfoChild.getOrderTypeId() != 5) {
                    check = false;
                    break;
                }
            }

            // ?????????????????????????????? ???????????????????????????
            if (check) {
                orderMapper.updateById(order.setOrderTypeId(10L));
            }
        }
        return Result.ok();
    }

    @Override
    public <T> Result<T> haveAll(OrderParam orderParam) {
        // ????????????
        Order order = orderMapper.selectById(orderParam.getId());

        // ???????????????
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

        // ????????????????????????????????????
        boolean check = false;
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 3) {
                check = true;
                break;
            }
        }

        if (!check) {
            return Result.fail("????????????????????????????????????");
        }

        // ????????????????????????
        for (OrderInfo orderInfo : orderInfoList) {
            if (orderInfo.getOrderTypeId() == 3) {
                haveOne(new OrderParam().setId(String.valueOf(orderInfo.getId())));
            }
        }
        return Result.ok();
    }

    @Override
    public <T> Result<T> rate(OrderParam orderParam) {

        // ????????????
        int i = userStarsMapper.insert(new UserStars()
                .setOrderInfoId(Long.parseLong(orderParam.getId()))
                .setStars(BigDecimal.valueOf(Double.parseDouble(orderParam.getStars())))
                .setCommentInfo(orderParam.getCommentInfo()));

        if (i == 1) {
            // ?????????????????????
            OrderInfo orderInfo = orderInfoMapper.selectById(orderParam.getId());

            orderInfoMapper.updateById(orderInfo.setOrderTypeId(5L));

            return Result.ok();
        } else {
            return Result.fail("????????????");
        }
    }

    @Override
    public <T> Result<T> changeOne(OrderParam orderParam) {
        // ????????????????????????
        OrderInfo orderInfo = orderInfoMapper.selectById(orderParam.getId());
        orderInfoMapper.updateById(orderInfo.setOrderTypeId(6L));

        // ??????????????????????????????
        Order order = orderMapper.selectById(orderInfo.getOrderId());
        if (order.getOrderTypeId() != 8) {
            orderMapper.updateById(order.setOrderTypeId(8L));
        }

        // ?????????????????????????????????
        CommodityDispute commodityDispute = commodityDisputeMapper.selectOne(new QueryWrapper<CommodityDispute>().eq("order_id", order.getId()));

        // ?????????????????????????????? ??????????????????
        if (commodityDispute == null) {
            // ????????????
            Long outTradeNo = System.currentTimeMillis();
            CommodityDispute commodityDisputeParent = new CommodityDispute();
            commodityDisputeMapper.insert(commodityDisputeParent
                .setDisputeNumber(String.valueOf(outTradeNo))
                .setOrderId(order.getId())
                .setDisputeStatus(0)
                .setPublishStatus(1)
                .setDeleteStatus(0));

            // ???????????????
            CommodityDisputeInfo commodityDisputeInfo = new CommodityDisputeInfo();
            commodityDisputeInfoMapper.insert(commodityDisputeInfo
                .setCommodityDisputeId(commodityDisputeParent.getId())
                .setOrderInfoId(orderInfo.getId())
                .setDisputeStatus(0)
                .setPublishStatus(1)
                .setDeleteStatus(0));

            // ???????????????
            CommodityDisputeComment commodityDisputeComment = new CommodityDisputeComment();
            commodityDisputeCommentMapper.insert(commodityDisputeComment
                .setCommodityDisputeInfoId(commodityDisputeInfo.getId())
                .setUserId(UserThreadLocal.get().getId())
                .setContext("??????"));
        } else {
            // ????????????????????? ???????????????????????????

            // ?????????????????????
            commodityDisputeMapper.updateById(commodityDispute.setDisputeStatus(0));

            // ???????????????
            CommodityDisputeInfo commodityDisputeInfo = commodityDisputeInfoMapper.selectOne(new QueryWrapper<CommodityDisputeInfo>().eq("order_info_id", orderInfo.getId()));

            // ??????????????? ???????????????
            if (commodityDisputeInfo != null) {

                commodityDisputeInfoMapper.updateById(commodityDisputeInfo.setDisputeStatus(0));

                // ????????????????????????
                commodityDisputeCommentMapper.delete(new QueryWrapper<CommodityDisputeComment>().eq("commodity_dispute_info_id", commodityDisputeInfo.getId()));

                // ???????????????
                CommodityDisputeComment commodityDisputeComment = new CommodityDisputeComment();
                commodityDisputeCommentMapper.insert(commodityDisputeComment
                        .setCommodityDisputeInfoId(commodityDisputeInfo.getId())
                        .setUserId(UserThreadLocal.get().getId())
                        .setContext("??????"));

            } else {
                // ?????????????????? ??????????????? ?????????????????????

                CommodityDisputeInfo commodityDisputeInfoChild = new CommodityDisputeInfo();

                commodityDisputeInfoMapper.insert(commodityDisputeInfoChild
                        .setCommodityDisputeId(commodityDispute.getId())
                        .setOrderInfoId(orderInfo.getId())
                        .setDisputeStatus(0)
                        .setPublishStatus(1)
                        .setDeleteStatus(0));

                // ???????????????
                CommodityDisputeComment commodityDisputeComment = new CommodityDisputeComment();
                commodityDisputeCommentMapper.insert(commodityDisputeComment
                        .setCommodityDisputeInfoId(commodityDisputeInfoChild.getId())
                        .setUserId(UserThreadLocal.get().getId())
                        .setContext("??????"));
            }

        }
        return Result.ok(BusinessCode.UPDATE_SUCCESS);
    }

    @Override
    public Result<String> allOrder() {
        Long user_id = orderMapper.selectCount(new QueryWrapper<Order>().eq("user_id", UserThreadLocal.get().getId()));
        return Result.ok(String.valueOf(user_id));
    }

    @Override
    public Result<String> allPrice() {
        List<Order> orders = orderMapper.selectList(new QueryWrapper<Order>().eq("user_id", UserThreadLocal.get().getId()));

        if (orders.size() == 0) {
            return Result.ok("0");
        }

        BigDecimal price = new BigDecimal(0);
        for (Order order : orders) {
            List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId())
                    .in("order_type_id", 4, 5));
            if (orderInfoList.size() == 0) {
                continue;
            }

            for (OrderInfo orderInfo : orderInfoList) {
                price = price.add(orderInfo.getAllPrice());
            }
        }

        return Result.ok(String.valueOf(price));
    }

    @Override
    public Result<String> allDate() {
        User user = userMapper.selectById(UserThreadLocal.get().getId());
        long time = user.getCreateTime().toLocalDate().until(LocalDate.now(), ChronoUnit.DAYS);
        return Result.ok(String.valueOf(time));
    }

    @Override
    public <T> Result<T> closePaypal(OrderParam orderParam) {
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("id", orderParam.getId()));

        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

        for (OrderInfo orderInfo : orderInfoList) {
            orderInfoMapper.updateById(orderInfo.setOrderTypeId(15L));
        }

        int i = orderMapper.updateById(order.setOrderTypeId(15L));

        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> deleteOne(OrderParam orderParam) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderParam.getId());

        int i = orderInfoMapper.updateById(orderInfo.setPublishStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }


    @Override
    public <T> Result<T> deleteAll(OrderParam orderParam) {
        Order order = orderMapper.selectById(orderParam.getId());

        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

        int i = 0;
        for (OrderInfo orderInfo : orderInfoList) {
            i += orderInfoMapper.updateById(orderInfo.setPublishStatus(0));
        }

        if (i == orderInfoList.size()) {
            i = orderMapper.updateById(order.setPublishStatus(0));

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
    public <T> Result<T> goSuccess(OrderParam orderParam) {
        // ??????????????????
        orderMapper.update(new Order().setOrderTypeId(9L), new QueryWrapper<Order>().eq("id", orderParam.getId()));

        // ????????????????????????
        Order order = orderMapper.selectOne(new QueryWrapper<Order>().eq("id", orderParam.getId()));
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", order.getId()));

        for (OrderInfo orderInfo : orderInfoList) {
            orderInfoMapper.updateById(orderInfo.setOrderTypeId(2L));
        }

        return Result.ok(BusinessCode.UPDATE_SUCCESS);
    }
}
