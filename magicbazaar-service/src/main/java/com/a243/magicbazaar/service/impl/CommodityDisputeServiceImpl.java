package com.a243.magicbazaar.service.impl;

import cn.hutool.core.util.StrUtil;
import com.a243.magicbazaar.dao.entity.*;
import com.a243.magicbazaar.dao.mapper.*;
import com.a243.magicbazaar.service.CommodityDisputeService;
import com.a243.magicbazaar.util.AlipayUtil;
import com.a243.magicbazaar.util.code.BusinessCode;
import com.a243.magicbazaar.util.convert.impl.CommodityDisputeConvert;
import com.a243.magicbazaar.util.result.Result;
import com.a243.magicbazaar.view.param.CommodityDisputeParam;
import com.a243.magicbazaar.view.vo.CommodityDisputeVo;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommodityDisputeServiceImpl implements CommodityDisputeService {
    private final CommodityDisputeMapper commodityDisputeMapper;
    private final CommodityDisputeConvert commodityDisputeConvert;
    private OrderMapper orderMapper;
    private OrderInfoMapper orderInfoMapper;
    private CommodityDisputeCommentMapper commodityDisputeCommentMapper;
    private CommodityMapper commodityMapper;
    private CommodityDisputeInfoMapper commodityDisputeInfoMapper;
    private UserMapper userMapper;

    @Autowired
    public CommodityDisputeServiceImpl(CommodityDisputeMapper commodityDisputeMapper, CommodityDisputeConvert commodityDisputeConvert) {
        this.commodityDisputeMapper = commodityDisputeMapper;
        this.commodityDisputeConvert = commodityDisputeConvert;
    }

    @Autowired
    public void setOrderMapper(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Autowired
    public void setOrderInfoMapper(OrderInfoMapper orderInfoMapper) {
        this.orderInfoMapper = orderInfoMapper;
    }

    @Autowired
    public void setCommodityDisputeCommentMapper(CommodityDisputeCommentMapper commodityDisputeCommentMapper) {
        this.commodityDisputeCommentMapper = commodityDisputeCommentMapper;
    }

    @Autowired
    public void setCommodityMapper(CommodityMapper commodityMapper) {
        this.commodityMapper = commodityMapper;
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
    public Result<List<CommodityDisputeVo>> list(CommodityDisputeParam commodityDisputeParam) {
        if (commodityDisputeParam.getPageParam() != null) {
            List<Long> orderId = new ArrayList<>();
            List<Long> orderInfoId = new ArrayList<>();
            if (!StrUtil.isBlank(commodityDisputeParam.getOrderNumber())) {
                orderId = orderMapper.selectList(new QueryWrapper<Order>().like("order_number", commodityDisputeParam.getOrderNumber())).stream().map(Order::getId).collect(Collectors.toList());
                if (orderId.size() != 0) {
                    orderInfoId = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().in("order_id", orderId)).stream().map(OrderInfo::getId).collect(Collectors.toList());
                }
            }

            IPage<CommodityDispute> iPage = commodityDisputeMapper.selectPage(new Page<>(commodityDisputeParam.getPageParam().getPage(), commodityDisputeParam.getPageParam().getPageSize()), new QueryWrapper<CommodityDispute>()
                    .in(orderId.size() != 0, "order_id", orderId)
                    .or()
                    .in(orderInfoId.size() != 0, "order_info_id", orderInfoId)
                    .eq(!StrUtil.isBlank(commodityDisputeParam.getDisputeStatus()), "dispute_status", commodityDisputeParam.getDisputeStatus())
                    .like(!StrUtil.isBlank(commodityDisputeParam.getDisputeNumber()), "dispute_number", commodityDisputeParam.getDisputeNumber())
                    .eq(!StrUtil.isBlank(commodityDisputeParam.getPublishStatus()), "publish_status", commodityDisputeParam.getPublishStatus())
                    .eq(!StrUtil.isBlank(commodityDisputeParam.getDeleteStatus()), "delete_status", commodityDisputeParam.getDeleteStatus()));

            if (!StrUtil.isBlank(commodityDisputeParam.getOrderNumber()) && orderId.size() == 0 && orderInfoId.size() == 0) {
                return Result.ok(0, null);
            }

            return Result.ok(Math.toIntExact(iPage.getTotal()), commodityDisputeConvert.convert(iPage.getRecords()));
        } else {
            return Result.ok(commodityDisputeConvert.convert(commodityDisputeMapper.selectList(new QueryWrapper<>())));
        }
    }

    @Override
    public <T> Result<T> on(CommodityDisputeParam commodityDisputeParam) {
        int i = commodityDisputeMapper.updateById(new CommodityDispute().setId(Long.parseLong(commodityDisputeParam.getId())).setPublishStatus(1));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> off(CommodityDisputeParam commodityDisputeParam) {
        int i = commodityDisputeMapper.updateById(new CommodityDispute().setId(Long.parseLong(commodityDisputeParam.getId())).setPublishStatus(0));
        if (i == 1) {
            return Result.ok(BusinessCode.UPDATE_SUCCESS);
        } else {
            return Result.fail(BusinessCode.UPDATE_ERROR);
        }
    }

    @Override
    public <T> Result<T> delete(CommodityDisputeParam commodityDisputeParam) {
        User user = userMapper.selectById(orderMapper.selectById(commodityDisputeMapper.selectById(commodityDisputeParam.getId()).getOrderId()).getUserId());

        if (user == null) {
            CommodityDispute commodityDispute = commodityDisputeMapper.selectById(commodityDisputeParam.getId());
            List<Long> commodityDisputeId = commodityDisputeInfoMapper.selectList(new QueryWrapper<CommodityDisputeInfo>().eq("commodity_dispute_id", commodityDispute.getId())).stream().map(CommodityDisputeInfo::getId).collect(Collectors.toList());

            // 纠纷交流删除
            commodityDisputeCommentMapper.delete(new QueryWrapper<CommodityDisputeComment>().in("commodity_dispute_info_id", commodityDisputeId));

            // 子纠纷订单删除
            for (Long id : commodityDisputeId) {
                commodityDisputeInfoMapper.updateById(new CommodityDisputeInfo().setId(id).setDeleteStatus(1));
            }

            // 父订单删除
            commodityDisputeMapper.updateById(commodityDispute.setDeleteStatus(1));

            return Result.ok(BusinessCode.DELETE_SUCCESS);

        } else {
            return Result.fail("该纠纷订单下还存在用户信息，无法删除");
        }
    }

    @Override
    public <T> Result<T> change(CommodityDisputeParam commodityDisputeParam) {

        // 判断子订单中是否有需要换货的订单
        List<CommodityDisputeInfo> commodityDisputeInfoList = commodityDisputeInfoMapper.selectList(new QueryWrapper<CommodityDisputeInfo>().eq("commodity_dispute_id", commodityDisputeParam.getId()));

        List<OrderInfo> infoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().in("id", commodityDisputeInfoList.stream().map(CommodityDisputeInfo::getOrderInfoId).collect(Collectors.toList())));

        boolean check = true;
        for (OrderInfo orderInfo : infoList) {
            if (orderInfo.getOrderTypeId() == 6) {
                check = false;
                break;
            }
        }

        if (check) {
            return Result.fail("该订单下没有需要换货的订单");
        }

        for (OrderInfo orderInfo : infoList) {
            if (orderInfo.getOrderTypeId() == 6) {
                changeOne(new CommodityDisputeParam().setId(String.valueOf(orderInfo.getId())));
            }
        }

        return Result.ok();
    }

    @Override
    public <T> Result<T> exit(CommodityDisputeParam commodityDisputeParam) throws AlipayApiException {
        // 判断子订单中是否有需要退货的订单
        List<CommodityDisputeInfo> commodityDisputeInfoList = commodityDisputeInfoMapper.selectList(new QueryWrapper<CommodityDisputeInfo>().eq("commodity_dispute_id", commodityDisputeParam.getId()));

        List<OrderInfo> infoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().in("id", commodityDisputeInfoList.stream().map(CommodityDisputeInfo::getOrderInfoId).collect(Collectors.toList())));

        boolean check = true;
        for (OrderInfo orderInfo : infoList) {
            if (orderInfo.getOrderTypeId() == 7) {
                check = false;
                break;
            }
        }

        if (check) {
            return Result.fail("该订单下没有需要退货的订单");
        }

        for (OrderInfo orderInfo : infoList) {
            if (orderInfo.getOrderTypeId() == 7) {
                exitOne(new CommodityDisputeParam().setId(String.valueOf(orderInfo.getId())));
            }
        }

        return Result.ok();
    }

    @Override
    public <T> Result<T> changeOne(CommodityDisputeParam commodityDisputeParam) {
        // 更改纠纷状态为已解决
        CommodityDisputeInfo commodityDisputeInfo = commodityDisputeInfoMapper.selectOne(new QueryWrapper<CommodityDisputeInfo>().eq("id", commodityDisputeParam.getId()));

        commodityDisputeInfoMapper.updateById(commodityDisputeInfo.setDisputeStatus(2));

        // 检查纠纷父订单是否需要更改状态
        List<CommodityDisputeInfo> commodityDisputeInfoList = commodityDisputeInfoMapper.selectList(new QueryWrapper<CommodityDisputeInfo>().eq("commodity_dispute_id", commodityDisputeInfo.getCommodityDisputeId()));

        boolean check = true;
        for (CommodityDisputeInfo commodityDisputeInfoChild : commodityDisputeInfoList) {
            if (commodityDisputeInfoChild.getDisputeStatus() != 2) {
                check = false;
                break;
            }
        }

        // 修改父订单状态
        CommodityDispute commodityDispute = commodityDisputeMapper.selectById(commodityDisputeInfo.getCommodityDisputeId());
        if (check) {
            commodityDisputeMapper.updateById(commodityDispute.setDisputeStatus(1));
        }

        // 其订单改变为待发货状态
        OrderInfo orderInfo = orderInfoMapper.selectById(commodityDisputeInfo.getOrderInfoId());

        orderInfoMapper.updateById(orderInfo.setOrderTypeId(2L));

        // 判断是否需要修改订单父状态
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", orderInfo.getOrderId()));

        boolean checkOrder = true;
        for (OrderInfo orderInfoChild : orderInfoList) {
            if (orderInfoChild.getOrderTypeId() != 2 && orderInfoChild.getOrderTypeId() != 3 && orderInfoChild.getOrderTypeId() != 4 && orderInfoChild.getOrderTypeId() != 5 && orderInfoChild.getOrderTypeId() != 11 &&orderInfoChild.getOrderTypeId() != 13 &&orderInfoChild.getOrderTypeId() != 14) {
                checkOrder = false;
                break;
            }
        }

        // 修改父订单状态
        Order order = orderMapper.selectById(orderInfo.getOrderId());
        if (checkOrder) {
            orderMapper.updateById(order.setOrderTypeId(9L));
        }

        return Result.ok();
    }

    @Override
    public <T> Result<T> exitOne(CommodityDisputeParam commodityDisputeParam) throws AlipayApiException {
        CommodityDisputeInfo commodityDisputeInfo = commodityDisputeInfoMapper.selectOne(new QueryWrapper<CommodityDisputeInfo>().eq("id", commodityDisputeParam.getId()));

        CommodityDispute commodityDispute = commodityDisputeMapper.selectById(commodityDisputeInfo.getCommodityDisputeId());

        OrderInfo orderInfo = orderInfoMapper.selectById(commodityDisputeInfo.getOrderInfoId());

        Order order = orderMapper.selectById(orderInfo.getOrderId());

        // 退款
        if (!AlipayUtil.exit(order.getOrderNumber(), String.valueOf(orderInfo.getAllPrice())).getSuccess()) {
            return Result.fail("退款失败");
        }

        // 更改纠纷状态为已解决
        commodityDisputeInfoMapper.updateById(commodityDisputeInfo.setDisputeStatus(2));

        // 检查纠纷父订单是否需要更改状态
        List<CommodityDisputeInfo> commodityDisputeInfoList = commodityDisputeInfoMapper.selectList(new QueryWrapper<CommodityDisputeInfo>().eq("commodity_dispute_id", commodityDisputeInfo.getCommodityDisputeId()));

        boolean check = true;
        for (CommodityDisputeInfo commodityDisputeInfoChild : commodityDisputeInfoList) {
            if (commodityDisputeInfoChild.getDisputeStatus() != 2) {
                check = false;
                break;
            }
        }

        // 修改父订单状态
        if (check) {
            commodityDisputeMapper.updateById(commodityDispute.setDisputeStatus(1));
        }

        // 其订单改变为已完成退款
        orderInfoMapper.updateById(orderInfo.setOrderTypeId(11L));

        // 判断是否需要修改订单父状态
        List<OrderInfo> orderInfoList = orderInfoMapper.selectList(new QueryWrapper<OrderInfo>().eq("order_id", orderInfo.getOrderId()));

        boolean checkOrder = true;
        for (OrderInfo orderInfoChild : orderInfoList) {
            if (orderInfoChild.getOrderTypeId() != 2 && orderInfoChild.getOrderTypeId() != 3 && orderInfoChild.getOrderTypeId() != 4 && orderInfoChild.getOrderTypeId() != 5 && orderInfoChild.getOrderTypeId() != 11 &&orderInfoChild.getOrderTypeId() != 13 &&orderInfoChild.getOrderTypeId() != 14) {
                checkOrder = false;
                break;
            }
        }

        // 修改父订单状态
        if (checkOrder) {
            orderMapper.updateById(order.setOrderTypeId(9L));
        }

        boolean checkSuccess = true;
        for (OrderInfo orderInfoChild : orderInfoList) {
            if (orderInfoChild.getOrderTypeId() != 4 && orderInfoChild.getOrderTypeId() != 5 && orderInfoChild.getOrderTypeId() != 11 &&orderInfoChild.getOrderTypeId() != 13 &&orderInfoChild.getOrderTypeId() != 14) {
                checkSuccess = false;
                break;
            }
        }

        if (checkSuccess) {
            orderMapper.updateById(order.setOrderTypeId(10L));
        }

        return Result.ok();
    }

    @Override
    public Result<String> disputeCounts() {
        Long toDayRegisterCount = commodityDisputeMapper.selectCount(new QueryWrapper<CommodityDispute>().eq("Date(create_time)", LocalDate.now()));
        Long lastDayRegister = commodityDisputeMapper.selectCount(new QueryWrapper<CommodityDispute>().eq("Date(create_time)", LocalDate.now().plusDays(-1)));
        Long selectCount = commodityDisputeMapper.selectCount(new QueryWrapper<CommodityDispute>().between("Date(create_time)", LocalDate.now().plusDays(-7), LocalDate.now()));

        return Result.ok(toDayRegisterCount + "," + (toDayRegisterCount - lastDayRegister) + "," + (toDayRegisterCount - selectCount));
    }
}
